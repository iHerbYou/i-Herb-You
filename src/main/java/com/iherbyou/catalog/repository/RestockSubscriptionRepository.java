package com.iherbyou.catalog.repository;

import com.iherbyou.catalog.entity.ProductVariant;
import com.iherbyou.catalog.entity.RestockSubscription;
import com.iherbyou.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestockSubscriptionRepository extends JpaRepository<RestockSubscription, Long> {

    Optional<RestockSubscription> findByUserAndProductVariant(User user, ProductVariant productVariant);

}