package com.example.qlybanhang.repository;

import com.example.qlybanhang.Entity.Bill;
import com.example.qlybanhang.dto.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface BillRepository extends JpaRepository<Bill, Integer> {

   // 1: search number bill by time
    @Query("select x from Bill x where x.buyDate >= :date_start and x.buyDate <= :date_end")
    Bill searchBillByDateTime(@Param("date_start") Date start, @Param("date_end") Date end);

    //C1
    //@Query("SELECT count(b.id) as number, MONTH(b.buyDate) as month FROM Bill b where b.buyDate = :x GROUP BY MONTH(b.buyDate)")
    @Query("SELECT MONTH(b.buyDate) as THANG, COUNT(b.id) as SO_LUONG FROM Bill b GROUP BY MONTH(b.buyDate)")
    List<Object[]> searchBillByMonth();

    //3 : Search account order by user
    @Query("select b.users.name as Name, count(b.users.id) as Account  from Bill b where b.users.name like :name group by b.users.id")
    List<Object[]> searchBillByUser(@Param("name") String name);

    //4 : Search number order by coupon code
    @Query("select  u from Bill u where u.buyDate >:date")
//    Bill searchBillByDate(Date parse, Date date);
    Bill searchBillByDate(@Param("date") Date parse);

}

