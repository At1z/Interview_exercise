package com.atis.remitly_project;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/swift-codes")
@RequiredArgsConstructor
public class SwiftCodeController {
    private final SwiftCodeRepository swiftCodeRepository;
    private final ExcelParser excelParser;
    private final SwiftCodeService swiftCodeService;

    @GetMapping("/{swiftCode}")
    public ResponseEntity<SwiftCodeDTO> getSwiftCode(@PathVariable String swiftCode) {
        return ResponseEntity.ok(swiftCodeService.getSwiftCodeDetails(swiftCode));
    }
    // curl.exe -X GET "http://localhost:8080/v1/swift-codes/AIZKLV22XXX" -H "Content-Type: application/json"

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<CountrySwiftCodesDTO> getCountrySwiftCodes(@PathVariable String countryISO2code) {
        return ResponseEntity.ok(swiftCodeService.getCountrySwiftCodes(countryISO2code));
    }
    // curl.exe -X GET "http://localhost:8080/v1/swift-codes/country/AL" -H "Content-Type: application/json"

    @PostMapping
    public ResponseEntity<MessageResponseDTO> addSwiftCode(@RequestBody SwiftCodeDTO swiftCodeDTO) {
        return ResponseEntity.ok(swiftCodeService.addSwiftCode(swiftCodeDTO));
    }
    // PS C:\Users\nosta\Desktop\remitly\Interview_exercise> curl.exe -X POST 'http://localhost:8080/v1/swift-codes' `
    //>> -H 'Content-Type: application/json' `
    //>> -d '{\"address\": \"ul. Pulawska 15, 02-515 Warszawa\", \"bankName\": \"PKO Bank Polski\", \"countryISO2\": \"PL\", \"countryName\": \"Poland\", \"headquarter\": true, \"swiftCode\": \"AAAAAAAAXXX\"}'
    //{"message":"Swift code added successfully"}

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<MessageResponseDTO> deleteSwiftCode(@PathVariable String swiftCode) {
        return ResponseEntity.ok(swiftCodeService.deleteSwiftCode(swiftCode));
    }
    // curl.exe -X DELETE "http://localhost:8080/v1/swift-codes/AAISALTRXXX" -H "Accept: application/json"
    @PostMapping("/parse")
    public ResponseEntity<List<SwiftCode>> parseAndSave(@RequestParam("filePath") String filePath) {
        try {
            List<SwiftCode> swiftCodes = excelParser.parse(filePath);
            return ResponseEntity.ok(swiftCodeRepository.saveAll(swiftCodes));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error parsing Excel file: " + e.getMessage());
        }
    }
    // curl.exe -X  POST "http://localhost:8080/v1/swift-codes/parse?filePath=/Users/nosta/Desktop/remitly/Interview_exercise/Interns_2025_SWIFT_CODES.xlsx"

}