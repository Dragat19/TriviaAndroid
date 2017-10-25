package com.playtown.triviaplay.interfaces;


public interface Presenter<T> {
    void attachMvpView(T mvpView);
    void detachMvpView();
}
