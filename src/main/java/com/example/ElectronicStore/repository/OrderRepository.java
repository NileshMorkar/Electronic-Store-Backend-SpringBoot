package com.example.ElectronicStore.repository;

import com.example.ElectronicStore.entity.Order;
import com.example.ElectronicStore.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(UserEntity userEntity);

}
