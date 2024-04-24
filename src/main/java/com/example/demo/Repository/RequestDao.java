package com.example.demo.Repository;

import com.example.demo.Entity.User_Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RequestDao extends JpaRepository<User_Request,Integer> {







}
