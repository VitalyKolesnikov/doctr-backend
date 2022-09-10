package ru.kvs.doctrspring.adapters.restapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorRepresentation {
    private Integer statusCode;
    private String errorCode;
    private String message;
    private String requestURI;
    private String httpMethod;
    private Date timestamp = new Date();
    private String errorTicketId;

    public ErrorRepresentation() {
    }

    public static void fillWithCommonValues(ErrorRepresentation representation, HttpServletRequest request) {
        representation.setRequestURI(request.getRequestURI());
        representation.setHttpMethod(request.getMethod());
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public ErrorRepresentation setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public ErrorRepresentation setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public ErrorRepresentation setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getRequestURI() {
        return this.requestURI;
    }

    public ErrorRepresentation setRequestURI(String requestURI) {
        this.requestURI = requestURI;
        return this;
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }

    public ErrorRepresentation setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public ErrorRepresentation setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getErrorTicketId() {
        return this.errorTicketId;
    }

    public ErrorRepresentation setErrorTicketId(String errorTicketId) {
        this.errorTicketId = errorTicketId;
        return this;
    }

}
