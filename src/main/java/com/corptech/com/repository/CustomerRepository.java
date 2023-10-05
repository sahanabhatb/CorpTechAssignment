package com.corptech.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corptech.com.model.Customer;
import com.corptech.com.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {

	public Optional<Customer> findById(String Id);

	public Customer getById(String Id);

	public Optional<Customer> findByEmail(String string);

}
