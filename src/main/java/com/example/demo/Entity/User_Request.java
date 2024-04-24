package com.example.demo.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User_Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Req_Id;
    private long from_Acc_Id;


    private long to_Acc_Id;
    private double amount;

    public User_Request(long from_Acc_Id, long to_Acc_Id, double amount) {
        this.from_Acc_Id = from_Acc_Id;
        this.to_Acc_Id = to_Acc_Id;
        this.amount = amount;
    }
}
