package com.atis.remitly_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class SwiftCodeControllerTest {


    @Mock
    private SwiftCodeRepository swiftCodeRepository;

    @Mock
    private ExcelParser excelParser;

    @InjectMocks
    private SwiftCodeService swiftCodeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getSwiftCode_InvalidSwiftCode_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/INVALID123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Swift code must be exactly 11 characters: 6 letters followed by 5 letters or numbers"
                ));
    }
    @Test
    void getSwiftCode_ValidSwiftCode_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/AIZKLVAAXXX"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSwiftCode_ValidSwiftCode_ReturnsOk() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/ABIEBGS1XXX"))
                .andExpect(status().isOk());
    }

    @Test
    void getSwiftCode_InvalidSwiftCodeLowercase_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/AIZKLV22XXx"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Swift code must be exactly 11 characters: 6 letters followed by 5 letters or numbers"
                ));
    }

    @Test
    void getSwiftCode_InvalidSwiftCodeNumberInFirstSix_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/AI2KLV22XXX"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Swift code must be exactly 11 characters: 6 letters followed by 5 letters or numbers"
                ));
    }

    @Test
    void getConutryISO2_ValidCountryISO2_ReturnsOk() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/country/AL"))
                .andExpect(status().isOk());
    }

    @Test
    void getConutryISO2_InvalidSwiftCodeLowercase_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/country/Al"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Country ISO2 code must be exactly 2 uppercase letters"
                ));
    }

    @Test
    void getConutryISO2_InvalidCountryISO2Length_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/country/ALL"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Country ISO2 code must be exactly 2 uppercase letters"
                ));
    }

    @Test
    void getConutryISO2_InvalidCountryISO2_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/country/12"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Country ISO2 code must be exactly 2 uppercase letters"
                ));
    }

    @Test
    void getConutryISO2_ValidCountryISO2_NotFound() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/country/QQ"))
                .andExpect(status().isNotFound());
    }
    @Test
    void addSwiftCode_ValidInput_ReturnsOk() throws Exception {
        SwiftCodeDTO swiftCodeDTO = new SwiftCodeDTO();
        swiftCodeDTO.setAddress("UL. SZARA KRAKOW");
        swiftCodeDTO.setBankName("PKO BANK POLSKI");
        swiftCodeDTO.setCountryISO2("PL");
        swiftCodeDTO.setCountryName("POLAND");
        swiftCodeDTO.setHeadquarter(true);
        swiftCodeDTO.setSwiftCode("AABXBCAAXXX");

        MessageResponseDTO responseDTO = new MessageResponseDTO("Swift code added successfully");
        when(swiftCodeService.addSwiftCode(swiftCodeDTO)).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(swiftCodeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Swift code added successfully"));
    }
    @Test
    void addSwiftCode_InvalidInputAddress_ReturnsBadRequest() throws Exception {
        SwiftCodeDTO swiftCodeDTO = new SwiftCodeDTO();
        swiftCodeDTO.setAddress("Ul. SZARA KRAKOW");
        swiftCodeDTO.setBankName("PKO BANK POLSKI");
        swiftCodeDTO.setCountryISO2("PL");
        swiftCodeDTO.setCountryName("POLAND");
        swiftCodeDTO.setHeadquarter(true);
        swiftCodeDTO.setSwiftCode("AABCBCAAXXX");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(swiftCodeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("address: Address must contain only uppercase letters, numbers and basic punctuation"));

    }
    @Test
    void addSwiftCode_InvalidBankName_ReturnsBadRequest() throws Exception {
        SwiftCodeDTO swiftCodeDTO = new SwiftCodeDTO();
        swiftCodeDTO.setAddress("UL. SZARA KRAKOW");
        swiftCodeDTO.setBankName("PKO BANK 123");
        swiftCodeDTO.setCountryISO2("PL");
        swiftCodeDTO.setCountryName("POLAND");
        swiftCodeDTO.setHeadquarter(true);
        swiftCodeDTO.setSwiftCode("AABCBCAAXXX");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(swiftCodeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("bankName: Bank name must contain only uppercase letters"));
    }

    @Test
    void addSwiftCode_InvalidCountryISO2_ReturnsBadRequest() throws Exception {
        SwiftCodeDTO swiftCodeDTO = new SwiftCodeDTO();
        swiftCodeDTO.setAddress("UL. SZARA KRAKOW");
        swiftCodeDTO.setBankName("PKO BANK POLSKI");
        swiftCodeDTO.setCountryISO2("pL");
        swiftCodeDTO.setCountryName("POLAND");
        swiftCodeDTO.setHeadquarter(true);
        swiftCodeDTO.setSwiftCode("AABCBCAAXXX");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(swiftCodeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("countryISO2: Country ISO2 code must be exactly 2 uppercase letters"));
    }

    @Test
    void addSwiftCode_InvalidCountryName_ReturnsBadRequest() throws Exception {
        SwiftCodeDTO swiftCodeDTO = new SwiftCodeDTO();
        swiftCodeDTO.setAddress("UL. SZARA KRAKOW");
        swiftCodeDTO.setBankName("PKO BANK POLSKI");
        swiftCodeDTO.setCountryISO2("PL");
        swiftCodeDTO.setCountryName("Poland");
        swiftCodeDTO.setHeadquarter(true);
        swiftCodeDTO.setSwiftCode("AABCBCAAXXX");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(swiftCodeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("countryName: Country name must contain only uppercase letters"));
    }



}
