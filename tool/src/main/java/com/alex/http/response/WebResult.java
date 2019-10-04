package com.alex.http.response;

import com.google.gson.GsonBuilder;

public class WebResult {
    Integer status;
    String message;
    Object result;

    public WebResult(Integer status, String message, Object result) {
        this.setStatus(status);
        this.setMessage(message);
        this.setResult(result);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return new GsonBuilder().serializeNulls().disableHtmlEscaping().create().toJson(this);
        //return new Gson().toJson(this);
    }

    public static String GetResultString(Integer status, String message, Object result) {

        //return new WebResult(status, message, result).toString();
       return new WebResult(status, message, result).toString();
    }

}