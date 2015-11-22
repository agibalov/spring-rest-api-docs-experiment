package me.loki2302.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@ApiModel(
        value = "Validation error details", // OK
        description = "Describes which attributes were invalid" // FAIL: Swagger doesn't display this anywhere
)
public class ValidationErrorDto extends ErrorDto {
    @ApiModelProperty(
            value = "A map of attribute names and their corresponding error messages" // OK
    )
    public Map<String, String> errorFields;

    public ValidationErrorDto(Map<String, String> errorFields) {
        super("VALIDATION", "Something is terribly wrong with your request");
        this.errorFields = errorFields;
    }
}
