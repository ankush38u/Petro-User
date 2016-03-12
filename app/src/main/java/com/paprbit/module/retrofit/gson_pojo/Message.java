package com.paprbit.module.retrofit.gson_pojo;

/**
 * Created by ankush38u on 12/27/2015.
 */
public class Message {
    boolean type;
    String message;

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", message='" + message + '\'' +
                '}';
    }
}
