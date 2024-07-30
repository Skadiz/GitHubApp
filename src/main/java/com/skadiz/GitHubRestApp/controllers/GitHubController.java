package com.skadiz.GitHubRestApp.controllers;

import com.skadiz.GitHubRestApp.dto.BranchDto;
import com.skadiz.GitHubRestApp.dto.RepositoryDto;
import com.skadiz.GitHubRestApp.services.GitHubService;
import com.skadiz.GitHubRestApp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Rest Controller for handling GitHub repository requests.
 */
@RestController
@RequestMapping("/api")
public class GitHubController {
    private final GitHubService gitHubService;

    /**
     * Constructs a new GitHubController with the given GitHubService.
     *
     * @param gitHubService the service to interact with GitHub API
     */
    @Autowired
    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    /**
     * Retrieves the non-fork repositories of a given GitHub user along with their branches and last commit SHAs.
     *
     * @param username the GitHub username
     * @param acceptHeader the Accept header, should be "application/json"
     * @return a ResponseEntity containing the list of repositories with branches or an error message
     */
    @GetMapping("/repositories/{username}")
    public ResponseEntity<?> getRepositories(@PathVariable String username, @RequestHeader("Accept") String acceptHeader) {
        if (!"application/json".equals(acceptHeader)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Accept header must be 'application/json'");
        }

        try {
            List<RepositoryDto> repositories = gitHubService.getUserRepositories(username);
            for (RepositoryDto repository : repositories) {
                List<BranchDto> branches = gitHubService.getRepositoryBranches(username, repository.getName());
                repository.setBranches(branches);
            }
            return ResponseEntity.ok(repositories);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", "User not found"));
        }
    }
}
