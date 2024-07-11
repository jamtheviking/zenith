package com.csis4175.zenith;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;


public class MeditateFragment extends Fragment {

    private static final String TAG = "MeditateFragment";
    private MeditationTimer meditationTimer;
    private ExoPlayer exoPlayer;
    private static final String CLIENT_ID = "9dfb4865"; //Jamendo Client ID
    private static final String TRACK_ID = "1890501"; // Jamendo track ID

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meditate, container, false);
        meditationTimer = view.findViewById(R.id.breathingCircleView);
        Button startButton = view.findViewById(R.id.startButton);

        exoPlayer = new ExoPlayer.Builder(getContext()).build();

        // Set button click listener
        startButton.setOnClickListener(v -> {
            if (meditationTimer.getVisibility() == View.INVISIBLE) {
                meditationTimer.setVisibility(View.VISIBLE); // Show the circle view if initially invisible
            }
            playJamendoTrack();
            meditationTimer.startAnimation();

        });

        return view;
    }

    // Method to play music from Jamendo
    private void playJamendoTrack() {
        String jamendoTrackUrl = "https://api.jamendo.com/v3.0/tracks/file/?client_id=" + CLIENT_ID + "&id=" + TRACK_ID;

        Log.d(TAG, "Playing track: " + jamendoTrackUrl);

        MediaItem mediaItem = MediaItem.fromUri(jamendoTrackUrl);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Log.e(TAG, "ExoPlayer error: " + error.getMessage());
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Log.d(TAG, "Playback state changed: " + playbackState);
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        Log.d(TAG, "Buffering...");
                        break;
                    case Player.STATE_READY:
                        Log.d(TAG, "Ready to play!");
                        break;
                    case Player.STATE_ENDED:
                        Log.d(TAG, "Playback ended.");
                        break;
                    case Player.STATE_IDLE:
                        Log.d(TAG, "Player idle.");
                        break;
                }
            }
        });
    }

    //Test music
    private void playTestTrack() {
        String testUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"; // Example of a public MP3 URL

        MediaItem mediaItem = MediaItem.fromUri(testUrl);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}