package com.atis.remitly_project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {
    List<SwiftCode> findByCountryISO2(String countryISO2);
    SwiftCode findBySwiftCode(String swiftCode);

    @Query("SELECT s FROM SwiftCode s WHERE s.swiftCode LIKE :prefix% AND s.swiftCode != :headquarterCode AND s.headquarter = false")
    List<SwiftCode> findBranchesByHeadquarterPrefix(@Param("prefix") String prefix, @Param("headquarterCode") String headquarterCode);
}