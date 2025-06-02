# 📋 Task Management REST API

A comprehensive Spring Boot application demonstrating modern Java enterprise development with clean architecture, containerization, and production-ready features.

## 📖 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Class Diagram](#class-diagram)
- [Component Diagram](#component-diagram)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Docker Deployment](#docker-deployment)
- [Configuration](#configuration)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

The Task Management API is a RESTful web service built with Spring Boot that provides CRUD operations for task management. This project serves as a reference implementation demonstrating:

- **Clean Architecture** with proper separation of concerns
- **Spring Framework** IoC/DI principles
- **RESTful API design** with proper HTTP semantics
- **Docker containerization** for consistent deployment
- **Production-ready features** including logging, monitoring, and error handling

### 🏢 Business Context

Developed for **Cloud Nova Inc.** as part of their transition to Spring Boot and containerized microservices architecture. The API will eventually integrate with a frontend dashboard for comprehensive task management.

## 🏗️ Architecture

This application follows a **layered architecture** pattern with clear separation of concerns:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Presentation   │    │    Business     │    │      Data       │
│     Layer       │◄──►│     Layer       │◄──►│     Layer       │
│                 │    │                 │    │                 │
│ TaskController  │    │  TaskService    │    │ TaskRepository  │
│ @RestController │    │   @Service      │    │  @Repository    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
        │                       │                       │
        ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   REST API      │    │ Business Logic  │    │  In-Memory      │
│   Endpoints     │    │   Validation    │    │  Collection     │
│                 │    │   Exception     │    │                 │
│ HTTP Methods    │    │   Handling      │    │ ConcurrentMap   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 📊 Class Diagram

![Class Diagram](/images/classDiagram.png)

*The class diagram shows the complete structure of the Task Management API with all classes, interfaces, relationships, and Spring annotations.*

## 🏛️ Component Diagram

![Component Diagram](images/componentDiagram.png)

*The component diagram illustrates the layered architecture showing how Spring Boot manages the application components through IoC/DI.*

## ✨ Features

### 🔧 Core Features
- **CRUD Operations**: Create, Read, Update, Delete tasks
- **Status Management**: TODO, IN_PROGRESS, COMPLETED states
- **Task Filtering**: Filter tasks by status
- **Task Counting**: Get total task count
- **Bulk Operations**: Delete all tasks

### 🏗️ Architecture Features
- **Clean Architecture**: Layered design with separation of concerns
- **Dependency Injection**: Spring IoC container management
- **Interface-Based Design**: Enables easy testing and swapping implementations
- **Exception Handling**: Global error handling with custom exceptions

### 🚀 Production Features
- **Validation**: Bean validation with custom error messages
- **Logging**: Structured logging with SLF4J and Logback
- **Monitoring**: Spring Boot Actuator endpoints
- **Health Checks**: Application health monitoring
- **Docker Support**: Containerized deployment
- **Multi-Environment**: Dev, Prod, Docker profiles

## 🛠️ Technology Stack

### Core Technologies
- **Java 17** - Latest LTS version
- **Spring Boot 3.5.0** - Application framework
- **Spring MVC** - Web layer
- **Spring Validation** - Input validation
- **Lombok** - Boilerplate code reduction

### Build & Deployment
- **Maven** - Dependency management and build tool
- **Docker** - Containerization
- **Eclipse Temurin** - OpenJDK distribution

### Data Storage
- **In-Memory Collections** - ConcurrentHashMap for thread-safe operations
- **AtomicLong** - Thread-safe ID generation

## 🚀 Getting Started

### Prerequisites
- **Java 17** or higher
- **Docker** (optional, for containerized deployment)
- **Git** for version control

### 1. Clone the Repository
```bash
git clone https://github.com/ingabireol/TaskManagementSystemSpring.git
cd TaskManagement
```

### 2. Run Locally (Without Docker)
```bash
# Using Maven wrapper (recommended)
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run

# With specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3. Run with Docker (Windows)
```powershell
# Build and run using PowerShell script
.\run-docker.ps1 run

# Or manually
docker build -t task-management-api .
docker run -d --name task-management-api-container -p 8080:8080 task-management-api
```

### 4. Verify Installation
```bash
# Check application health
curl http://localhost:8080/actuator/health

# Get all tasks
curl http://localhost:8080/api/tasks
```

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/tasks
```

### Endpoints

#### Tasks Management

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/tasks` | Get all tasks | - | `List<Task>` |
| GET | `/api/tasks/{id}` | Get task by ID | - | `Task` |
| POST | `/api/tasks` | Create new task | `Task` | `Task` (201) |
| PUT | `/api/tasks/{id}` | Update task | `Task` | `Task` |
| DELETE | `/api/tasks/{id}` | Delete task | - | - (204) |
| GET | `/api/tasks?status={status}` | Filter by status | - | `List<Task>` |
| GET | `/api/tasks/count` | Get task count | - | `TaskCountResponse` |
| DELETE | `/api/tasks` | Delete all tasks | - | - (204) |

#### Monitoring

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/actuator/health` | Application health |

### Request/Response Examples

#### Create Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete project documentation",
    "description": "Write comprehensive README and API docs",
    "status": "TODO"
  }'
```

#### Response
```json
{
  "id": 1,
  "title": "Complete project documentation",
  "description": "Write comprehensive README and API docs",
  "status": "TODO",
  "createdAt": "2024-01-15T10:30:00.000Z",
  "updatedAt": "2024-01-15T10:30:00.000Z"
}
```

#### Update Task
```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete project documentation",
    "description": "Write comprehensive README and API docs",
    "status": "IN_PROGRESS"
  }'
