package com.atis.remitly_project;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
public class SwiftCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String countryISO2;
    @Column(unique = true)
    private String swiftCode;
    private String codeType;
    private String bankName;
    private String address;
    private String townName;
    private String countryName;
    private String timeZone;

    @Getter @Setter
    private boolean isHeadquarter;

    @PrePersist
    @PreUpdate
    public void setHeadquarterValue() {
        this.isHeadquarter = (this.swiftCode != null && this.swiftCode.endsWith("XXX"));
    }
}