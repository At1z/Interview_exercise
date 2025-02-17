# Interview Exercise

This repository contains an application for parsing SWIFT codes from an Excel file into a database and providing REST endpoints to manage and retrieve SWIFT code data.

## Environment

- **OS:** Ubuntu 20.04.6 LTS  
- **Docker:** version 27.5.1  
- **Java:** OpenJDK version "17.0.14" (build 17.0.14+7-Ubuntu-120.04)  
  - OpenJDK Runtime Environment (build 17.0.14+7-Ubuntu-120.04)
  - OpenJDK 64-Bit Server VM (build 17.0.14+7-Ubuntu-120.04, mixed mode, sharing)

## Setup and Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/At1z/Interview_exercise.git
   cd Interview_exercise
   ```
## Building and running application

1. **Build the Docker image (includes unit and integration tests):**
  ```bash
  docker compose build --no-cache
  ```
2. **Run application:**
  ```bash
  docker compose up
  ```
