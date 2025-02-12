package com.valuelab.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

@Service
public class S3ServiceImpl implements S3Service {

    @Value("${s3.bucketname}")
    private String bucketName;

    @Value("${s3.foldername}")
    private String s3FolderName;

    @Autowired
    private AmazonS3 amazonS3;

    /**
     * upload file to S3
     * 
     * @param importExportHistoryId インポートエクスポート履歴テーブルのID
     * @param csvFile
     * @return downloadKey
     */
    @Override
    public String uploadFile(String importExportHistoryId, XSSFWorkbook workbook) throws IOException {
        String fileName = "export.xlsx";
        String key = s3FolderName + "/" + importExportHistoryId + "/" + fileName;

        // trans XSSFWorkbook to InputStream
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            workbook.write(baos);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, null);
            amazonS3.putObject(putObjectRequest);
        }
        // real download-key is file path, can't be used to http response at aws-env
        return importExportHistoryId;
    }

    /**
     * download file from S3
     * 
     * @param downloadKey
     * @return File
     */
    @Override
    public File exportFile(String downloadKey) throws IOException {
        // trans id to real download-key
        downloadKey = transImportExportHistoryIdToDownloadkey(downloadKey);

        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, downloadKey));
        File file = File.createTempFile("download-", ".xlsx"); // Create a unique temp file

        try (InputStream inputStream = s3Object.getObjectContent();
                FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[8192]; // Use a larger buffer size for efficiency
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return file;
    }

    /**
     * download file from S3
     * 
     * @param downloadKey
     * @return InputStream
     */
    @Override
    public InputStream exportFileAsStream(String downloadKey) {
        // trans id to real download-key
        downloadKey = transImportExportHistoryIdToDownloadkey(downloadKey);

        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, downloadKey));
        return s3Object.getObjectContent();
    }

    /**
     * download file from S3
     * 
     * @param downloadKey
     * @return byte[]
     */
    @Override
    public byte[] exportFileAsBytes(String downloadKey) throws IOException {
        // trans id to real download-key
        downloadKey = transImportExportHistoryIdToDownloadkey(downloadKey);

        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, downloadKey));
        try (InputStream inputStream = s3Object.getObjectContent()) {
            return inputStream.readAllBytes();
        }
    }

    /**
     * trans transImportExportHistoryId from to real download-key, real download-key
     * is file path, can't be used to http response at aws-env
     * 
     * @param fileName
     * @return extension
     */
    private String transImportExportHistoryIdToDownloadkey(String importExportHistoryId) {
        return s3FolderName + "/" + importExportHistoryId + "/" + "export.xlsx";
    }
}
