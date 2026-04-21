package com.product_service.repository;

import com.product_service.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
}