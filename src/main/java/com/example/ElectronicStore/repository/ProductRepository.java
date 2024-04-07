package com.example.ElectronicStore.repository;

import com.example.ElectronicStore.entity.CategoryEntity;
import com.example.ElectronicStore.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findByNameContaining(String name, PageRequest pageRequest);

    Page<ProductEntity> findByIsLiveTrue(PageRequest pageRequest);

    Page<ProductEntity> findByCategory(CategoryEntity category, PageRequest pageRequest);

}
