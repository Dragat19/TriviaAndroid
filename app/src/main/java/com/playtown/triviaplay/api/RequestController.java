package com.playtown.triviaplay.api;

import android.content.Context;

import com.playtown.triviaplay.BaseApplication;
import com.playtown.triviaplay.utils.Utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class RequestController {

    private final static String BASE_URL_CONTROL = "http://control.playtown.com.ar/";
    private final static String BASE_URL_APPS = "http://apps.playtown.mx/set_br/";
    private static final int TIMEOUT_MILLIS = 10000;
    private static final TimeUnit TIMEOUT_UNIT = TimeUnit.MILLISECONDS;
    private static RequestApiEndpoints apiServiceAsync;
    private static RequestApiEndpoints apiServiceAsyncControl;
    private static RequestController instance;

    private RequestController(Context context) {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        Retrofit retrofitAsync = new Retrofit.Builder()
                .baseUrl(BASE_URL_APPS)
                .client(createDefaultOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();

        apiServiceAsync = retrofitAsync.create(RequestApiEndpoints.class);

        Retrofit retrofitAsyncControl = new Retrofit.Builder()
                .baseUrl(BASE_URL_CONTROL)
                .client(createDefaultOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();

        apiServiceAsyncControl = retrofitAsyncControl.create(RequestApiEndpoints.class);
    }

    public static RequestController getInstance(Context context) {
        if (instance == null) {
            instance = new RequestController(context);
        }
        return instance;
    }

    private OkHttpClient createDefaultOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient().newBuilder()
                .cache(new Cache(BaseApplication.getInstance().getCacheDir(), 10 * 1024 * 1024)) // 10 MB
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (Utils.hasInternet(BaseApplication.getInstance().getInstance())) {
                            request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
                        } else {
                            request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24).build();
                        }
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(TIMEOUT_MILLIS, TIMEOUT_UNIT)
                .readTimeout(TIMEOUT_MILLIS, TIMEOUT_UNIT)
                .writeTimeout(TIMEOUT_MILLIS, TIMEOUT_UNIT)
                .addInterceptor(interceptor)
                .build();
    }

    /*public Observable<NewsResponse> getHomeNews(int nextPage, String oneSignalId) {
        return apiServiceAsync.getHomeNews(nextPage, oneSignalId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NewsResponse> getFavNews(int nextPage, String oneSignalId) {
        return apiServiceAsync.getFavNews(nextPage, oneSignalId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NewsResponse> getInterNews(int nextPage, String oneSignalId) {
        return apiServiceAsync.getInterNews(nextPage, oneSignalId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NewsDetailResponse> getNewsDetail(int newsId) {
        return apiServiceAsync.getNewsDetail(newsId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<UserDataResponse> getInit(String oneSignalId) {
        return apiServiceAsync.getInit(oneSignalId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }*/

}
