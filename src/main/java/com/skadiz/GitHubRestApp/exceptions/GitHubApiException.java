package com.skadiz.GitHubRestApp.exceptions;

/**
 * Custom exception to handle errors related to GitHub API interactions.
 */
public class GitHubApiException extends RuntimeException {

    /**
     * Constructs a new GitHubApiException with the specified detail message.
     *
     * @param message the detail message describing the error
     */
    public GitHubApiException(String message) {
        super(message);
    }
}
