package com.skadiz.GitHubRestApp;

import static org.mockito.Mockito.*;
import com.skadiz.GitHubRestApp.dto.BranchDto;
import com.skadiz.GitHubRestApp.dto.CommitDto;
import com.skadiz.GitHubRestApp.dto.RepositoryDto;
import com.skadiz.GitHubRestApp.exceptions.UserNotFoundException;
import com.skadiz.GitHubRestApp.services.GitHubService;
import com.skadiz.GitHubRestApp.controllers.GitHubController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(GitHubController.class)
public class GitHubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubService gitHubService;

    @Test
    public void testGetRepositories_Success() throws Exception {
        // Arrange
        String username = "testuser";
        RepositoryDto repo1 = new RepositoryDto();
        repo1.setName("repo1");
        repo1.setFork(false);

        RepositoryDto repo2 = new RepositoryDto();
        repo2.setName("repo2");
        repo2.setFork(false);

        BranchDto branch = new BranchDto();
        CommitDto commit = new CommitDto();
        commit.setSha("123abc");
        branch.setName("main");
        branch.setCommit(commit);
        List<RepositoryDto> repos = Arrays.asList(repo1, repo2);

        when(gitHubService.getUserRepositories(username)).thenReturn(repos);
        when(gitHubService.getRepositoryBranches(username, "repo1")).thenReturn(Arrays.asList(branch));
        when(gitHubService.getRepositoryBranches(username, "repo2")).thenReturn(Arrays.asList(branch));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/repositories/{username}", username)
                        .header("Accept", "application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("repo1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].branches[0].name").value("main"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].branches[0].commit.sha").value("123abc"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("repo2"));
    }

    @Test
    public void testGetRepositories_WrongAcceptHeader() throws Exception {
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/repositories/{username}", "testuser")
                        .header("Accept", "text/plain"))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andExpect(MockMvcResultMatchers.content().string("Accept header must be 'application/json'"));
    }

    @Test
    public void testGetRepositories_UserNotFoundException() throws Exception {
        // Arrange
        String username = "testuser";

        when(gitHubService.getUserRepositories(username))
                .thenThrow(new UserNotFoundException("User not found"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/repositories/{username}", username)
                        .header("Accept", "application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found"));
    }
}


