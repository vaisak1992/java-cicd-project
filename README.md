
# Java Maven CI/CD Automation

## Project Overview

This project implements an end-to-end CI/CD pipeline
for a Java Calculator application using GitHub Actions.

## Technologies

- Java 17
- Maven
- JUnit 5
- GitHub Actions
- SonarQube
- GitHub Packages
- AWS EC2
- Linux systemd

## Application Features

- Addition
- Subtraction
- Multiplication
- Division
- Division by zero exception handling

## CI/CD Pipelines

### Pipeline 1 – Build and Unit Testing
- Verify Maven project structure
- Compile Java source code
- Execute JUnit tests
- Package the application
- Upload JAR artifact

### Pipeline 2 – SonarQube Analysis
- Analyze Java source code
- Evaluate SonarQube Quality Gate

### Pipeline 3 – Publish and Deploy
- Publish Maven package to GitHub Packages
- Deploy JAR to AWS EC2
- Restart application using systemd

## Project Outcome

GitHub Push → Build → Test → SonarQube →
GitHub Packages → AWS EC2 Deployment
