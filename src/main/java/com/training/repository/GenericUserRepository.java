package com.training.repository;

import com.training.entity.GenericUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenericUserRepository extends JpaRepository<GenericUser,Integer> {
    Optional<GenericUser> findByEmail(String email);
}
