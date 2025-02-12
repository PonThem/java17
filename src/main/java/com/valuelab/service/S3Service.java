package com.valuelab.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface S3Service {
    /**
     * upload file to S3
     * 
     * @param importExportHistoryId インポートエクスポート履歴テーブルのID
     * @param workbook
     * @return downloadKey
     */
    public String uploadFile(String importExportHistoryId, XSSFWorkbook workbook) throws IOException;

    /**
     * download file from S3
     * 
     * @param downloadKey
     * @return File
     */
    public File exportFile(String downloadKey) throws IOException;

    /**
     * download file from S3
     * 
     * @param downloadKey
     * @return InputStream
     */
    public InputStream exportFileAsStream(String downloadKey);

    /**
     * download file from S3
     * 
     * @param downloadKey
     * @return byte[]
     */
    public byte[] exportFileAsBytes(String downloadKey) throws IOException;
}
