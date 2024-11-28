package com.c1se62.clinic_booking.repository;

import com.c1se62.clinic_booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    void deleteByUserId(Integer id);

    Optional<User> findById(Integer id);

    void deleteById(Integer id);
}
