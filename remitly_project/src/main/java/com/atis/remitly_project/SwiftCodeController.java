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

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<CountrySwiftCodesDTO> getCountrySwiftCodes(@PathVariable String countryISO2code) {
        return ResponseEntity.ok(swiftCodeService.getCountrySwiftCodes(countryISO2code));
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO> addSwiftCode(@RequestBody SwiftCodeDTO swiftCodeDTO) {
        return ResponseEntity.ok(swiftCodeService.addSwiftCode(swiftCodeDTO));
    }

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<MessageResponseDTO> deleteSwiftCode(@PathVariable String swiftCode) {
        return ResponseEntity.ok(swiftCodeService.deleteSwiftCode(swiftCode));
    }

    @PostMapping("/parse")
    public ResponseEntity<List<SwiftCode>> parseAndSave(@RequestParam("filePath") String filePath) {
        try {
            List<SwiftCode> swiftCodes = excelParser.parse(filePath);
            return ResponseEntity.ok(swiftCodeRepository.saveAll(swiftCodes));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error parsing Excel file: " + e.getMessage());
        }
    }
}