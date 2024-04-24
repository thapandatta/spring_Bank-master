package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.engine.internal.Cascade;


@Data
@Entity
@Table
public class Cust_Acc_Map {

    @Id
    private long rid;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Acc_Id")
    private Acc_Balance acc_balance;

    public Cust_Acc_Map(Acc_Balance acc_balance, Cust_details cust_details) {
        this.acc_balance = acc_balance;
        this.cust_details = cust_details;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Cust_Id")
    private Cust_details cust_details;


    public Cust_Acc_Map() {

    }
}
