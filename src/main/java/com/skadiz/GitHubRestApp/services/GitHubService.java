package com.skadiz.GitHubRestApp.services;

import com.skadiz.GitHubRestApp.dto.BranchDto;
import com.skadiz.GitHubRestApp.dto.RepositoryDto;
import com.skadiz.GitHubRestApp.exceptions.GitHubApiException;
import com.skadiz.GitHubRestApp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for interacting with the GitHub API.
 * This class contains methods to retrieve repositories and branches of a user using the GitHub API.
 */
@Service
public class GitHubService {
    private final RestTemplate restTemplate;
    private final String githubApiUrl;

    public GitHubService(RestTemplate restTemplate, @Value("${github.api.url}") String githubApiUrl) {
        this.restTemplate = restTemplate;
        this.githubApiUrl = githubApiUrl;
    }

    public List<RepositoryDto> getUserRepositories(String username) {
        try {
            String url = githubApiUrl + "/users/" + username + "/repos";
            RepositoryDto[] repositories = restTemplate.getForObject(url, RepositoryDto[].class);
            return Arrays.stream(repositories)
                    .filter(repo -> !repo.isFork())
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException("User not found");
        } catch (HttpClientErrorException e) {
            throw new GitHubApiException("GitHub API error: " + e.getStatusCode());
        } catch (ResourceAccessException e) {
            throw new GitHubApiException("Network error: " + e.getMessage());
        }

    }

    public List<BranchDto> getRepositoryBranches(String username, String repositoryName) {
        String url = githubApiUrl + "/repos/" + username + "/" + repositoryName + "/branches";
        BranchDto[] branches = restTemplate.getForObject(url, BranchDto[].class);
        return Arrays.asList(branches);
    }

    public String checkRateLimit() {
        try {
            String url = githubApiUrl + "/rate_limit";
            return restTemplate.getForObject(url, String.class);
        } catch (HttpClientErrorException e) {
            throw new GitHubApiException("GitHub API error: " + e.getStatusCode());
        }
    }

}

