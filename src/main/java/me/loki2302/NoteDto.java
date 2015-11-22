package me.loki2302;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = "Note data transfer object", // OK
        description = "Note dto description" // FAIL: Swagger doesn't seem to display it anywhere
)
public class NoteDto extends NoteAttributesDto {
    @ApiModelProperty(
            value = "Note id", // OK
            notes = "id notes", // FAIL: Swagger doesn't seem to display it anywhere
            example = "id example" // FAIL: Swagger doesn't seem to display it anywhere
    )
    public String id;
}
