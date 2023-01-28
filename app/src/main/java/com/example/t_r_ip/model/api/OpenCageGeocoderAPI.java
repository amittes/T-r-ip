package com.example.t_r_ip.model.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenCageGeocoderAPI {
    @GET("geocode/v1/json")
    Call<LocationSearchResult> search(@Query("q") String query,
                                      @Query("limit") int limit,
                                      @Query("key") String key);
}
