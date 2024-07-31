# GitHub REST API Application

## Overview

The **GitHub REST API Application** is a Spring Boot-based application that provides a RESTful interface for interacting with the GitHub API. It allows users to retrieve non-fork repositories and their branches for a given GitHub user. The application handles various scenarios including API errors, rate limits, and missing users, providing appropriate responses for each case.

## Features

- **Retrieve Non-Fork Repositories**: Get a list of non-fork repositories for a specific GitHub user.
- **Fetch Repository Branches**: Retrieve branches for each repository.
- **Error Handling**: Gracefully manage API errors, rate limits, and user not found scenarios.
- **Configuration**: Use an authorization token for authenticated requests to the GitHub API.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- An active GitHub API token

### Installation

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/yourusername/your-repo-name.git
    cd your-repo-name
    ```

2. **Configure Application Properties**:
    - Create a file named `application.properties` in the `src/main/resources` directory.
    - Add the following properties:
      ```properties
      github.api.url=https://api.github.com
      github.api.token=your_github_api_token_here
      ```

3. **Build the Project**:
    ```bash
    mvn clean install
    ```

4. **Run the Application**:
    ```bash
    mvn spring-boot:run
    ```

### Endpoints

- **GET /api/repositories/{username}**: Retrieves a list of non-fork repositories for the specified GitHub user, including branch information.

### Documentation

For detailed documentation on the project, including response formats and API usage, please refer to the following resources in the `docs` folder:

- [API Documentation](https://documenter.getpostman.com/view/24435953/2sA3kbgyYy)
- [Project-requirements](docs/project-requirements.md)
- [Tech-design](docs/tech-design.md)

## Testing

Unit and integration tests are provided for the application. To run the tests, use the following command:

```bash
mvn test
