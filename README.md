# Interview Exercise

Application for parsing SWIFT codes from an Excel file into a database and providing REST endpoints to manage and retrieve SWIFT code data.

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
## API Endpoints
1. Parse Excel into Database

   Endpoint: @POST /v1/swift-codes/parse

   Description: Parses the specified Excel file and inserts the data into the database.

   Example Usage:
   ```bash
   curl -X POST "http://localhost:8080/v1/swift-codes/parse?filePath=/app/data/Interns_2025_SWIFT_CODES.xlsx"
   ```
    Expected Output:
    Returns all data that was added to the database.
  
2. Retrieve Swift Code by Code

   Endpoint: GET /v1/swift-codes/{swiftCode}

   Description: Retrieves the details for a specific SWIFT code, including any associated branch information.

   Example Usage:
  ```bash
   curl -X GET "http://localhost:8080/v1/swift-codes/AIZKLV22XXX" -H "Content-Type: application/json"
  ```
  Expected Output:
  ```json
    {
      "address": "MIHAILA TALA STREET 1  RIGA, RIGA, LV-1045",
      "bankName": "ABLV BANK, AS IN LIQUIDATION",
      "countryISO2": "LV",
      "countryName": "LATVIA",
      "isHeadquarter": true,
      "swiftCode": "AIZKLV22XXX",
      "branches": [
        {
          "address": "ELIZABETES STREET 23  RIGA, RIGA, LV-1010",
          "bankName": "ABLV BANK, AS IN LIQUIDATION",
          "countryISO2": "LV",
          "isHeadquarter": false,
          "swiftCode": "AIZKLV22CLN"
        }
      ]
    }
 ```

