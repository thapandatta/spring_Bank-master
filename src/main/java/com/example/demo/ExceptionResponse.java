package com.example.demo;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String message;
    private String code;

    public ExceptionResponse(String message, String code) {
        this();
        this.message = message;
        this.code = code;
    }

    public ExceptionResponse() {

    }
}
