package com.atis.remitly_project;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

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
    void getSwiftCode_ValidSwiftCode_isOk() throws Exception {
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
}
