package com.example.demo.Response;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Data
public class CustomerIdResponse {

    private String name;
    private long phone;
    private String email;
    private long Cust_Id;
    private Timestamp Created_On;

    public CustomerIdResponse() {
    }

    public CustomerIdResponse(String name, long phone, String email, long Cust_Id, Timestamp created_On) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.Cust_Id = Cust_Id;
        this.Created_On = Created_On;
    }




}
