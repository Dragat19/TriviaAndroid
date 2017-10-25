package com.playtown.triviaplay.mvp.view;


public interface MvpView {
    void showEmptyState();
    void hideEmptyState(boolean showProgress);
    void showProgress();
    void hideProgress();
}
