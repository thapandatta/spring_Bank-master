package com.example.demo.Service;

import com.example.demo.Controller.AdminController;
import com.example.demo.Entity.*;
import com.example.demo.ExceptionResponse;
import com.example.demo.Repository.*;
import com.example.demo.Response.CustomerIdResponse;
import com.example.demo.Response.CustomerResponse;
import com.example.demo.getRandom;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ServiceTest {
    private MockMvc mockMvc;


    @Mock
    private CustomerDao customerDao;


    @Mock
    private TransactionDao transactionDao;


    @Mock
    private getRandom rand;


    @Mock
    private Acc_MapDao accMapDao;


    @Mock
    private BalanceDao balanceDao;



    @Mock
    private RequestDao requestDao;




    @InjectMocks
    private AdminService adminService;



    ObjectMapper objectMapper=new ObjectMapper();
    ObjectWriter objectWriter=objectMapper.writer();

    Date date = new Date(); // Current date and time
    Timestamp time = new Timestamp(new Date().getTime());


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc= MockMvcBuilders.standaloneSetup(adminService).build();
    }


    Cust_Address cust_address1 = new Cust_Address(111L, "india", "hyderabad", "street1", 50600L, time);
    Cust_details cust_details1= new Cust_details(1L, "thapan", cust_address1, 789L, "thap@",time,time);

    Cust_Address cust_address2 = new Cust_Address(121L, "usa", "redmond", "street2", 1000L, time);
    Cust_details cust_details2 = new Cust_details(2L, "sohail", cust_address2, 998L, "sohail@",time,time);


    @Test
    void allCustomers_success() throws  Exception{
        List<Cust_details> records = Arrays.asList(cust_details1,cust_details2);

        Mockito.when(customerDao.findAll()).thenReturn(records);
        List<Cust_details> actual=adminService.allCustomers();
        assertEquals(2,actual.size());
        assertEquals("sohail",records.get(1).getName().toString());
        assertEquals(998,records.get(1).getPhone());

    }


    @Test
   public void getdetailsbyId_Success() throws Exception
   {
       CustomerIdResponse expected=new CustomerIdResponse(cust_details1.getName(),cust_details1.getPhone(),cust_details1.getEmail(),cust_details1.getCust_Id(),cust_details1.getCreated());
       Mockito.when(customerDao.findById(cust_details1.getCust_Id())).thenReturn(Optional.ofNullable(cust_details1));
       ResponseEntity<Object> actual=adminService.getDetailsbyId(cust_details1.getCust_Id());
       Assertions.assertTrue(actual.getBody().toString().contains(expected.getName()));
      Assertions.assertTrue(actual.getBody().toString().contains(String.valueOf(expected.getPhone())));
   }


   @Test
    public void getDetailsbyID_failure() throws Exception
   {
       Mockito.when(customerDao.findById(cust_details1.getCust_Id())).thenReturn(Optional.empty());
       ExceptionResponse exceptionResponse=new ExceptionResponse("enter valid id","hctb404");
       ResponseEntity<Object> actual=adminService.getDetailsbyId(456);
       Assertions.assertTrue(actual.getBody().toString().contains(exceptionResponse.getMessage()));
       Assertions.assertTrue(actual.getBody().toString().contains(exceptionResponse.getCode()));

   }




   @Test
    public void addcustdetails_Success() throws Exception
   {
       Cust_Address cust_address3 = new Cust_Address(131L, "canada", "toronto", "street3", 2000L, time);
       Cust_details cust_details3 = new Cust_details(3L, "vikas", cust_address3, 667L, "vikas@",time,time);
       Acc_Balance accBalance=new Acc_Balance(555L,500);
       Cust_Acc_Map accMap=new Cust_Acc_Map(accBalance,cust_details3);
     Mockito.when( rand.getrandom()).thenReturn(677L);
       Mockito.when( customerDao.save(cust_details1)).thenReturn(cust_details1);
       Mockito.when(balanceDao.save(accBalance)).thenReturn(accBalance);
       Mockito.when(accMapDao.save(accMap)).thenReturn(accMap);
       CustomerResponse customerResponse=adminService.addCustdetails(cust_details3);
       assertEquals(500,customerResponse.getBalance());
       assertEquals(customerResponse.getName(),cust_details3.getName());

   }


   @Test
   public void getbalancebyAcc_id_Success() throws Exception
   {
       Cust_Address cust_address3 = new Cust_Address(131L, "canada", "toronto", "street3", 2000L, time);
       Cust_details cust_details3 = new Cust_details(3L, "vikas", cust_address3, 667L, "vikas@",time,time);
       Acc_Balance accBalance=new Acc_Balance(555L,500);
       Cust_Acc_Map accMap=new Cust_Acc_Map(accBalance,cust_details3);
       Mockito.when(balanceDao.findById(Long.parseLong(String.valueOf(accBalance.getAcc_Id())))).thenReturn(Optional.of(accBalance));
       ResponseEntity<Object> actual=adminService.getBalancebyAccId(String.valueOf(accBalance.getAcc_Id()));
       Assertions.assertTrue(actual.getBody().toString().contains("500"));

   }


   @Test
    public void getBalancebyaccId_Failure() throws Exception
   {
       Mockito.when(balanceDao.findById(Long.parseLong("8574"))).thenReturn(Optional.empty());
       ResponseEntity<Object> actual=adminService.getBalancebyAccId("8574");
       Assertions.assertTrue(actual.getBody().toString().contains("error"));

   }

   @Test
    public void getBalancebycustId_success() throws Exception
   {
       Cust_Address cust_address3 = new Cust_Address(131L, "canada", "toronto", "street3", 2000L, time);
       Cust_details cust_details3 = new Cust_details(3L, "vikas", cust_address3, 667L, "vikas@",time,time);
       Acc_Balance expected=new Acc_Balance(867L,5000);
       Cust_Acc_Map cust_acc_map=new Cust_Acc_Map(expected,cust_details3);
       Mockito.when(accMapDao.findbycustId(Long.parseLong(String.valueOf(111L)))).thenReturn(cust_acc_map);
       Mockito.when(balanceDao.findById(cust_acc_map.getAcc_balance().getAcc_Id())).thenReturn(Optional.of(expected));
       ResponseEntity<Object> actual=adminService.getBalancebyCustId("111");
       Assertions.assertTrue(actual.getBody().toString().contains("5000"));

   }


   @Test
   public void getbalancebycustId_failure() throws Exception
   {
       Cust_Address cust_address3 = new Cust_Address(131L, "canada", "toronto", "street3", 2000L, time);
       Cust_details cust_details3 = new Cust_details(3L, "vikas", cust_address3, 667L, "vikas@",time,time);
       Acc_Balance expected=new Acc_Balance(867L,5000);
       Cust_Acc_Map cust_acc_map=new Cust_Acc_Map(expected,cust_details3);
       Mockito.when(accMapDao.findbycustId(Long.parseLong(String.valueOf(111L)))).thenReturn(cust_acc_map);
       Mockito.when(balanceDao.findById(cust_acc_map.getAcc_balance().getAcc_Id())).thenReturn(Optional.empty());
       ResponseEntity<Object> actual=adminService.getBalancebyCustId("111");
       Assertions.assertTrue(actual.getBody().toString().contains("enter valid custID"));
   }

    @Test
    public void allTransaction_Success() throws Exception{

        Acc_Transactions accTransactions1=new Acc_Transactions(rand.getrandom(), rand.getrandom(), 11L, 0, 500, 100,time);
        Acc_Transactions accTransactions2=new Acc_Transactions(rand.getrandom(), rand.getrandom() ,22L, 599,0, 200,time);
        List<Acc_Transactions> record=Arrays.asList(accTransactions1,accTransactions2);

        Mockito.when(transactionDao.findAll()).thenReturn(record);
        List<Acc_Transactions> actual=adminService.getTransactions();
        assertEquals(actual.get(0).getAvlBalance(),100);
        assertEquals(actual.get(1).getCredit(),599);
    }


    @Test
    public void addTransaction_Success() throws Exception
    {
        Acc_Balance accBalance1=new Acc_Balance(111,500);
        Acc_Balance accBalance2=new Acc_Balance(222,500);
        User_Request userRequest=new User_Request(111,222,100);
        Mockito.when(balanceDao.findById(userRequest.getFrom_Acc_Id())).thenReturn(Optional.of(accBalance1));
        Mockito.when(balanceDao.findById(userRequest.getTo_Acc_Id())).thenReturn(Optional.of(accBalance2));
        Mockito.when( requestDao.save(userRequest)).thenReturn(userRequest);
        Mockito.when( rand.getrandom()).thenReturn(463L);
        Acc_Transactions accTransactions1=new Acc_Transactions(rand.getrandom(),rand.getrandom() , userRequest.getFrom_Acc_Id(), 0, userRequest.getAmount(), accBalance1.getBalance(),time);
        Acc_Transactions accTransactions2=new Acc_Transactions(rand.getrandom(),rand.getrandom() , userRequest.getTo_Acc_Id(), userRequest.getAmount() ,0, accBalance2.getBalance(),time);
        Mockito.when(transactionDao.save(accTransactions1)).thenReturn(accTransactions1);
        Mockito.when(transactionDao.save(accTransactions2)).thenReturn(accTransactions2);
        ResponseEntity<Object> actual=adminService.addTransaction(userRequest);
        Assertions.assertTrue(actual.getBody().toString().contains("Transaction Successful"));

    }



    @Test
    public void adddTransaction_NullFailure() throws Exception {
        User_Request userRequest = new User_Request(111, 222, 100);

        Mockito.when(balanceDao.findById(111L)).thenReturn(Optional.empty());
        Mockito.when((balanceDao.findById(222L))).thenReturn(Optional.empty());

        ResponseEntity<Object> actual = adminService.addTransaction(userRequest);
        System.out.println(actual.getBody().toString());
        Assertions.assertTrue(actual.getBody().toString().contains("Invalid details"));
    }

    @Test
    public void addTransaction_insufficientFailure() throws Exception
    {
        User_Request userRequest = new User_Request(111, 222, 600);
        Acc_Balance accBalance1=new Acc_Balance(111,500);
        Acc_Balance accBalance2=new Acc_Balance(222,500);
        Mockito.when(balanceDao.findById(userRequest.getFrom_Acc_Id())).thenReturn(Optional.of(accBalance1));
        Mockito.when(balanceDao.findById(userRequest.getTo_Acc_Id())).thenReturn(Optional.of(accBalance2));
        ResponseEntity<Object> actual=adminService.addTransaction(userRequest);
        Assertions.assertTrue(actual.getBody().toString().contains("No sufficient funds"));

    }

    @Test
    public void getRndom() throws Exception
    {
        Mockito.when(rand.getrandom()).thenReturn(556L);
        long actual=rand.getrandom();
        assertNotEquals(actual,75L);
    }





}