```

#### Error Response
```json
{
  "message": "Task not found with id: 999",
  "status": 404,
  "error": "Task Not Found",
  "timestamp": "2024-01-15T10:30:00.000Z"
}
```

#### Validation Error Response
```json
{
  "message": "Validation failed",
  "status": 400,
  "error": "Validation Error",
  "timestamp": "2024-01-15T10:30:00.000Z",
  "fieldErrors": {
    "title": "Title is required",
    "description": "Description must not exceed 500 characters"
  }
}
```

## 🐳 Docker Deployment

### Windows PowerShell Script (Recommended)

```powershell
# Build and run
.\run-docker.ps1 run

# View logs
.\run-docker.ps1 logs

# Check health
.\run-docker.ps1 health

# Stop application
.\run-docker.ps1 stop

# Clean up
.\run-docker.ps1 clean
```

### Manual Docker Commands

```bash
# Build image
docker build -t task-management-api .

# Run container
docker run -d \
  --name task-management-api-container \
  -p 8080:8080 \
  -v "${PWD}\logs:/app/logs" \
  -e SPRING_PROFILES_ACTIVE=docker \
  task-management-api

# View logs
docker logs -f task-management-api-container

# Stop and remove
docker stop task-management-api-container
docker rm task-management-api-container
```

### Docker Features
- **Multi-stage build** for optimized image size
- **Non-root user** for security
- **Health checks** for container orchestration
- **Volume mounting** for log persistence
- **Environment variables** for configuration

## ⚙️ Configuration

### Application Profiles

#### Default Profile (`application.properties`)
```properties
spring.application.name=TaskManagement
```





### Logging Configuration

The application uses **Logback** with profile-specific configurations:

- **Development**: Console output with DEBUG level
- **Production**: File output with INFO level, log rotation
- **Docker**: Console output for container logs

## 📁 Project Structure

```
TaskManagement/
├── 📄 pom.xml                                 # Maven configuration
├── 🐳 Dockerfile                              # Docker image definition
├── 📝 .dockerignore                           # Docker build exclusions
├── 🔷 run-docker.ps1                          # Windows Docker script
├── 📖 README.md                               # Project documentation
├── 📂 logs/                                   # Application logs
├── 📂 .mvn/                                   # Maven wrapper
├── 📄 mvnw / mvnw.cmd                         # Maven wrapper scripts
└── 📂 src/
    ├── 📂 main/
    │   ├── 📂 java/com/cloudnova/taskmanagement/
    │   │   ├── 🎯 TaskManagementApplication.java
    │   │   ├── 📂 controller/                  # REST controllers
    │   │   │   └── TaskController.java
    │   │   ├── 📂 service/                     # Business logic
    │   │   │   ├── TaskService.java
    │   │   │   └── impl/TaskServiceImpl.java
    │   │   ├── 📂 repository/                  # Data access
    │   │   │   ├── TaskRepository.java
    │   │   │   └── impl/TaskRepositoryImpl.java
    │   │   ├── 📂 model/                       # Domain entities
    │   │   │   ├── Task.java
    │   │   │   └── TaskStatus.java
    │   │   └── 📂 exception/                   # Error handling
    │   │       ├── TaskNotFoundException.java
    │   │       ├── GlobalExceptionHandler.java
    │   │       ├── ErrorResponse.java
    │   │       └── ValidationErrorResponse.java
    │   └── 📂 resources/
    │       ├── application.properties          # Default config
    │       ├── application-dev.yml             # Development config
    │       ├── application-prod.yml            # Production config
    │       ├── application-docker.yml          # Docker config
    │       └── logback-spring.xml              # Logging config
    └── 📂 test/                                # Test cases
        └── 📂 java/com/cloudnova/taskmanagement/
            ├── TaskManagementApplicationTests.java
            ├── 📂 controller/
            ├── 📂 service/
            └── 📂 repository/
```

## 🎯 Key Design Principles

### 1. **Clean Architecture**
- **Separation of Concerns**: Each layer has a distinct responsibility
- **Dependency Inversion**: Higher layers depend on abstractions
- **Interface Segregation**: Small, focused interfaces

### 2. **Spring IoC/DI**
- **Constructor Injection**: Immutable dependencies
- **Interface-Based Design**: Enables easy testing and swapping
- **Component Scanning**: Automatic bean discovery

### 3. **RESTful Design**
- **Resource-Oriented URLs**: `/api/tasks/{id}`
- **HTTP Semantics**: Proper use of methods and status codes
- **Content Negotiation**: JSON request/response format

### 4. **Exception Handling**
- **Global Handler**: Centralized error processing
- **Custom Exceptions**: Domain-specific error types
- **Consistent Format**: Standard error response structure

### 5. **Validation Strategy**
- **Bean Validation**: Declarative validation with annotations
- **Business Rules**: Service layer validation
- **Error Aggregation**: Multiple validation errors in single response

## 🤝 Contributing

### Development Setup
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Make your changes following the coding standards
4. Commit your changes: `git commit -m 'Add amazing feature'`
5. Push to the branch: `git push origin feature/amazing-feature`
6. Open a Pull Request

### Coding Standards
- Follow **Java Code Conventions**
- Use **Lombok** to reduce boilerplate
- Document **public APIs** with JavaDoc
- Follow **RESTful principles** for API design


## 🙏 Acknowledgments

- **Spring Framework Team** for the excellent framework
- **Lombok Project** for reducing boilerplate code
- **Docker Community** for containerization tools
- **Eclipse Temurin** for the OpenJDK distribution
- **Cloud Nova Inc.** for the project requirements and context

## 📞 Support

For support and questions:
- **Issues**: Create an issue on GitHub
- **Health Checks**: Use `/actuator/health` endpoint
- **Logs**: Check application logs in `logs/` directory

---

**Built with ❤️ using Spring Boot, Docker, and modern Java practices.**