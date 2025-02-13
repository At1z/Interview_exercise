package com.atis.remitly_project;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {
    List<SwiftCode> findByCountryISO2(String countryISO2);
    SwiftCode findBySwiftCode(String swiftCode);
}