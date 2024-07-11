package com.csis4175.zenith;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MeditationTimer extends View {

    private static final long INHALE_TIME = 4000; // 4 seconds in milliseconds
    private static final long HOLD_TIME = 7000;   // 7 seconds in milliseconds
    private static final long EXHALE_TIME = 8000; // 8 seconds in milliseconds

    private static final long TOTAL_TIME = INHALE_TIME + HOLD_TIME + EXHALE_TIME;

    private long startTime;
    private Paint circlePaint;
    private Paint arcPaint;
    private Paint textPaint;
    private String phase = "Inhale";
    private Timer timer;
    private boolean isAnimating = false;

    public MeditationTimer(Context context) {
        super(context);
        init();
    }

    public MeditationTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeditationTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.LTGRAY);
        circlePaint.setStyle(Paint.Style.FILL);

        arcPaint = new Paint();
        arcPaint.setColor(Color.BLUE);
        arcPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void startAnimation() {
        if (timer != null) {
            timer.cancel();
        }
        isAnimating = true;
        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                postInvalidate(); // Request the view to be redrawn on the UI thread
            }
        }, 0, 30); // Update every 30 milliseconds
    }

    public void stopAnimation() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        isAnimating = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isAnimating) {
            return; // Skip drawing if animation is not running
        }

        long elapsed = System.currentTimeMillis() - startTime;
        double progress;

        if (elapsed < INHALE_TIME) {
            phase = "Inhale";
            progress = (double) elapsed / INHALE_TIME;
        } else if (elapsed < INHALE_TIME + HOLD_TIME) {
            phase = "Hold";
            progress = (double) (elapsed - INHALE_TIME) / HOLD_TIME;
        } else if (elapsed < TOTAL_TIME) {
            phase = "Exhale";
            progress = (double) (elapsed - INHALE_TIME - HOLD_TIME) / EXHALE_TIME;
        } else {
            startTime = System.currentTimeMillis(); // Reset the cycle
            phase = "Inhale";
            progress = 0;
        }

        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height) - 40;
        int radius = diameter / 2;
        int cx = width / 2;
        int cy = height / 2;

        // Draw background circle
        canvas.drawCircle(cx, cy, radius, circlePaint);

        // Draw progress arc
        canvas.drawArc(cx - radius, cy - radius, cx + radius, cy + radius, 270, (float) (360 * progress), true, arcPaint);

        // Draw phase text
        canvas.drawText(phase, cx, cy + (textPaint.getTextSize() / 4), textPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timer != null) {
            timer.cancel();
        }
    }
}
