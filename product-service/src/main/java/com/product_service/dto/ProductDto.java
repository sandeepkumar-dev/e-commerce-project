package com.product_service.dto;

import java.util.LinkedHashSet;
import java.util.Set;

public class ProductDto {

        private  Integer id;
        private String  name;
        private SubCategoryDto subCategoryDto;

        private Set<BrandDto> brands = new LinkedHashSet<>();

    public SubCategoryDto getSubCategoryDto() {
        return subCategoryDto;
    }

    public void setSubCategoryDto(SubCategoryDto subCategoryDto) {
        this.subCategoryDto = subCategoryDto;
    }

    public Set<BrandDto> getBrands() {
        return brands;
    }

    public void setBrands(Set<BrandDto> brands) {
        this.brands = brands;
    }

    public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


