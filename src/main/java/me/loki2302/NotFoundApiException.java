package me.loki2302;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class NotFoundApiException extends ApiException {
    @Override
    public ResponseEntity getResponseEntity() {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
