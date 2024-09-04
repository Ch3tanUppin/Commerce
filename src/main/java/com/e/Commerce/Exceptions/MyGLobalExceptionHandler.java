package com.e.Commerce.Exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.e.Commerce.payload.ApiRespose;

@RestControllerAdvice
public class MyGLobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField();
            String message = err.getDefaultMessage();
            response.put(fieldName, message);
        });
        return new ResponseEntity<Map<String, String>>(response,HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ResoucreNotFoundException.class)
    public ResponseEntity<ApiRespose> myRecourceNotFoundException(ResoucreNotFoundException e){
        String message = e.getMessage();
        ApiRespose apiRespose = new ApiRespose(message, false);
        return new ResponseEntity<>(apiRespose, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ApiExecption.class)
    public ResponseEntity<ApiRespose> myApiExecption(ApiExecption e){
        String message = e.getMessage();
        ApiRespose apiRespose = new ApiRespose(message, false);
        return new ResponseEntity<>(apiRespose, HttpStatus.NOT_FOUND);
    }
}
    
