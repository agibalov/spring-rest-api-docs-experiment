package me.loki2302;

import org.springframework.http.ResponseEntity;

public abstract class ApiException extends RuntimeException {
    abstract public ResponseEntity getResponseEntity();
}
