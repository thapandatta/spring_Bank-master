package com.example.demo.Repository;

import com.example.demo.Entity.Acc_Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionDao extends JpaRepository<Acc_Transactions,Long> {



}
