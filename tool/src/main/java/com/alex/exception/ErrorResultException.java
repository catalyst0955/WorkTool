package com.alex.exception;

import com.alex.http.response.WebResult;

public class ErrorResultException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Integer status = 0;
    private Object result;

    public ErrorResultException(Integer status, String errorMessage, Throwable err) {
        super(errorMessage, err);
        this.setStatus(status);
    }

    public ErrorResultException(Integer status, String errorMessage, Object result, Throwable err) {
        super(errorMessage, err);
        this.setStatus(status);
        this.setResult(result);
    }

    public ErrorResultException(WebResult wr, Throwable err) {
        super(wr.getMessage(), err);
        this.setStatus(wr.getStatus());
        this.setResult(wr.getResult());
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}