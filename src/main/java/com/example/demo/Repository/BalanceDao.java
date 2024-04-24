package com.example.demo.Repository;

import com.example.demo.Entity.Acc_Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BalanceDao extends JpaRepository<Acc_Balance,Long> {

}
