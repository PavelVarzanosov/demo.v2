package com.example.demo;

public class Response {
    private String status;
    private Integer code;

    public Response(String status, Integer code) {
        this.status = status;
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }
}
