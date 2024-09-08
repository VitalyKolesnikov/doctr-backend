package ru.kvs.doctrspring.adapters.restapi;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ErrorRepresentation;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandlers {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseBody
    public ResponseEntity<ErrorRepresentation> handleNoSuchElementException(NoSuchElementException e, HttpServletRequest request) {
        String message = e.getMessage();
        log.warn("{}, request URI: {}", message, request.getRequestURI(), e);

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ErrorRepresentation error = new ErrorRepresentation()
                .setStatusCode(httpStatus.value())
                .setMessage(message);

        return ResponseEntity.status(httpStatus).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorRepresentation> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = "Bad request, please check your data";
        log.warn("{}, request URI: {}", message, request.getRequestURI(), e);

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorRepresentation error = new ErrorRepresentation()
                .setStatusCode(httpStatus.value())
                .setMessage(message);

        return ResponseEntity.status(httpStatus).body(error);
    }

}
