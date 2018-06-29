package com.android.sampleapp2.Conectivity;

import com.android.sampleapp2.Model.Response;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by rahul.kalamkar on 6/25/2018.
 */

public interface API {
    @GET("{JSON}")
    Call<Response> getData(@Path(value = "JSON", encoded = true) String path, @QueryMap() HashMap<String, String> map);
}
