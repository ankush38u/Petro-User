package com.paprbit.module.retrofit.gson_pojo;

/**
 * Created by ankush38u on 1/4/2016.
 */
public class LoginResponse {
    boolean type;
    String message;
    String session_id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
    @Override
    public String toString() {
        return "LoginResponse{" +
                "message='" + message + '\'' +
                ", type=" + type +
                ", session_id='" + session_id + '\'' +
                '}';
    }
}
