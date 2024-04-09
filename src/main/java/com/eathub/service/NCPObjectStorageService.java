package com.eathub.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.eathub.conf.NaverConfiguration;
import com.eathub.mapper.ObjectStorageMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class NCPObjectStorageService implements ObjectStorageMapper {

    final AmazonS3 s3;

    public NCPObjectStorageService(NaverConfiguration naverConfiguration) {
        s3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder
                                .EndpointConfiguration(naverConfiguration.getEndPoint(),
                                naverConfiguration.getRegionName())
                )
                .withCredentials(new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(naverConfiguration.getAccessKey(),
                                        naverConfiguration.getSecretKey())
                        )
                )
                .build();
    }

    @Override
    public String uploadFile(String bucketName, String directoryPath, MultipartFile img) {
        if (img.isEmpty()) return null;

        try (InputStream fileIn = img.getInputStream()) {
            String originalFileName = img.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // UUID 와 파일의 확장자를 합쳐서 파일명으로 사용
            String fileName = UUID.randomUUID() + extension;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(img.getContentType());

            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName
                            , directoryPath + fileName
                            , fileIn
                            , objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);

            // 실제로 올라가는 부분
            s3.putObject(putObjectRequest);

            return fileName;

        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 에러 " + e);// 강제로 Exception 발생시키는 throw
            // e.printStackTrace(); // return 타입이 String 이기때문에 e.print~는 안된다. String을 리턴하지 않기때문에
        }
    }

    @Override
    public void deleteFile(String bucketName, String directoryPath, String imageFileName) {
        s3.deleteObject(bucketName, directoryPath + imageFileName);
    }

    @Override
    public void deleteFile(String bucketName, String directoryPath, List<String> list) {
        for (String imageFileName : list) {
            s3.deleteObject(bucketName, directoryPath + imageFileName);
        }
    }
}
