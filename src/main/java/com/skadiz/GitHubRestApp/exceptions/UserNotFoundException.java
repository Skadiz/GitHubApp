package com.skadiz.GitHubRestApp.exceptions;

/**
 * Custom exception to handle cases where a GitHub user is not found.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     *
     * @param message the detail message describing the error
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}

