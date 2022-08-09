package com.example.qlybanhang.repository;

import com.example.qlybanhang.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findCategoryById(int id);
}
