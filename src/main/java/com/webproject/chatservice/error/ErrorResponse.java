package com.webproject.chatservice.error;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {

    private String code;

    private String description;

    private String errorMessage;

    public ErrorResponse(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public ErrorResponse(String code, String description, String errorMessage) {
        this.code = code;
        this.description = description;
        this.errorMessage = errorMessage;
    }

}
