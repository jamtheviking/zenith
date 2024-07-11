package com.csis4175.zenith;

import androidx.lifecycle.ViewModel;

public class QuoteViewModel extends ViewModel {
    private String currentQuote;
    private String currentAuthor;
    private long lastFetchTime;

    public String getCurrentQuote() {
        return currentQuote;
    }

    public void setCurrentQuote(String currentQuote) {
        this.currentQuote = currentQuote;
    }

    public String getCurrentAuthor() {
        return currentAuthor;
    }

    public void setCurrentAuthor(String currentAuthor) {
        this.currentAuthor = currentAuthor;
    }

    public long getLastFetchTime() {
        return lastFetchTime;
    }

    public void setLastFetchTime(long lastFetchTime) {
        this.lastFetchTime = lastFetchTime;
    }
}