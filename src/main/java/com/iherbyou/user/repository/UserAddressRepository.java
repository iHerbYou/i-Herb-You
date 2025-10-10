package com.iherbyou.user.repository;

import com.iherbyou.user.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    List<UserAddress> findByUser_IdOrderByIsDefaultDescIdDesc(Long userId);

    Optional<UserAddress> findByIdAndUser_Id(Long addressId, Long userId);

    boolean existsByUser_IdAndIsDefaultTrue(Long userId);

    Optional<UserAddress> findFirstByUser_IdOrderByIdAsc(Long userId);
}
