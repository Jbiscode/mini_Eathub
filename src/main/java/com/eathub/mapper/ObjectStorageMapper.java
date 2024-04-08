package com.eathub.mapper;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ObjectStorageMapper {
    String uploadFile(String bucketName, String storage, MultipartFile img);

    void deleteFile(String bucketName, String s, String imageFileName);

    void deleteFile(String bucketName, String s, List<String> list);
}
