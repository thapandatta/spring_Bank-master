package com.example.demo.Service;


import com.example.demo.ExceptionResponse;
import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import com.example.demo.Response.CustomerIdResponse;
import com.example.demo.Response.CustomerResponse;
import com.example.demo.getRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

import java.util.Date;

@Service
public class AdminService {

    @Autowired
    CustomerDao customerDao;

    @Autowired
    TransactionDao transactionDao;

    @Autowired
    RequestDao requestDao;

    @Autowired
    AddressDao addressDao;

    @Autowired
    Acc_MapDao accMapDao;

    @Autowired
    JdbcTemplate template;

    @Autowired
    getRandom rand;
    Timestamp time = new Timestamp(new Date().getTime());

    @Autowired
    BalanceDao balanceDao;

    @Autowired
    CustomerResponse customerResponse;

    @Autowired
    CustomerIdResponse customerIdResponse;


    Date date = new Date();


    Timestamp timestamp = new Timestamp(date.getTime());

    public List<Cust_details> allCustomers() {

        return  customerDao.findAll();
    }

    public CustomerResponse addCustdetails(Cust_details cust_details) {
        cust_details.setCreated(time);
        cust_details.setLastUpdated(time);
        long temp_cust_id = rand.getrandom();
        long temp_acc_id = rand.getrandom();
        long rid = rand.getrandom();
        cust_details.setCust_Id(temp_cust_id);


        customerDao.save(cust_details);
        Cust_Acc_Map cust_acc_map = new Cust_Acc_Map();
        cust_acc_map.setRid(rid);
        Acc_Balance accBalance = new Acc_Balance(temp_acc_id, 500);
        cust_acc_map.setAcc_balance(accBalance);
        cust_acc_map.setCust_details(cust_details);

        balanceDao.save(accBalance);
        accMapDao.save(cust_acc_map);

        CustomerResponse customerResponse1 = new CustomerResponse();
        customerResponse1.setName(cust_details.getName());

        customerResponse1.setBalance(500.00);
        customerResponse1.setCust_Id(temp_cust_id);
        return customerResponse1;
    }

    public  ResponseEntity<Object> getDetailsbyId(long custId) {

            Optional<Cust_details> c1 = customerDao.findById(custId);

            if (c1.isPresent()) {
               CustomerIdResponse customerIdResponse1=new CustomerIdResponse();
               customerIdResponse1.setEmail(c1.get().getEmail());
               customerIdResponse1.setPhone(c1.get().getPhone());
               customerIdResponse1.setCreated_On(c1.get().getCreated());
               customerIdResponse1.setName(c1.get().getName());
               customerIdResponse1.setCust_Id(c1.get().getCust_Id());
               return ResponseEntity.ok(customerIdResponse1);
            } else {
                return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new ExceptionResponse("enter valid id", "hctb404"));
            }
        }



    public ResponseEntity<Object> getBalancebyAccId(String accId) {
        Optional<Acc_Balance> Balance=balanceDao.findById(Long.parseLong(accId));
       if(Balance.isPresent())
       {
           return ResponseEntity.ok(Balance);
       }
           return  ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new ExceptionResponse("error","hctb404"));
       }


    public ResponseEntity<Object> getBalancebyCustId(String custId) {
        Cust_Acc_Map custAccMap=accMapDao.findbycustId(Long.parseLong(custId));
        Optional<Acc_Balance> accBalance=balanceDao.findById(custAccMap.getAcc_balance().getAcc_Id());
        if(accBalance.isPresent())
        {
            return ResponseEntity.ok(accBalance);
        }
        else
        {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new ExceptionResponse("enter valid custID","404"));
        }



    }

    public ResponseEntity<Object> addTransaction(User_Request userRequest) {


            Optional<Acc_Balance> b1=balanceDao.findById(userRequest.getFrom_Acc_Id());
            Optional<Acc_Balance> b2=balanceDao.findById(userRequest.getTo_Acc_Id());

            if(b1.isEmpty()|| b2.isEmpty())
            {
                return ResponseEntity.ok(new ExceptionResponse("Invalid details","200K"));
            }
            if(b1.get().getBalance()< userRequest.getAmount())
            {
                return ResponseEntity.ok(new ExceptionResponse("No sufficient funds","200oK"));
            }
            double from_acc_balace=b1.get().getBalance();
            double to_acc_balance=b2.get().getBalance();
            requestDao.save(userRequest);
            b1.get().setBalance(from_acc_balace-userRequest.getAmount());
            b1.get().setBalance(to_acc_balance+ userRequest.getAmount());
            long tid=rand.getrandom();

            Acc_Transactions accTransactions1=new Acc_Transactions(rand.getrandom(), tid, userRequest.getFrom_Acc_Id(), 0, userRequest.getAmount(), b1.get().getBalance(),timestamp);
            Acc_Transactions accTransactions2=new Acc_Transactions(rand.getrandom(), tid, userRequest.getTo_Acc_Id(), userRequest.getAmount() ,0, b2.get().getBalance(),timestamp);
            transactionDao.save(accTransactions1);
            transactionDao.save(accTransactions2);
            return ResponseEntity.ok(new ExceptionResponse("Transaction Successful","200oK"));


    }

    public List<Acc_Transactions> getTransactions() {

        return transactionDao.findAll();
    }

}
