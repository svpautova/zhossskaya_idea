package com.example.myapplication;
import com.example.myapplication.Photo;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface PexelApi {
    @Headers("Authorization: " + "563492ad6f91700001000001adde317a314948a09054ec793ef96f19")
    @GET("curated")
    Call<List<Photo>> getCurated(@Query("per_page") int itemNum, @Query("page") int numPage);

    @Headers("Authorization: " + "563492ad6f91700001000001adde317a314948a09054ec793ef96f19")
    @GET("search")
    Call<List<Photo>> getSearch(@Query("query") String query, @Query("per_page") int itemNum, @Query("page") int numPage);
}
