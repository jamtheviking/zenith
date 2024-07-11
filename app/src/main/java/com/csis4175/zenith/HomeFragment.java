package com.csis4175.zenith;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class HomeFragment extends Fragment {

    private TextView userInfoTextView, txvQuote, txvAuthor;
    private Button btnLogout;

    private Handler handler;
    private Runnable quoteUpdater;
    private QuoteViewModel quoteViewModel;

    private static final long ONE_HOUR_MILLIS = 3600000; // 1 hour in milliseconds
    private static final long ONE_MIN_IN_MS = 60000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        userInfoTextView = view.findViewById(R.id.tvWelcomeName);
        btnLogout = view.findViewById(R.id.btnLogout);
        txvQuote = view.findViewById(R.id.quoteTextView);
        txvAuthor = view.findViewById(R.id.authorTextView);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });

        quoteViewModel = new ViewModelProvider(this).get(QuoteViewModel.class);
        displaySavedQuote();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null && activity.getUser() != null) {
            displayUserInfo(activity.getUser().getEmail());
        }
    }



    public void displayUserInfo(String email) {
        if (userInfoTextView != null) {
            userInfoTextView.setText("Hi, " + email);
        }
    }

    private void performLogout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Fetch quote
        if (shouldFetchNewQuote()) {
            fetchAndDisplayQuote();
        }
        //update quote per hour
        handler = new Handler(Looper.getMainLooper());
        quoteUpdater = new Runnable() {
            @Override
            public void run() {
                fetchAndDisplayQuote();
                handler.postDelayed(this, ONE_HOUR_MILLIS);
            }
        };
        handler.postDelayed(quoteUpdater, ONE_HOUR_MILLIS);
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop updates when not needed
        if (handler != null && quoteUpdater != null) {
            handler.removeCallbacks(quoteUpdater);
        }
    }

    private boolean shouldFetchNewQuote() {
        long currentTime = System.currentTimeMillis();
        return (quoteViewModel.getLastFetchTime() == 0 ||
                (currentTime - quoteViewModel.getLastFetchTime() > ONE_HOUR_MILLIS));
    }

    private void displaySavedQuote() {
        txvQuote.setText(quoteViewModel.getCurrentQuote());
        txvQuote.setText("- " + quoteViewModel.getCurrentAuthor());
    }

    private void fetchAndDisplayQuote() {
        QuotesFetcher.fetchQuote(new QuotesFetcher.QuoteListener() {
            @Override
            public void onQuoteFetched(final String quote, final String author) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txvQuote.setText(quote);
                        txvAuthor.setText("- " + author);
                    }
                });
            }

            @Override
            public void onError(String error) {
                Log.e("QuoteFetcher", "Error fetching quote: " + error);
            }
        });
    }
}
