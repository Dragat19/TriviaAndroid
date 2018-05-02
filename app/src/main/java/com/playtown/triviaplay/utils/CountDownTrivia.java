package com.playtown.triviaplay.utils;

import android.os.CountDownTimer;

/**
 * Created by albertsanchez on 26/10/17.
 */

public class CountDownTrivia extends CountDownTimer {

    private onListenerCountDown listener;
    private static final String COUNTDOWN = "0";

    public interface onListenerCountDown {
        void onCountDownTriviaFinish(String finish);
        void onShowCountDownTrivia(long countDownTrivia);
    }

    public CountDownTrivia(long millisInFuture, long countDownInterval, onListenerCountDown listener) {
        super(millisInFuture, countDownInterval);
        this.listener = listener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        long segundos = millisUntilFinished / 1000;
        listener.onShowCountDownTrivia(segundos);
    }

    @Override
    public void onFinish() {
        listener.onCountDownTriviaFinish(COUNTDOWN);
    }
}
