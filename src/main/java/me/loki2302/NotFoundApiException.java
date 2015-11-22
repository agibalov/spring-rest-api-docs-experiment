package me.loki2302;

import me.loki2302.dto.NotFoundErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class NotFoundApiException extends ApiException {
    @Override
    public ResponseEntity getResponseEntity() {
        return new ResponseEntity(new NotFoundErrorDto(), HttpStatus.NOT_FOUND);
    }
}
