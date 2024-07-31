# Technical Design

## 1. **Architecture Overview**

This project uses a Spring Boot framework to create a RESTful application that interacts with the GitHub API. The architecture follows a layered design pattern, including:

- **Controller Layer**: Handles HTTP requests and responses.
- **Service Layer**: Contains business logic and interacts with external APIs.
- **Exception Handling**: Manages errors and provides meaningful responses.
- **Configuration**: Manages application settings and dependencies.

## 2. **Components**

### 2.1 **GitHubController**

- **Responsibilities**:
    - Handle HTTP GET requests for retrieving user repositories.
    - Validate request headers.
    - Use the `GitHubService` to fetch repositories and branches.
    - Handle exceptions and return appropriate HTTP responses.

- **Endpoints**:
    - **GET /api/repositories/{username}**: Retrieves a list of non-fork repositories for the specified GitHub user, including branch information.

### 2.2 **GitHubService**

- **Responsibilities**:
    - Communicate with the GitHub API using `RestTemplate`.
    - Retrieve non-fork repositories for a user.
    - Fetch branches for a specific repository.
    - Handle GitHub API errors and network issues.

- **Methods**:
    - **getUserRepositories(String username)**:
        - Fetches repositories from GitHub.
        - Filters out forked repositories.
        - Retrieves branches for each repository.
    - **getRepositoryBranches(String username, String repositoryName)**:
        - Fetches branches for a specific repository.

### 2.3 **GlobalExceptionHandler**

- **Responsibilities**:
    - Handle various exceptions globally across the application.
    - Provide meaningful error responses in case of exceptions.

- **Exception Handlers**:
    - **UserNotFoundException**: Returns a 404 response with a "User not found" message.
    - **GitHubApiException**: Returns a 500 response with details of the GitHub API error.
    - **RateLimitExceededException**: Returns a 403 response indicating that the API rate limit has been exceeded.
    - **HttpClientErrorException**: Handles HTTP client errors, including rate-limiting issues.
    - **Exception**: Handles any other generic exceptions.

### 2.4 **RestTemplateConfig**

- **Responsibilities**:
    - Configure `RestTemplate` with an authorization token for making authenticated requests to the GitHub API.

- **Configuration**:
    - **Bean Method**: `restTemplate()`
        - Adds an `Authorization` header with the GitHub API token to all outgoing requests.

## 3. **Data Models**

### 3.1 **RepositoryDto**

- **Fields**:
    - `name`: The name of the repository.
    - `owner`: An object containing `login` (username of the repository owner).
    - `branches`: A list of `BranchDto` objects representing branches of the repository.
    - `fork`: A boolean indicating whether the repository is a fork.

### 3.2 **BranchDto**

- **Fields**:
    - `name`: The name of the branch.
    - `commit`: An object containing `sha` (the commit SHA).

## 4. **Error Handling**

- **UserNotFoundException**:
    - **Response**: `404 Not Found`
    - **Message**: `"User not found"`

- **GitHubApiException**:
    - **Response**: `500 Internal Server Error`
    - **Message**: `"GitHub API error: <error_message>"`

- **RateLimitExceededException**:
    - **Response**: `403 Forbidden`
    - **Message**: `"API rate limit exceeded. Please try again later."`

- **Generic Exception**:
    - **Response**: `500 Internal Server Error`
    - **Message**: `"An unexpected error occurred"`

## 5. **Configuration**

### 5.1 **Properties**

- **GitHub API URL**: Defined in `application.properties`.
    - Example: `github.api.url=https://api.github.com`

- **GitHub API Token**: Defined in `application.properties`.
    - Example: `github.api.token=your_github_api_token_here`

### 5.2 **Application Properties**

- **Server Port**: Configured to run on a specific port if required.
    - Example: `server.port=8080`

## 6. **Testing**

### 6.1 **Unit Tests**

- **GitHubService**:
    - Test methods for fetching repositories and branches.
    - Handle various scenarios such as successful responses, user not found, and API errors.

- **GitHubController**:
    - Test endpoints for correct request handling and response formatting.
    - Validate error handling and status codes.
