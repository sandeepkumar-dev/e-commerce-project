package com.product_service.controller;


import com.product_service.dto.ApiResponse;
import com.product_service.dto.CategoryDto;
import com.product_service.dto.ProductDto;
import com.product_service.service.CategoryService;
import com.product_service.service.ProductService;
import com.product_service.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {


    private CategoryService categoryService;

    private ProductService productService;
    private final S3Service s3Service;

    public ProductController(CategoryService categoryService, ProductService productService, S3Service s3Service) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.s3Service = s3Service;
    }

    @GetMapping("/list/categories")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getCategories(){
        List<CategoryDto> categoriesDto = categoryService.findAll();
        ApiResponse<List<CategoryDto>> response = new ApiResponse<>();
        if(categoriesDto!=null)
        {
            response.setMessage("All categories data fetched");
            response.setStatus(200);
            response.setData(categoriesDto);
            return  new ResponseEntity<>(response, HttpStatus.OK);

        }

        response.setMessage("No categories data Found");
        response.setStatus(500);
        response.setData(null);
        return  new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/list/search")
    public ResponseEntity<ApiResponse<List<ProductDto>>> searchProducts(
            @RequestParam String keyword
    ) {
        List<ProductDto> productDtos = productService.searchProducts(keyword);

        ApiResponse<List<ProductDto>> response = new ApiResponse<>();

        if (productDtos != null && !productDtos.isEmpty()) {
            response.setMessage("All Product data fetched");
            response.setStatus(200);
            response.setData(productDtos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setMessage("No Product data found");
        response.setStatus(500);
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("brandId") int brandId) {

        System.out.println("API HIT ✅ brandId = " + brandId);

        return ResponseEntity.ok(s3Service.uploadFiles(files, brandId));
    }

}
