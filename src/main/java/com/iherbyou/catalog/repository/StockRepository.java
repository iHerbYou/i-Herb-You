package com.iherbyou.catalog.repository;

import com.iherbyou.catalog.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
