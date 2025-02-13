package com.atis.remitly_project;

import lombok.Data;
import java.util.List;

@Data
public class SwiftCodeDTO {
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private boolean headquarter;
    private String swiftCode;
    private List<BranchDTO> branches;
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