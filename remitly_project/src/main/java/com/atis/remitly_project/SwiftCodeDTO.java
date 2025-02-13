package com.atis.remitly_project;

import lombok.Data;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
@Data
public class SwiftCodeDTO {
    @NotBlank(message = "Address is required")
    @Pattern(regexp = "^[A-Z0-9\\s,./-]+$", message = "Address must contain only uppercase letters, numbers and basic punctuation")
    private String address;

    @NotBlank(message = "Bank name is required")
    @Pattern(regexp = "^[A-Z\\s]+$", message = "Bank name must contain only uppercase letters")
    private String bankName;

    @NotBlank(message = "Country ISO2 code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country ISO2 code must be exactly 2 uppercase letters")
    private String countryISO2;

    @NotBlank(message = "Country name is required")
    @Pattern(regexp = "^[A-Z\\s]+$", message = "Country name must contain only uppercase letters")
    private String countryName;

    private boolean headquarter;

    @NotBlank(message = "Swift code is required")
    @Pattern(regexp = "^[A-Z0-9]{11}$", message = "Swift code must be exactly 11 uppercase letters or numbers")
    private String swiftCode;

    private List<BranchDTO> branches;

    public void validate() {
        if (swiftCode != null) {
            boolean endsWithXXX = swiftCode.endsWith("XXX");
            if (headquarter && !endsWithXXX) {
                throw new SwiftCodeValidationException("Headquarter swift code must end with XXX");
            }
            if (!headquarter && endsWithXXX) {
                throw new SwiftCodeValidationException("Non-headquarter swift code cannot end with XXX");
            }
        }
    }
}
class SwiftCodeValidationException extends RuntimeException {
    public SwiftCodeValidationException(String message) {
        super(message);
    }
}
@Data
class BranchDTO {
    private String address;
    private String bankName;
    private String countryISO2;
    private boolean isHeadquarter;
    private String swiftCode;
}

@Data
class CountrySwiftCodesDTO {
    private String countryISO2;
    private String countryName;
    private List<BranchDTO> swiftCodes;
}

@Data
class MessageResponseDTO {
    private String message;

    public MessageResponseDTO(String message) {
        this.message = message;
    }
}