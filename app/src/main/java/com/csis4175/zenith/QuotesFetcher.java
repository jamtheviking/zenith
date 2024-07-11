package com.csis4175.zenith;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuotesFetcher {

    private static final String API_URL = "https://zenquotes.io/api/random";

    public interface QuoteListener {
        void onQuoteFetched(String quote, String author);
        void onError(String error);
    }

    public static void fetchQuote(final QuoteListener listener) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    if (listener != null) {
                        listener.onError("Unexpected code " + response);
                    }
                    return;
                }

                String responseData = response.body().string();
                try {
                    Gson gson = new Gson();
                    JsonArray jsonArray = gson.fromJson(responseData, JsonArray.class);
                    JsonObject quoteObject = jsonArray.get(0).getAsJsonObject();
                    String quote = quoteObject.get("q").getAsString();
                    String author = quoteObject.get("a").getAsString();

                    if (listener != null) {
                        listener.onQuoteFetched(quote, author);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e.getMessage());
                    }
                }
            }
        });
    }
}