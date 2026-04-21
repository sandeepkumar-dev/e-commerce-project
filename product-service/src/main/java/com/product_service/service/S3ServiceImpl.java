package com.product_service.service;

import com.product_service.entity.Brand;
import com.product_service.entity.Image;
import com.product_service.repository.BrandRepository;
import com.product_service.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3ServiceImpl implements  S3Service {

    private final S3Client s3Client;

    private ImageRepository  imageRepository;

    private BrandRepository brandRepository;

    @Value("${bucket-name}")
    private String bucketName;

    @Value("${region}")
    private String region;

    public S3ServiceImpl(S3Client s3Client, ImageRepository imageRepository, BrandRepository brandRepository) {
        this.s3Client = s3Client;
        this.imageRepository = imageRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public List<String> uploadFiles(MultipartFile[] files, int brandId) {

        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            try {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(file.getContentType())
                        .build();

                s3Client.putObject(
                        putObjectRequest,
                        RequestBody.fromInputStream(file.getInputStream(), file.getSize())
                );
                String fileUrl = "https://" + bucketName + ".s3." + region +".amazonaws.com/" + fileName;
                //String url = "https://" + bucketName + ".s3." + region +".amazonaws.com/" + fileName;
                //Save the data inside image entity

                Brand brand = brandRepository.findById(brandId).get();

                Image image = new Image();
                image.setBrand(brand);
                image.setUrl(fileUrl);

                imageRepository.save(image);



                fileUrls.add(fileUrl);

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
            }
        }

        return fileUrls;
    }

}