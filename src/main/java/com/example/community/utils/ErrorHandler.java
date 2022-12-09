package com.example.community.utils;

import com.example.community.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<?> IndexOutOfBoundsExceptionHandler(IndexOutOfBoundsException exception){

        log.info("==========");
        log.info("IndexOutOfBoundsException Pointer = {}", exception.toString());
        log.info("==========");

        log.error(exception.toString());

        Response response = new Response();
        response.setMessage("INTERNAL_SERVER_ERROR");
        response.setData(exception.toString());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<?> ClassNotFoundExceptionHandler(ClassNotFoundException exception){

        log.info("==========");
        log.info("ClassNotFoundException Pointer = {}", exception.toString());
        log.info("==========");

        log.error(exception.toString());

        Response response = new Response();
        response.setMessage("INTERNAL_SERVER_ERROR");
        response.setData(exception.toString());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> SQLExceptionHandler(SQLException exception){

        log.info("==========");
        log.info("SQLException Pointer = {}", exception.toString());
        log.info("==========");

        log.error(exception.toString());

        Response response = new Response();
        response.setMessage("INTERNAL_SERVER_ERROR");
        response.setData(exception.toString());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
