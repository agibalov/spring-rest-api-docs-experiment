package me.loki2302.dto;

import io.swagger.annotations.ApiModel;

@ApiModel(
        value = "Not found error details", // OK
        description = "Describes not found error details" // FAIL: Swagger doesn't display this anywhere
)
public class NotFoundErrorDto extends ErrorDto {
    public NotFoundErrorDto() {
        super("NOT_FOUND", "What you're referring to, does not exist");
    }
}
