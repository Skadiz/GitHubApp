package com.skadiz.GitHubRestApp.services;

import com.skadiz.GitHubRestApp.dto.BranchDto;
import com.skadiz.GitHubRestApp.dto.RepositoryDto;
import com.skadiz.GitHubRestApp.exceptions.GitHubApiException;
import com.skadiz.GitHubRestApp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for interacting with the GitHub API.
 */
@Service
public class GitHubService {
    private final RestTemplate restTemplate;
    private final String githubApiUrl;

    /**
     * Constructs a new GitHubService with the specified RestTemplate and GitHub API URL.
     *
     * @param restTemplate the RestTemplate to use for API requests
     * @param githubApiUrl the base URL for the GitHub API
     */
    public GitHubService(RestTemplate restTemplate, @Value("${github.api.url}") String githubApiUrl) {
        this.restTemplate = restTemplate;
        this.githubApiUrl = githubApiUrl;
    }

    /**
     * Retrieves the non-fork repositories of a given GitHub user.
     *
     * @param username the GitHub username
     * @return a list of RepositoryDto objects representing the user's non-fork repositories
     * @throws UserNotFoundException if the user is not found
     * @throws GitHubApiException if there is an error with the GitHub API or network issues
     */
    public List<RepositoryDto> getUserRepositories(String username) {
        try {
            String url = githubApiUrl + "/users/" + username + "/repos";
            ResponseEntity<RepositoryDto[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, RepositoryDto[].class, username);
            RepositoryDto[] repos = response.getBody();
            return Optional.ofNullable(repos)
                    .stream().flatMap(Arrays::stream)
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

    /**
     * Retrieves the branches of a given repository for a specified GitHub user.
     *
     * @param username the GitHub username
     * @param repositoryName the name of the repository
     * @return a list of BranchDto objects representing the branches of the repository
     * @throws GitHubApiException if there is an error with the GitHub API or network issues
     */
    public List<BranchDto> getRepositoryBranches(String username, String repositoryName) {
        String url = githubApiUrl + "/repos/" + username + "/" + repositoryName + "/branches";
        BranchDto[] branches = restTemplate.getForObject(url, BranchDto[].class);

        return Optional.ofNullable(branches)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }
}

