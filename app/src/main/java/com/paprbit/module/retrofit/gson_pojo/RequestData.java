package com.paprbit.module.retrofit.gson_pojo;

/**
 * Created by ankush38u on 3/12/2016.
 */
public class RequestData {
    private int id;
    private String pid;
    private int status;
    private int quantity;
    private String uid;

    public String getCarno() {
        return carno;
    }

    private String carno;

    public int getId() {
        return id;
    }

    public String getPid() {
        return pid;
    }

    public int getStatus() {
        return status;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUid() {
        return uid;
    }
}
