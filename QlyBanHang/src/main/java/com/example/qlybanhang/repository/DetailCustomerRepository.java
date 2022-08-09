package com.example.qlybanhang.repository;

import com.example.qlybanhang.Entity.DetailCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface DetailCustomerRepository extends JpaRepository<DetailCustomer, Integer> {
}
