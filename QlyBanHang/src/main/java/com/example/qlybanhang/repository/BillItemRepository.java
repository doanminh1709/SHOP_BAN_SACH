package com.example.qlybanhang.repository;

import com.example.qlybanhang.Entity.BillItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface BillItemRepository extends JpaRepository<BillItems, Integer> {
}
