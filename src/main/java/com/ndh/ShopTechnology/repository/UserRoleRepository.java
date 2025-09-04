package com.ndh.ShopTechnology.repository;

import com.ndh.ShopTechnology.entities.user.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    UserRoleEntity getUserRoleEntityByCode(String code);

}
