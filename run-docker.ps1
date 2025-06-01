# Task Management API - PowerShell Script for Docker Management

param(
    [Parameter(Position=0)]
    [ValidateSet("build", "run", "stop", "restart", "logs", "clean", "health", "shell", "help")]
    [string]$Command = "help"
)

# Configuration
$AppName = "task-management-api"
$ImageName = "$AppName:latest"
$ContainerName = "$AppName-container"

# Helper functions
function Write-Status {
    param([string]$Message)
    Write-Host "[INFO] $Message" -ForegroundColor Blue
}

function Write-Success {
    param([string]$Message)
    Write-Host "[SUCCESS] $Message" -ForegroundColor Green
}

function Write-Warning {
    param([string]$Message)
    Write-Host "[WARNING] $Message" -ForegroundColor Yellow
}

function Write-Error {
    param([string]$Message)
    Write-Host "[ERROR] $Message" -ForegroundColor Red
}

function Show-Usage {
    Write-Host "Usage: .\run-docker.ps1 [COMMAND]"
    Write-Host ""
    Write-Host "Commands:"
    Write-Host "  build    - Build the Docker image"
    Write-Host "  run      - Run the application container"
    Write-Host "  stop     - Stop the running container"
    Write-Host "  restart  - Restart the container"
    Write-Host "  logs     - Show container logs"
    Write-Host "  clean    - Remove container and image"
    Write-Host "  health   - Check application health"
    Write-Host "  shell    - Open shell in running container"
    Write-Host "  help     - Show this help message"
}

function Build-Image {
    Write-Status "Building Docker image: $ImageName"
    
    docker build -t $ImageName .
    
    if ($LASTEXITCODE -eq 0) {
        Write-Success "Docker image built successfully!"
    } else {
        Write-Error "Failed to build Docker image"
        exit 1
    }
}

function Run-Container {
    # Stop and remove existing container if it exists
    $existingContainer = docker ps -q -f name=$ContainerName
    if ($existingContainer) {
        Write-Warning "Stopping existing container..."
        docker stop $ContainerName | Out-Null
    }
    
    $existingContainer = docker ps -aq -f name=$ContainerName
    if ($existingContainer) {
        Write-Warning "Removing existing container..."
        docker rm $ContainerName | Out-Null
    }
    
    Write-Status "Starting container: $ContainerName"
    
    # Create logs directory if it doesn't exist
    if (!(Test-Path "logs")) {
        New-Item -ItemType Directory -Name "logs" | Out-Null
    }
    
    # Run the container
    docker run -d --name $ContainerName -p 8080:8080 -v "${PWD}\logs:/app/logs" -e SPRING_PROFILES_ACTIVE=docker $ImageName
    
    if ($LASTEXITCODE -eq 0) {
        Write-Success "Container started successfully!"
        Write-Status "API available at: http://localhost:8080"
        Write-Status "Health check: http://localhost:8080/actuator/health"
        Write-Status "Use '.\run-docker.ps1 logs' to view application logs"
    } else {
        Write-Error "Failed to start container"
        exit 1
    }
}

function Stop-Container {
    $runningContainer = docker ps -q -f name=$ContainerName
    if ($runningContainer) {
        Write-Status "Stopping container: $ContainerName"
        docker stop $ContainerName | Out-Null
        Write-Success "Container stopped successfully!"
    } else {
        Write-Warning "Container $ContainerName is not running"
    }
}

function Restart-Container {
    Stop-Container
    Start-Sleep -Seconds 2
    Run-Container
}

function Show-Logs {
    $runningContainer = docker ps -q -f name=$ContainerName
    if ($runningContainer) {
        Write-Status "Showing logs for container: $ContainerName"
        docker logs -f $ContainerName
    } else {
        Write-Error "Container $ContainerName is not running"
        exit 1
    }
}

function Clean-Up {
    Write-Status "Cleaning up containers and images..."
    
    # Stop and remove container
    $runningContainer = docker ps -q -f name=$ContainerName
    if ($runningContainer) {
        docker stop $ContainerName | Out-Null
    }
    
    $existingContainer = docker ps -aq -f name=$ContainerName
    if ($existingContainer) {
        docker rm $ContainerName | Out-Null
    }
    
    # Remove image
    $existingImage = docker images -q $ImageName
    if ($existingImage) {
        docker rmi $ImageName | Out-Null
    }
    
    Write-Success "Cleanup completed!"
}

function Check-Health {
    Write-Status "Checking application health..."
    
    $runningContainer = docker ps -q -f name=$ContainerName
    if ($runningContainer) {
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -TimeoutSec 5
            if ($response.StatusCode -eq 200) {
                Write-Success "Application is healthy!"
                $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 3
            } else {
                Write-Error "Application is not healthy"
                exit 1
            }
        } catch {
            Write-Error "Application is not healthy: $($_.Exception.Message)"
            exit 1
        }
    } else {
        Write-Error "Container is not running"
        exit 1
    }
}

function Open-Shell {
    $runningContainer = docker ps -q -f name=$ContainerName
    if ($runningContainer) {
        Write-Status "Opening shell in container: $ContainerName"
        docker exec -it $ContainerName /bin/bash
    } else {
        Write-Error "Container $ContainerName is not running"
        exit 1
    }
}

# Main script logic
switch ($Command) {
    "build" { Build-Image }
    "run" { 
        Build-Image
        Run-Container 
    }
    "stop" { Stop-Container }
    "restart" { Restart-Container }
    "logs" { Show-Logs }
    "clean" { Clean-Up }
    "health" { Check-Health }
    "shell" { Open-Shell }
    "help" { Show-Usage }
    default { Show-Usage }
}