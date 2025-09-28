package com.ndh.ShopTechnology.repository;

import com.ndh.ShopTechnology.entities.user.UserAuthRightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRightRepository extends JpaRepository<UserAuthRightEntity, Long> {

    UserAuthRightEntity findByUserIdAndType(Long id,int type);
}
