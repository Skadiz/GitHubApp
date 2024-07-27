package com.skadiz.GitHubRestApp.exceptions;

public class GitHubApiException extends RuntimeException{
    public GitHubApiException(String message) {
        super(message);
    }
}
