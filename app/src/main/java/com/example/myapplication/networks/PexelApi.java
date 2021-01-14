package com.example.myapplication.networks;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface PexelApi {

    @Headers("Authorization: " + "563492ad6f9170000100000111e516782a474c0a8982dd456f317a7f")
    @GET("search")
    Call<List<String>> getSearch(@Query("query") String query, @Query("per_page") int itemNum, @Query("page") int numPage);
}
