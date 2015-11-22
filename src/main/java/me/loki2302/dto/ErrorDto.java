package me.loki2302.dto;

import io.swagger.annotations.ApiModelProperty;

public abstract class ErrorDto {
    @ApiModelProperty
    public String error;

    @ApiModelProperty
    public String message;

    protected ErrorDto(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
