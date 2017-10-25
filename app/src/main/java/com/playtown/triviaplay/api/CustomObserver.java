package com.playtown.triviaplay.api;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;

public abstract class CustomObserver<T> implements Observer<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof HttpException) {
            try {
                onHttpErrorCode(((HttpException) e).code(), ((HttpException) e).message());

            } catch (Exception e1) {
                e1.printStackTrace();
                onUnknownError();
            }
        } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
            onNoInternetConnection();
        } else if (e instanceof SocketTimeoutException) {
            onTimeOut();
        } else {
            onUnknownError();
        }
    }

    @Override
    public void onNext(T t) {

    }

    public abstract void onNoInternetConnection();

    public abstract void onUnknownError();

    public abstract void onTimeOut();

    public abstract void onHttpErrorCode(int errorCode, String meg);


}
