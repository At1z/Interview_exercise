package com.atis.remitly_project;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class SwiftCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String countryISO2;
    private String swiftCode;
    private String codeType;
    private String bankName;
    private String address;
    private String townName;
    private String countryName;
    private String timeZone;
    private boolean headquarter;

    @PrePersist
    @PreUpdate
    public void setHeadquarterValue() {
        this.headquarter = (this.swiftCode != null && this.swiftCode.endsWith("XXX"));
    }
}