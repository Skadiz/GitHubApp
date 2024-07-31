# Project Requirements

## 1. **Overview**

This project is a Spring Boot application that interacts with the GitHub API. The application allows users to retrieve information about GitHub repositories, including their branches and details. It also handles various types of errors and rate-limiting issues gracefully.

## 2. **Functional Requirements**

### 2.1 **Repository Information**
- **Retrieve User Repositories**:
    - **Endpoint**: `/api/repositories/{username}`
    - **Method**: `GET`
    - **Description**: Fetch all non-fork repositories of a given GitHub user. Each repository should include details such as branches and the last commit SHA.
    - **Response**: A list of repository objects with their respective branches and commit information.

### 2.2 **Branch Information**
- **Retrieve Repository Branches**:
    - **Description**: For each repository, fetch its branches and their respective last commit SHA.
    - **Response**: A list of branches for a specific repository.

## 3. **Error Handling**

### 3.1 **User Not Found**
- **Error Response**:
    - **Status Code**: `404 Not Found`
    - **Message**: `"User not found"`
    - **Description**: This error occurs when the specified user does not exist on GitHub.

### 3.2 **GitHub API Error**
- **Error Response**:
    - **Status Code**: `500 Internal Server Error`
    - **Message**: `"GitHub API error: 401 UNAUTHORIZED"`
    - **Description**: This error indicates that there was an issue with GitHub API authorization. Ensure that a valid token is provided in the request headers.

### 3.3 **General Exceptions**
- **Error Response**:
    - **Status Code**: `500 Internal Server Error`
    - **Message**: `"An unexpected error occurred"`
    - **Description**: This is a generic error message for unexpected issues that are not handled by specific exception handlers.

## 4. **Authentication**

- **Token Requirement**:
    - All requests to the GitHub API must include a valid authentication token in the request headers.
    - The token is specified in the application properties file and is added to the headers by the `RestTemplateConfig` class.

## 5. **Project Configuration**

### 5.1 **Dependencies**
- **Spring Boot**: For building the application.
- **Spring Web**: For RESTful web services.
- **Spring Boot Test**: For unit testing and integration testing.
- **Mockito**: For mocking dependencies in tests.

### 5.2 **Configuration**
- **GitHub API URL**: Configured in `application.properties`.
- **GitHub API Token**: Configured (need to enter yours token from GitHub) in `application.properties` and used for authenticated requests.

## 6. **Testing**

### 6.1 **Unit Tests**
- **Service Layer**: Test methods in the `GitHubService` class for retrieving repositories and branches.
- **Controller Layer**: Test endpoints in the `GitHubController` class to ensure proper handling of requests and responses.

## 7. **Documentation**

### 7.1 **API Documentation**
- **Postman Collection**: Includes examples of requests and responses for various endpoints: [Link](https://documenter.getpostman.com/view/24435953/2sA3kbgyYy)

### 7.2 **Code Documentation**
- **JavaDoc**: Ensure that all classes, methods, and fields are documented with appropriate JavaDoc comments.
