package br.com.wswork.module.customers.controllers;

import br.com.wswork.module.customers.configs.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatusCode(), ex.getStatusText(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getStatusCode()));
    }


    public static class ErrorResponse {
        private final int statusCode;
        private final String statusText;
        private final String message;

        public ErrorResponse(int statusCode, String statusText, String message) {
            this.statusCode = statusCode;
            this.statusText = statusText;
            this.message = message;
        }

        public String getStatusText() {
            return statusText;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getMessage() {
            return message;
        }
    }
}
