package com.atis.remitly_project;

import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelParser {
    public List<SwiftCode> parse(String filePath) throws IOException {
        List<SwiftCode> swiftCodes = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                SwiftCode swiftCode = new SwiftCode();
                swiftCode.setCountryISO2(row.getCell(0).getStringCellValue().toUpperCase());
                swiftCode.setSwiftCode(row.getCell(1).getStringCellValue());
                swiftCode.setCodeType(row.getCell(2).getStringCellValue());
                swiftCode.setBankName(row.getCell(3).getStringCellValue());
                swiftCode.setAddress(row.getCell(4).getStringCellValue());
                swiftCode.setTownName(row.getCell(5).getStringCellValue());
                swiftCode.setCountryName(row.getCell(6).getStringCellValue().toUpperCase());
                swiftCode.setTimeZone(row.getCell(7).getStringCellValue());
                swiftCodes.add(swiftCode);
            }
        }

        return swiftCodes;
    }
}