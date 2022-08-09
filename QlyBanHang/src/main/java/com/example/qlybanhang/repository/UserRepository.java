package com.example.qlybanhang.repository;

import com.example.qlybanhang.Entity.Bill;
import com.example.qlybanhang.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);
    @Query("select u.id from User u where u.username like :x")
    int searchIdCodeByUserNam(@Param("x") String userName);
}
