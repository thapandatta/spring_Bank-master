package com.example.demo.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Acc_Balance {

    @Id
    private long Acc_Id;
    private double Balance;

    public Acc_Balance(long tempAccId,double i) {
        this();
        this.Acc_Id=tempAccId;
        this.Balance=i;
    }

    public Acc_Balance() {

    }
}
