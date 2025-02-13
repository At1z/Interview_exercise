package com.atis.remitly_project;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SwiftCodeService {
    private final SwiftCodeRepository swiftCodeRepository;

    public SwiftCodeDTO getSwiftCodeDetails(String swiftCode) {
        SwiftCode code = swiftCodeRepository.findBySwiftCode(swiftCode);
        if (code == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Swift code not found");
        }

        SwiftCodeDTO dto = convertToDTO(code);
        String searchPrefix = swiftCode.substring(0, 8);

        if (!code.isHeadquarter()) {
            String potentialHQCode = searchPrefix + "XXX";
            SwiftCode headquarter = swiftCodeRepository.findBySwiftCode(potentialHQCode);
            if (headquarter != null) {
                dto.setHeadquarter(false);
                BranchDTO hqBranch = convertToBranchDTO(headquarter);
                List<BranchDTO> branches = new ArrayList<>();
                branches.add(hqBranch);

                List<SwiftCode> siblingBranches = swiftCodeRepository
                        .findBranchesByHeadquarterPrefix(searchPrefix, swiftCode);
                branches.addAll(siblingBranches.stream()
                        .map(this::convertToBranchDTO)
                        .collect(Collectors.toList()));

                dto.setBranches(branches);
            }
        } else {
            List<SwiftCode> branches = swiftCodeRepository
                    .findBranchesByHeadquarterPrefix(searchPrefix, swiftCode);
            dto.setBranches(branches.stream()
                    .map(this::convertToBranchDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public CountrySwiftCodesDTO getCountrySwiftCodes(String countryISO2) {
        List<SwiftCode> codes = swiftCodeRepository.findByCountryISO2(countryISO2.toUpperCase());
        if (codes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No swift codes found for country");
        }

        CountrySwiftCodesDTO dto = new CountrySwiftCodesDTO();
        dto.setCountryISO2(countryISO2.toUpperCase());
        dto.setCountryName(codes.get(0).getCountryName());
        dto.setSwiftCodes(codes.stream()
                .map(this::convertToBranchDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    public MessageResponseDTO addSwiftCode(SwiftCodeDTO swiftCodeDTO) {
        if (swiftCodeRepository.findBySwiftCode(swiftCodeDTO.getSwiftCode()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Swift code already exists");
        }

        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setAddress(swiftCodeDTO.getAddress());
        swiftCode.setBankName(swiftCodeDTO.getBankName());
        swiftCode.setCountryISO2(swiftCodeDTO.getCountryISO2().toUpperCase());
        swiftCode.setCountryName(swiftCodeDTO.getCountryName().toUpperCase());
        swiftCode.setSwiftCode(swiftCodeDTO.getSwiftCode());

        swiftCodeRepository.save(swiftCode);
        return new MessageResponseDTO("Swift code added successfully");
    }

    public MessageResponseDTO deleteSwiftCode(String swiftCode) {
        SwiftCode code = swiftCodeRepository.findBySwiftCode(swiftCode);
        if (code == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Swift code not found");
        }

        swiftCodeRepository.delete(code);
        return new MessageResponseDTO("Swift code deleted successfully");
    }

    private SwiftCodeDTO convertToDTO(SwiftCode swiftCode) {
        SwiftCodeDTO dto = new SwiftCodeDTO();
        dto.setAddress(swiftCode.getAddress());
        dto.setBankName(swiftCode.getBankName());
        dto.setCountryISO2(swiftCode.getCountryISO2());
        dto.setCountryName(swiftCode.getCountryName());
        dto.setHeadquarter(swiftCode.isHeadquarter());
        dto.setSwiftCode(swiftCode.getSwiftCode());
        return dto;
    }

    private BranchDTO convertToBranchDTO(SwiftCode swiftCode) {
        BranchDTO dto = new BranchDTO();
        dto.setAddress(swiftCode.getAddress());
        dto.setBankName(swiftCode.getBankName());
        dto.setCountryISO2(swiftCode.getCountryISO2());
        dto.setHeadquarter(swiftCode.isHeadquarter());
        dto.setSwiftCode(swiftCode.getSwiftCode());
        return dto;
    }
}