package com.example.demo.Response;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CustomerResponse {

    private String name;
    private long Cust_Id;
    private double Balance;

}
