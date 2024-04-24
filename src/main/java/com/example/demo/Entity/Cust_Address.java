package com.example.demo.Entity;


import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Table
public class Cust_Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Address_Id;
    private String country;
    private String city;
    private String addressLane;
    private Long pin;

    @UpdateTimestamp
    private Timestamp LastUpdated;
    public Cust_Address(Long address_Id, String country, String city, String addressLane, Long pin, Timestamp lastUpdated) {
        this();
        this.Address_Id = address_Id;
        this.country = country;
        this.city = city;
        this.addressLane = addressLane;
        this.pin = pin;
        this.LastUpdated = lastUpdated;
    }


    public Cust_Address() {

    }
}
