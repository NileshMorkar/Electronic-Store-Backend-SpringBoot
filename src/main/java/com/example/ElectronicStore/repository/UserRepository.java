package com.example.ElectronicStore.repository;

import com.example.ElectronicStore.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String userEmail);

    List<UserEntity> findByNameContaining(String name);

}
