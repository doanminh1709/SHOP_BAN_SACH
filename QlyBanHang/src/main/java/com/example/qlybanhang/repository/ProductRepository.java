package com.example.qlybanhang.repository;

import com.example.qlybanhang.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findProductById(int id);

    @Query("select u from Product  u join Category  n where n.name like :name")
//    Page<Product> searchByCategoryName(@Param("name") String name, Pageable pageable);
    Product searchByCategoryName(@Param("name")String name);

    @Query("select u from Product  u where u.name like :name")
//    Page<Product> searchByName(@Param("name") String name, Pageable pageable);
    Product searchByName(@Param("name") String name);

    @Query("select u from Product  u where u.name like :name")
    Page<Product> searchByNameProduct(@Param("name") String name, Pageable pageable);



}
