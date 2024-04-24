package com.example.demo.Controller;


import com.example.demo.Entity.*;
import com.example.demo.ExceptionResponse;
import com.example.demo.Response.CustomerIdResponse;
import com.example.demo.Response.CustomerResponse;
import com.example.demo.Service.AdminService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AdminController {


    @Autowired
    AdminService adminService;

    @GetMapping("/allCustomers")
    public List<Cust_details> getCustomers() {
        return adminService.allCustomers();
    }

    @PostMapping("/addCustomer")
    public CustomerResponse addCustomer(@RequestBody Cust_details cust_details) {
        return adminService.addCustdetails(cust_details);
    }

    @GetMapping("/getDetailsbyId/{cust_Id}")
    public ResponseEntity<Object> getDetailsbyId(@PathVariable long cust_Id) {
        return adminService.getDetailsbyId(cust_Id);
    }
    @GetMapping("/getBalance")
    public ResponseEntity<Object> getBalance(@RequestParam(required = false) String Cust_Id, @RequestParam(required = false) String Acc_Id) {
        if (Cust_Id == null && Acc_Id == null) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new ExceptionResponse("Enter any value","hctb404"));
        } else if (Acc_Id != null) {
            return adminService.getBalancebyAccId(Acc_Id);
        } else {
            return adminService.getBalancebyCustId(Cust_Id);
        }
    }

    @PostMapping("/addTransaction")
    public ResponseEntity<Object> addTransaction(@RequestBody User_Request userRequest)
    {
        return adminService.addTransaction(userRequest);
    }


    @GetMapping("/allTransactions")
    public List<Acc_Transactions> getTransactions() {
        return adminService.getTransactions();
    }
}