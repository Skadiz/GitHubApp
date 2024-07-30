package com.skadiz.GitHubRestApp.exceptions;

/**
 * Custom exception to handle cases where the GitHub API rate limit is exceeded.
 */
public class RateLimitExceededException extends RuntimeException {

    /**
     * Constructs a new RateLimitExceededException with the specified detail message.
     *
     * @param message the detail message describing the error
     */
    public RateLimitExceededException(String message) {
        super(message);
    }
}

