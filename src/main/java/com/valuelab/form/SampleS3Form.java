package com.valuelab.form;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SampleS3Form {
    XSSFWorkbook workbook;
}
