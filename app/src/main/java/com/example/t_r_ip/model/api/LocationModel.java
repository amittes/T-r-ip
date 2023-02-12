package com.example.t_r_ip.model.api;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationModel {
    final public static LocationModel instance = new LocationModel();

    final String BASE_URL = "https://api.opencagedata.com/";
    final String KEY = "071075bb21894de58c0238738d9e4a2c";
    final int LIMIT = 5;
    Retrofit retrofit;
    OpenCageGeocoderAPI openCageGeocoderAPI;

    private LocationModel() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        openCageGeocoderAPI = retrofit.create(OpenCageGeocoderAPI.class);
    }

    public LiveData<List<Location>> searchLocationByName(String name) {
        MutableLiveData<List<Location>> data = new MutableLiveData<>();
        Call<LocationSearchResult> call = openCageGeocoderAPI.search(name, LIMIT, KEY);
        call.enqueue(new Callback<LocationSearchResult>() {
            @Override
            public void onResponse(Call<LocationSearchResult> call, Response<LocationSearchResult> response) {
                if (response.isSuccessful()) {
                    LocationSearchResult res = response.body();
                    data.setValue(res.getResults());
                } else {
                    data.setValue(null);
                    Log.d("TAG", "-------- searchLocation response error --------");
                }
            }

            @Override
            public void onFailure(Call<LocationSearchResult> call, Throwable t) {
                data.setValue(null);
                Log.d("TAG", "-------- searchLocation fail --------");
            }
        });
        return data;
    }
}

