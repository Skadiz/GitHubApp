package com.skadiz.GitHubRestApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Configuration class for creating and configuring {@link RestTemplate}.
 * This class configures {@link RestTemplate} to add an authentication token to the headers of all outgoing requests.
 * The token is used to perform authenticated requests to the GitHub API, allowing for a higher request limit.
 */
@Configuration
public class RestTemplateConfig {

    @Value("${github.api.token}")
    private String githubApiToken;


    /**
     * Creates and configures a {@link RestTemplate}.
     * @return a configured {@link RestTemplate} with the authentication token added to the request headers
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            request.getHeaders().add("Authorization", "token " + githubApiToken);
            return execution.execute(request, body);
        }));
        return restTemplate;
    }
}