package com.example.demo.Repository;


import com.example.demo.Entity.Cust_Address;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDao extends JpaRepository<Cust_Address, Long> {



}
