package com.iherbyou.catalog.service;

import com.iherbyou.catalog.Product;
import com.iherbyou.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    public List<Product> getProductsByCategory(Long category_id, Pageable pageable) {
        return productRepository.findByProductCategories_CategoryId(category_id, pageable);
    }

}
