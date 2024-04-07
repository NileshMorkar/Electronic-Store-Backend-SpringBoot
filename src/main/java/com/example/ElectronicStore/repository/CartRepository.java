package com.example.ElectronicStore.repository;

import com.example.ElectronicStore.entity.Cart;
import com.example.ElectronicStore.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(UserEntity userEntity);


}
