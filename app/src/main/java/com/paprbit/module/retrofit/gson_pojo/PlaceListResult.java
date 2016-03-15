package com.paprbit.module.retrofit.gson_pojo;

import java.util.List;

/**
 * Created by ankush38u on 3/15/2016.
 */
public class PlaceListResult {
    public String status;
    public List<Place> results;

    public String getStatus() {
        return status;
    }

    public List<Place> getResults() {
        return results;
    }
}
