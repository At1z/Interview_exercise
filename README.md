<body>
  <h1>Interview Exercise</h1>
  <p>Application for parsing SWIFT codes from an Excel file into a database and providing REST endpoints to manage and retrieve SWIFT code data.</p>
  
  <h2>Environment</h2>
  <ul>
    <li><strong>OS:</strong> Ubuntu 20.04.6 LTS</li>
    <li><strong>Docker:</strong> version 27.5.1</li>
    <li>
      <strong>Java:
        <ul>
        <li></strong> OpenJDK version "17.0.14" (build 17.0.14+7-Ubuntu-120.04)</li>
        <li>OpenJDK Runtime Environment (build 17.0.14+7-Ubuntu-120.04)</li>
        <li>OpenJDK 64-Bit Server VM (build 17.0.14+7-Ubuntu-120.04, mixed mode, sharing)</li>
      </ul>
    </li>
  </ul>
  
  <h2>Setup and Installation</h2>
  <ol>
    <li>
      <strong>Clone the repository:</strong>
      <pre><code>git clone https://github.com/At1z/Interview_exercise.git
cd Interview_exercise</code></pre>
    </li>
  </ol>
  
  <h2>Building and Running Application</h2>
  <ol>
    <li>
      <strong>Build the Docker image (includes unit and integration tests):</strong>
      <pre><code>docker compose build --no-cache</code></pre>
    </li>
    <li>
      <strong>Run application:</strong>
      <pre><code>docker compose up</code></pre>
    </li>
  </ol>
  
  <h2>API Endpoints</h2>
  
  <h3>1. Parse Excel into Database</h3>
  <p><strong>Endpoint:</strong> <code>POST /v1/swift-codes/parse</code></p>
  <p><strong>Description:</strong> Parses the specified Excel file and inserts the data into the database.</p>
  <p><strong>Example Usage:</strong></p>
  <pre><code>curl -X POST "http://localhost:8080/v1/swift-codes/parse?filePath=/app/data/Interns_2025_SWIFT_CODES.xlsx"</code></pre>
  <p><strong>Expected Output:</strong> Returns all data that was added to the database.</p>
  
  <hr>
  
  <h3>2. Retrieve Swift Code by Code</h3>
  <p><strong>Endpoint:</strong> <code>GET /v1/swift-codes/{swiftCode}</code></p>
  <p><strong>Description:</strong> Retrieves the details for a specific SWIFT code, including any associated branch information.</p>
  <p><strong>Example Usage:</strong></p>
  <pre><code>curl -X GET "http://localhost:8080/v1/swift-codes/AIZKLV22XXX" -H "Content-Type: application/json"</code></pre>
  <p><strong>Expected Output:</strong></p>
  <pre><code>{
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
}</code></pre>
  
  <hr>
  
  <h3>3. Retrieve Data by Country ISO2 Code</h3>
  <p><strong>Endpoint:</strong> <code>GET /v1/swift-codes/country/{countryISO2code}</code></p>
  <p><strong>Description:</strong> Retrieves data for the specified country, including all associated SWIFT codes.</p>
  <p><strong>Example Usage:</strong></p>
  <pre><code>curl -X GET "http://localhost:8080/v1/swift-codes/country/AL" -H "Content-Type: application/json"</code></pre>
  <p><strong>Expected Output:</strong> A JSON object containing the country details and a list of associated SWIFT codes. (For brevity, the complete JSON response is not shown here.)</p>
  
  <hr>
  
  <h3>4. Add a New Swift Code</h3>
  <p><strong>Endpoint:</strong> <code>POST /v1/swift-codes</code></p>
  <p><strong>Description:</strong> Adds a new SWIFT code record to the database.</p>
  <p><strong>Example Usage:</strong></p>
  <pre><code>curl -X POST "http://localhost:8080/v1/swift-codes" \
     -H "Content-Type: application/json" \
     -d '{"address": "UL. SZARA KRAKOW", "bankName": "PKO BANK POLSKI", "countryISO2": "PL", "countryName": "POLAND", "isHeadquarter": true, "swiftCode": "AAAAACAAXXX"}'</code></pre>
  <p><strong>Expected Output:</strong></p>
  <pre><code>{
  "message": "Swift code added successfully"
}</code></pre>
  
  <hr>
  
  <h3>5. Delete a Swift Code</h3>
  <p><strong>Endpoint:</strong> <code>DELETE /v1/swift-codes/{swiftCode}</code></p>
  <p><strong>Description:</strong> Deletes the specified SWIFT code record from the database.</p>
  <p><strong>Example Usage:</strong></p>
  <pre><code>curl -X DELETE "http://localhost:8080/v1/swift-codes/AAISALTRXXX" -H "Accept: application/json"</code></pre>
  <p><strong>Expected Output:</strong></p>
  <pre><code>{
  "message": "Swift code deleted successfully"
}</code></pre>
</body>
