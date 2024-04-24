package com.example.demo.Repository;

import com.example.demo.Entity.Cust_Acc_Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface Acc_MapDao extends JpaRepository<Cust_Acc_Map,Long> {

    @Query(value="select * from Cust_Acc_Map where Cust_Id=:l",nativeQuery = true)
    Cust_Acc_Map findbycustId(long l);
}
