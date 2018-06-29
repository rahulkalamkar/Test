package com.android.sampleapp2.Conectivity;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rahul.kalamkar on 11/23/2017.
 */

public class APIClient {

    public static String BASE_URL = "https://stark-spire-93433.herokuapp.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        BASE_URL = baseUrl;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

}
