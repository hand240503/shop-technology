package com.ndh.ShopTechnology.repository;

import com.ndh.ShopTechnology.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findOneByUsername(String username);

    Optional<UserEntity> findOneByPhoneNumber(String phoneNumber);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);
}
