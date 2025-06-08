# Cleaning Service

A comprehensive cleaning service management system built with modern Java technologies and monolith architecture.

## Tech Stack

### Backend
- **Java** - Core programming language
- **Spring Framework** - Application framework for dependency injection, web services, and enterprise features
- **Spring Boot** - For rapid application development and auto-configuration

### Database & Caching
- **PostgreSQL** - Primary relational database for persistent data storage
- **Redis** - In-memory data store for caching and session management

### Message Broker
- **Apache Kafka** - Distributed event streaming platform for real-time data processing and microservices communication

### Containerization & Deployment
- **Docker** - Containerization platform for consistent deployment environments
- **Jib** - Container image builder for Java applications without Docker daemon
- **DockerHub** - Container registry for storing and distributing Docker images

## Architecture

This application follows a microservices architecture pattern, leveraging:
- Event-driven communication through Kafka
- Containerized services for scalability and portability
- Distributed caching with Redis for improved performance
- Reliable data persistence with PostgreSQL

## Getting Started

### Prerequisites
- Java 11 or higher
- Docker and Docker Compose
- PostgreSQL
- Redis
- Apache Kafka

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/cleaning-service.git
cd cleaning-service
```

2. Build application images:
```bash
chmod +x build_app_image.sh
./build_app_image.sh
```

3. Start all services:
```bash
chmod +x docker_compose.sh
./docker_compose.sh
```

The build script creates multiple Docker images using different base images (Ubuntu Jammy, Alpine, Amazon Corretto, Eclipse Temurin) and uses Jib to build optimized Java container images with JVM tuning parameters.

## Features

- Service booking and management
- Real-time notifications via Kafka messaging
- User authentication and authorization
- Scalable microservices architecture
- High-performance caching layer
- Containerized deployment ready

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.