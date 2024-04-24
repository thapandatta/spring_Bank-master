package com.example.demo.Entity;


import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;

import java.sql.Timestamp;






@Entity
@Data
@Table
public class Cust_details {

    @Id
    private long Cust_Id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="Address_Id")
    private Cust_Address cust_address;
    private long phone;
    private String Email;

    private Timestamp Created;
    private Timestamp LastUpdated;

    public Cust_details(long cust_Id, String name, Cust_Address cust_address, long phone, String email, Timestamp created, Timestamp lastUpdated) {
        this();
        Cust_Id = cust_Id;
        this.name = name;
        this.cust_address = cust_address;
        this.phone = phone;
        this.Email = email;
        Created = created;
        LastUpdated = lastUpdated;
    }

    public Cust_details() {

    }
}
