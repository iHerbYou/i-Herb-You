package com.iherbyou.ordering.repository;

import com.iherbyou.ordering.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUser_IdOrderByOrderDateDesc(Long userId, Pageable pageable);

    boolean existsByIdAndUser_Id(Long orderId, Long userId);

    @Query("""
      select distinct o
      from Order o
      join fetch o.orderStatusCode osc
      left join fetch o.delivery d
      left join fetch o.orderProducts op
      left join fetch op.productVariant pv
      left join fetch pv.product p
      where o.id = :orderId
    """)
    Optional<Order> findWithDetailsById(@Param("orderId") Long orderId);

}