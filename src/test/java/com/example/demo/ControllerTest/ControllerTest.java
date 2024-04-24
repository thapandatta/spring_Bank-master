package com.example.demo.ControllerTest;

import com.example.demo.Controller.AdminController;
import com.example.demo.Entity.*;
import com.example.demo.ExceptionResponse;
import com.example.demo.Response.CustomerIdResponse;
import com.example.demo.Response.CustomerResponse;
import com.example.demo.Service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class
ControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    ObjectMapper objectMapper=new ObjectMapper();
    ObjectWriter objectWriter=objectMapper.writer();

    Date date = new Date(); // Current date and time
    Timestamp time = new Timestamp(new Date().getTime());



    Cust_Address cust_address1 = new Cust_Address(111L, "india", "hyderabad", "street1", 50600L, time);
    Cust_details cust_details1= new Cust_details(1L, "thapan", cust_address1, 789L, "thap@",time,time);

    Cust_Address cust_address2 = new Cust_Address(121L, "usa", "redmond", "street2", 1000L, time);
    Cust_details cust_details2 = new Cust_details(2L, "sohail", cust_address2, 998L, "sohail@",time,time);





    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc=MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void getAllSuccess() throws Exception {

        List<Cust_details> records = Arrays.asList(cust_details1,cust_details2);


        Mockito.when(adminService.allCustomers()).thenReturn(records);


        mockMvc.perform(MockMvcRequestBuilders.get("/allCustomers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cust_address.country", Matchers.is("india")));


        List<Cust_details> actual=adminController.getCustomers();
        assertEquals(2,actual.size());
    }


    @Test
    public void getdetailsbyId_Success() throws Exception
    {
        CustomerIdResponse customerIdResponse1=new CustomerIdResponse();
        customerIdResponse1.setEmail(cust_details1.getEmail());
        customerIdResponse1.setPhone(cust_details1.getPhone());
        customerIdResponse1.setCreated_On(cust_details1.getCreated());
        customerIdResponse1.setName(cust_details1.getName());
        customerIdResponse1.setCust_Id(cust_details1.getCust_Id());
        Mockito.when(adminService.getDetailsbyId(cust_details1.getCust_Id())).thenReturn(ResponseEntity.ok(customerIdResponse1));

        mockMvc.perform(MockMvcRequestBuilders.get("/getDetailsbyId/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("thapan")));

    }


    @Test
    public void addCustomer_Success() throws Exception
    {
        Cust_Address cust_address3= new Cust_Address(131L, "canada", "toronto", "street3", 2000L, time);
        Cust_details cust_details3= new Cust_details(3L, "shanmukh", cust_address3, 110L, "shanmukh@",time,time);

        CustomerResponse customerResponse1 = new CustomerResponse();
        customerResponse1.setName(cust_details3.getName());
        customerResponse1.setBalance(500.00);
        customerResponse1.setCust_Id(cust_details3.getCust_Id());
        Mockito.when(adminService.addCustdetails(cust_details3)).thenReturn(customerResponse1);

        String content=objectWriter.writeValueAsString(cust_details3);
        MockHttpServletRequestBuilder mocreq=MockMvcRequestBuilders.post("/addCustomer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mocreq)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("shanmukh")));
    }


    @Test
    public void getBalancewithoutParams_Success() throws Exception
    {
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/getBalance")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String res=result.getResponse().getContentAsString();
        mockMvc.perform(MockMvcRequestBuilders.get("/getBalance").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
        assertTrue(res.contains("Enter any value"));

    }

    @Test
    public void getBalancebyACCid_Success() throws Exception {
        Acc_Balance accBalance = new Acc_Balance(342L, 400);

        Mockito.when(adminService.getBalancebyAccId("342")).thenReturn(ResponseEntity.ok(accBalance));
        MockHttpServletRequestBuilder mocreq = MockMvcRequestBuilders.get("/getBalance")
                .contentType(MediaType.APPLICATION_JSON)
                .param("Acc_Id", "342");

        MvcResult result = mockMvc.perform(mocreq)
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();


        assertTrue(content.contains("\"acc_Id\":342"));
        assertTrue(content.contains("\"balance\":400"));
    }

    @Test
    public void getBalancebyCustid_Success() throws Exception {

        Cust_Address cust_address3= new Cust_Address(131L, "canada", "toronto", "street3", 2000L, time);
        Cust_details cust_details3= new Cust_details(3L, "shanmukh", cust_address3, 110L, "shanmukh@",time,time);
        Acc_Balance accBalance = new Acc_Balance(342L, 400);
        Cust_Acc_Map cust_acc_map=new Cust_Acc_Map(accBalance,cust_details3);


        Mockito.when(adminService.getBalancebyCustId("3")).thenReturn(ResponseEntity.ok(accBalance));

        MockHttpServletRequestBuilder mocreq = MockMvcRequestBuilders.get("/getBalance")
                .contentType(MediaType.APPLICATION_JSON)
                .param("Cust_Id", "3");

        MvcResult result = mockMvc.perform(mocreq)
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("\"acc_Id\":342"));
        assertTrue(content.contains("\"balance\":400"));
    }

    @Test
    public void getBalancebyCustidorAcc_Id_Success() throws Exception {


        Acc_Balance accBalance = new Acc_Balance(342L, 400);
        Mockito.when(adminService.getBalancebyAccId("342")).thenReturn(ResponseEntity.ok(accBalance));
        MockHttpServletRequestBuilder mocreq = MockMvcRequestBuilders.get("/getBalance")
                .contentType(MediaType.APPLICATION_JSON)
                .param("Acc_Id", "342")
                .param("Cust_Id","3");

        MvcResult result = mockMvc.perform(mocreq)
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();


        assertTrue(content.contains("\"acc_Id\":342"));
        assertTrue(content.contains("\"balance\":400"));

    }


        @Test
        public void addTransaction_Success() throws Exception
        {

            Acc_Balance accBalance1=new Acc_Balance(11L,300);
            Acc_Balance accBalance2=new Acc_Balance(22L,100);
            ExceptionResponse exceptionResponse=new ExceptionResponse("Transaction Successfull","200Ok");
            User_Request user_request=new User_Request(accBalance1.getAcc_Id(),accBalance2.getAcc_Id(),50);

            Mockito.when(adminService.addTransaction(user_request)).thenReturn(ResponseEntity.ok(exceptionResponse));
            ResponseEntity<Object> actual=adminController.addTransaction(user_request);
            assertTrue(actual.getBody().toString().contains("Transaction Successfull"));

        }

        @Test
        public void getAllTransaction_Success() throws Exception
        {
            Acc_Transactions accTransactions1=new Acc_Transactions(1L,111L,2345L,0,500,100,time);
            Acc_Transactions accTransactions2=new Acc_Transactions(2L,111L,3456L,500,0,600,time);
            List<Acc_Transactions> accTransactions=Arrays.asList(accTransactions1,accTransactions2);
            Mockito.when(adminService.getTransactions()).thenReturn(accTransactions);

            MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/allTransactions").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2))).andReturn();
            String s=result.getResponse().getContentAsString();
            assertTrue(s.contains("\"acc_Id\":2345"));
        }
}
