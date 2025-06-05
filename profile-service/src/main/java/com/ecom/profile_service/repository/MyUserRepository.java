package com.ecom.profile_service.repository;

import com.ecom.profile_service.entity.EcomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserRepository extends JpaRepository<EcomUser, Long> {
    Optional<EcomUser> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);
}

