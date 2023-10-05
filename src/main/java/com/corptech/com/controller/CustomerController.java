package com.corptech.com.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.corptech.com.model.Customer;
import com.corptech.com.repository.CustomerRepository;
import com.corptech.com.repository.SalesforceDataService;
import com.corptech.com.service.CustomerService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/customers")
@Api(value = "Customer API", tags = "Customer")
@CrossOrigin(allowCredentials = "true", origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET,
		RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT })
public class CustomerController {

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	private CustomerService customerService;
	

	// Get all customer
	@GetMapping(value = "")
	@ApiOperation(value = "Get all customer", response = String.class)
	public ResponseEntity<Object> getAllCustomer() {

		try {
			return customerService.getAllCustomer();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	// Get Particular customer based on Id
	@GetMapping(value = "/{Id}")
	@ApiOperation(value = "Get customer by customer id", response = String.class)
	public ResponseEntity<Object> CustomerByCustId(@PathVariable(value = "Id") String Id) {

		try {
			return customerService.customerByCustId(Id);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}


	// Create new customer
	@PostMapping(value = "")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Create new customer", response = String.class)
	public ResponseEntity<Object> creatCustomer(@RequestBody Customer customer) {

		try {
			customer.setId(UUID.randomUUID().toString());
			Optional<Customer> savedProduct= customerService.creatCustomer(customer);
			return ResponseEntity.ok().body(savedProduct);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error Message: " + e.getMessage());
		}
	}

	// Edit Particular customer based on Id
	@PutMapping(value = "/{Id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Edit particular customer", response = String.class)
	public ResponseEntity<Object> editCustomer(@PathVariable("Id") String Id, @RequestBody Customer customer) {

		Optional<Customer> editedCustomer = customerService.editCustomer(Id, customer);
		if (editedCustomer!=null) {
			// Customer found, return it
			return ResponseEntity.ok().body(editedCustomer);
		} else {
			// Customer not found, return a JSON response
			Map<String, String> response = new HashMap<>();
			response.put("message", "No data found for the ID provided");
			response.put("status", "fail");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	// Delete Particular customer based on Id
	@DeleteMapping(value = "/{Id}")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Delete particular customer", response = String.class)
	public Map<String, Boolean> deleteCustomer(@PathVariable(value = "Id") String Id) {

		try {
			return customerService.deleteCustomer(Id);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return Collections.emptyMap();
	}
	
	// sync customer
	@PostMapping(value = "/sync")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "Synch Data from MySQL to Salesforce ", response = String.class)
	public Map SynchCustomersToSalesforce() {
		try {
			return customerService.synchCustomersToSalesforce();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return Collections.emptyMap();
	}

}
