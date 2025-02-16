package com.atis.remitly_project;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/swift-codes")
@RequiredArgsConstructor
@Validated
public class SwiftCodeController {
    private final SwiftCodeRepository swiftCodeRepository;
    private final ExcelParser excelParser;
    private final SwiftCodeService swiftCodeService;

    @GetMapping("/{swiftCode}")
    public ResponseEntity<SwiftCodeDTO> getSwiftCode(
            @PathVariable
            @Pattern(regexp = "^[A-Z]{6}[A-Z0-9]{5}$",
                    message = "Swift code must be exactly 11 characters: 6 letters followed by 5 letters or numbers")
            String swiftCode) {
        try {
            SwiftCodeDTO result = swiftCodeService.getSwiftCodeDetails(swiftCode);
            if (result == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Swift code not found: " + swiftCode,
                    e
            );
        }
    }
    // curl.exe -X GET "http://localhost:8080/v1/swift-codes/AIZKLV22XXX" -H "Content-Type: application/json"

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<CountrySwiftCodesDTO> getCountrySwiftCodes(
            @PathVariable
            @Pattern(regexp = "^[A-Z]{2}$",
                    message = "Country ISO2 code must be exactly 2 uppercase letters")
            String countryISO2code) {
        try {
            CountrySwiftCodesDTO result = swiftCodeService.getCountrySwiftCodes(countryISO2code);
            if (result == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Country not found: " + countryISO2code,
                    e
            );
        }
    }
    // curl.exe -X GET "http://localhost:8080/v1/swift-codes/country/AL" -H "Content-Type: application/json"

    @PostMapping
    public ResponseEntity<MessageResponseDTO> addSwiftCode(@Valid @RequestBody SwiftCodeDTO swiftCodeDTO) {
        return ResponseEntity.ok(swiftCodeService.addSwiftCode(swiftCodeDTO));
    }

    //  curl.exe -X POST 'http://localhost:8080/v1/swift-codes' `
    // -H 'Content-Type: application/json' `
    // -d '{\"address\": \"UL. SZARA KRAKOW\", \"bankName\": \"PKO BANK POLSKI\", \"countryISO2\": \"PL\", \"countryName\": \"POLAND\", \"isHeadquarter\": true, \"swiftCode\": \"AAAAACAAXXX\"}'

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<MessageResponseDTO> deleteSwiftCode(
            @PathVariable
            @Pattern(regexp = "^[A-Z]{6}[A-Z0-9]{5}$",
                    message = "Swift code must be exactly 11 characters: 6 letters followed by 5 letters or numbers")
            String swiftCode) {
        try {
            MessageResponseDTO result = swiftCodeService.deleteSwiftCode(swiftCode);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Swift code not found: " + swiftCode,
                    e
            );
        }
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

    @ExceptionHandler(SwiftCodeValidationException.class)
    public ResponseEntity<MessageResponseDTO> handleValidationException(SwiftCodeValidationException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponseDTO(String.join(", ", errors)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MessageResponseDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponseDTO(errorMessage));
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<MessageResponseDTO> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new MessageResponseDTO(ex.getReason()));
    }
}
