package com.example.t_r_ip.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationSearchResult {
    @SerializedName("Search")
    List<Location> search;

    public List<Location> getSearch() {
        return search;
    }

    public void setSearch(List<Location> search) {
        this.search = search;
    }
}
