package com.skadiz.GitHubRestApp.util;

import com.skadiz.GitHubRestApp.exceptions.GitHubApiException;
import com.skadiz.GitHubRestApp.exceptions.RateLimitExceededException;
import com.skadiz.GitHubRestApp.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

/**
 * Global exception handler for handling exceptions across the whole application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles UserNotFoundException by returning a 404 response with the error message.
     *
     * @param ex the UserNotFoundException instance
     * @return a ResponseEntity containing the status and error message
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("status", HttpStatus.NOT_FOUND.value(), "message", ex.getMessage()));
    }

    /**
     * Handles GitHubApiException by returning a 500 response with the error message.
     *
     * @param ex the GitHubApiException instance
     * @return a ResponseEntity containing the status and error message
     */
    @ExceptionHandler(GitHubApiException.class)
    public ResponseEntity<?> handleGitHubApiException(GitHubApiException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("status", HttpStatus.INTERNAL_SERVER_ERROR.value(), "message", ex.getMessage()));
    }

    /**
     * Handles RateLimitExceededException by returning a 403 response with the error message.
     *
     * @param ex the RateLimitExceededException instance
     * @return a ResponseEntity containing the status and error message
     */
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<?> handleRateLimitExceededException(RateLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("status", HttpStatus.FORBIDDEN.value(), "message", ex.getMessage()));
    }

    /**
     * Handles HttpClientErrorException by returning an appropriate response based on the HTTP status code.
     *
     * @param ex the HttpClientErrorException instance
     * @return a ResponseEntity containing the status and error message
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleHttpClientErrorException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("status", HttpStatus.FORBIDDEN.value(), "message", "API rate limit exceeded. Please try again later."));
        }
        return ResponseEntity.status(ex.getStatusCode())
                .body(Map.of("status", ex.getStatusCode().value(), "message", ex.getResponseBodyAsString()));
    }

    /**
     * Handles any other generic exceptions by returning a 500 response with a generic error message.
     *
     * @param ex the Exception instance
     * @return a ResponseEntity containing the status and error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("status", HttpStatus.INTERNAL_SERVER_ERROR.value(), "message", "An unexpected error occurred"));
    }
}
