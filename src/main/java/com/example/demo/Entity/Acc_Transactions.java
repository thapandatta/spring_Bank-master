package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;



@Data
@Entity
@Table

public class Acc_Transactions {

    @Id
    private long TransactionId;
    private long TransactionRefld;
    private long Acc_Id;
    private double Credit;
    private double Debit;
    private double AvlBalance;
    private Timestamp lastUpdated;
    public Acc_Transactions(long TransactionId,long TransactionRefld,long Acc_Id,double Credit,double Debit,double AvlBalance,Timestamp lastUpdated)
    {
        this();
        this.TransactionId=TransactionId;
        this.TransactionRefld=TransactionRefld;
        this.Acc_Id=Acc_Id;
        this.Credit=Credit;
        this.Debit=Debit;
        this.AvlBalance=AvlBalance;
        this.lastUpdated=lastUpdated;
    }

    public Acc_Transactions() {

    }
}
