package com.skadiz.GitHubRestApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.skadiz.GitHubRestApp.dto.BranchDto;
import com.skadiz.GitHubRestApp.dto.CommitDto;
import com.skadiz.GitHubRestApp.dto.RepositoryDto;
import com.skadiz.GitHubRestApp.exceptions.GitHubApiException;
import com.skadiz.GitHubRestApp.exceptions.UserNotFoundException;
import com.skadiz.GitHubRestApp.services.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class GitHubServiceTest {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private GitHubService gitHubService;

	private final String githubApiUrl = "https://api.github.com";

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		// Initialize GitHubService with mocked RestTemplate and URL
		gitHubService = new GitHubService(restTemplate, githubApiUrl);
	}

	@Test
	public void testGetUserRepositories_Success() {
		// Arrange
		String username = "testuser";
		RepositoryDto repo1 = new RepositoryDto();
		repo1.setName("repo1");
		RepositoryDto repo2 = new RepositoryDto(); // This is a fork and should be filtered out
		repo2.setName("repo2");
		RepositoryDto[] repos = {repo1, repo2};
		ResponseEntity<RepositoryDto[]> response = ResponseEntity.ok(repos);

		when(restTemplate.exchange(
				githubApiUrl + "/users/" + username + "/repos",
				HttpMethod.GET,
				null,
				RepositoryDto[].class,
				username))
				.thenReturn(response);

		// Act
		List<RepositoryDto> result = gitHubService.getUserRepositories(username);

		// Assert
		assertEquals(2, result.size());
		assertEquals("repo1", result.get(0).getName());
	}

	@Test
	public void testGetUserRepositories_UserNotFound() {
		// Arrange
		String username = "testuser";
		when(restTemplate.exchange(
				githubApiUrl + "/users/" + username + "/repos",
				HttpMethod.GET,
				null,
				RepositoryDto[].class,
				username))
				.thenThrow(new UserNotFoundException("User not found"));

		// Act & Assert
		UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> {
			gitHubService.getUserRepositories(username);
		});
		assertEquals("User not found", thrown.getMessage());
	}

	@Test
	public void testGetUserRepositories_GitHubApiException() {
		// Arrange
		String username = "testuser";
		when(restTemplate.exchange(
				githubApiUrl + "/users/" + username + "/repos",
				HttpMethod.GET,
				null,
				RepositoryDto[].class,
				username))
				.thenThrow(new GitHubApiException(HttpStatus.INTERNAL_SERVER_ERROR.toString()));

		// Act & Assert
		GitHubApiException thrown = assertThrows(GitHubApiException.class, () -> {
			gitHubService.getUserRepositories(username);
		});
		assertEquals("500 INTERNAL_SERVER_ERROR", thrown.getMessage());
	}

	@Test
	public void testGetUserRepositories_NetworkError() {
		// Arrange
		String username = "testuser";
		when(restTemplate.exchange(
				githubApiUrl + "/users/" + username + "/repos",
				HttpMethod.GET,
				null,
				RepositoryDto[].class,
				username))
				.thenThrow(new ResourceAccessException("Network error"));

		// Act & Assert
		GitHubApiException thrown = assertThrows(GitHubApiException.class, () -> {
			gitHubService.getUserRepositories(username);
		});
		assertEquals("Network error: Network error", thrown.getMessage());
	}

	@Test
	public void testGetRepositoryBranches_Success() {
		// Arrange
		String username = "testuser";
		String repositoryName = "repo1";
		BranchDto branch1 = new BranchDto();
		branch1.setName("main");
		CommitDto commit1 = new CommitDto();
		commit1.setSha("1234567890abcdef");
		branch1.setCommit(commit1);
		BranchDto[] branches = {branch1};
		when(restTemplate.getForObject(
				githubApiUrl + "/repos/" + username + "/" + repositoryName + "/branches",
				BranchDto[].class))
				.thenReturn(branches);

		// Act
		List<BranchDto> result = gitHubService.getRepositoryBranches(username, repositoryName);

		// Assert
		assertEquals(1, result.size());
		assertEquals("main", result.get(0).getName());
		assertEquals("1234567890abcdef", result.get(0).getCommit().getSha());
	}

	@Test
	public void testGetRepositoryBranches_NoBranches() {
		// Arrange
		String username = "testuser";
		String repositoryName = "repo1";
		when(restTemplate.getForObject(
				githubApiUrl + "/repos/" + username + "/" + repositoryName + "/branches",
				BranchDto[].class))
				.thenReturn(null);

		// Act
		List<BranchDto> result = gitHubService.getRepositoryBranches(username, repositoryName);

		// Assert
		assertEquals(Collections.emptyList(), result);
	}

	@Test
	public void testGetRepositoryBranches_GitHubApiException() {
		// Arrange
		String username = "testuser";
		String repositoryName = "repo1";
		when(restTemplate.getForObject(
				githubApiUrl + "/repos/" + username + "/" + repositoryName + "/branches",
				BranchDto[].class))
				.thenThrow(new GitHubApiException(HttpStatus.INTERNAL_SERVER_ERROR.toString()));

		// Act & Assert
		GitHubApiException thrown = assertThrows(GitHubApiException.class, () -> {
			gitHubService.getRepositoryBranches(username, repositoryName);
		});
		assertEquals("500 INTERNAL_SERVER_ERROR", thrown.getMessage());
	}
}


