package me.loki2302.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel(
        value = "Note data transfer object", // OK
        description = "Note dto description" // FAIL: Swagger doesn't seem to display it anywhere
)
public class NoteAttributesDto {
    @ApiModelProperty(
            value = "Note text", // OK
            notes = "text notes", // FAIL: Swagger doesn't seem to display it anywhere
            example = "text example" // FAIL: Swagger doesn't seem to display it anywhere
    )
    @NotBlank
    public String text;
}
