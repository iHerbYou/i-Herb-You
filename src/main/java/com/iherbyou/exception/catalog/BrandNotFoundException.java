package com.iherbyou.exception.catalog;

public class BrandNotFoundException extends RuntimeException {

    public BrandNotFoundException(Long brandId) {
        super("존재하지 않는 브랜드입니다. (id=" + brandId + ")");
    }
    
}
