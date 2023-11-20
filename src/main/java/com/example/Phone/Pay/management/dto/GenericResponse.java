package com.example.Phone.Pay.management.dto;

import lombok.Data;

@Data
public class GenericResponse {
    private int code;

    private String status;

    private String message;

    private Object payLoad;

    public GenericResponse(int code, String message, String status) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public GenericResponse(int code, Object payLoad) {
        this.code = code;
        this.payLoad = payLoad;
    }

    public GenericResponse() {
        super();

    }
}
