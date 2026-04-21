package com.product_service.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {
    List<String> uploadFiles(MultipartFile[] files, int brandId);
}
