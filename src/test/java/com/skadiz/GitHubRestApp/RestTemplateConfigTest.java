package com.skadiz.GitHubRestApp;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.skadiz.GitHubRestApp.dto.RepositoryDto;
import com.skadiz.GitHubRestApp.exceptions.GitHubApiException;
import com.skadiz.GitHubRestApp.services.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class RestTemplateConfigTest {

    private GitHubService gitHubService;
    private RestTemplate restTemplate;

    @Value("${github.api.url}")
    private String githubApiUrl;

    @BeforeEach
    public void setUp() {
        restTemplate = mock(RestTemplate.class);
        gitHubService = new GitHubService(restTemplate, githubApiUrl);
    }

    @Test
    public void testGetUserRepositories_UnauthorizedException() {
        // Arrange
        String username = "testuser";
        String url = githubApiUrl + "/users/" + username + "/repos";

        when(restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                RepositoryDto[].class,
                username))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        // Act & Assert
        assertThrows(GitHubApiException.class, () -> gitHubService.getUserRepositories(username));
    }
}
