package com.example.demo.Repository;

import com.example.demo.Entity.Cust_details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerDao extends JpaRepository<Cust_details,Long> {





}
