package com.doc.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.doc.exception.UploadException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {
  private final AmazonS3 amazonS3;

  public S3Service(AmazonS3 amazonS3) {
    this.amazonS3 = amazonS3;
  }

  @Value("${aws.s3.bucket}")
  private String bucketName;

  @Value("${aws.s3.timeout}")
  private long signedUrlTime;

  public String generateSignedUrl(String key) {
    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += signedUrlTime;
    expiration.setTime(expTimeMillis);

    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucketName, key)
            .withMethod(HttpMethod.GET)
            .withExpiration(expiration);

    URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    return url.toExternalForm();
  }

  public String uploadFile(MultipartFile file) {
    String fileName = generateUniqueFileName(file.getOriginalFilename());

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    try {
      PutObjectRequest putRequest =
          new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata);
      amazonS3.putObject(putRequest);
      return fileName;
    } catch (IOException | SdkClientException e) {
      throw new UploadException("Could not upload Image", e);
    }
  }

  private String generateUniqueFileName(String originalFilename) {
    return UUID.randomUUID() + "_" + originalFilename;
  }
}
