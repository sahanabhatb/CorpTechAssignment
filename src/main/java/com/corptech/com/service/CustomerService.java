package com.corptech.com.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.corptech.com.model.Customer;
import com.corptech.com.repository.CustomerRepository;
import com.corptech.com.repository.SalesforceDataService;

@Repository
@Transactional
public class CustomerService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private SalesforceDataService salesforceDataService;
	
	public String insert(Customer customer) {
		entityManager.persist(customer);
		return customer.getId();
	}
	public Customer find(String id) {
		return entityManager.find(Customer.class, id);
	}
	
	public Map synchCustomersToSalesforce() {
		String query = "SELECT Name,Id,Customer_id__c,Email__c,Phone__c FROM CorpCustomer__c";
		
		//delete all salesforce data before synching
		Map<String, Object>  jsonResponse = salesforceDataService.getSalesforceData(query);
		List<Map<String, Object>> records = (List<Map<String, Object>>) jsonResponse.get("records");

		for (Map<String, Object> record : records) {
            String recordId = (String) record.get("Id");
            salesforceDataService.deleteCustomerdata(recordId);
        }
		
		//get all mysql db data 
		List<Customer> foundCustomers = customerRepository.findAll();
		
		//create all mysql db data to salesforece
		salesforceDataService.createDataForCustomers(foundCustomers);
		
		return salesforceDataService.getSalesforceData(query);
	}
	
	public Map<String, Boolean> deleteCustomer(String Id) {

		try {
			Customer customerToBeDelete = customerRepository.getById(Id);
			customerRepository.delete(customerToBeDelete);
			Map<String, Boolean> response = new HashMap<>();
			response.put("deleted", Boolean.TRUE);
			return response;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public Optional<Customer> editCustomer( String Id, Customer customer) {

		Customer customerToBeEdited = customerRepository.getById(Id);
		if (customerToBeEdited!=null) {
			// Customer found, return it
			customerToBeEdited.setName(customer.getName());
			customerToBeEdited.setEmail(customer.getEmail());
			customerToBeEdited.setPhone(customer.getPhone());
			customerRepository.save(customerToBeEdited);
			Optional<Customer> editedCustomer = customerRepository.findById(Id);
			return editedCustomer;
		} else {
			// Customer not found, 
			return null;
		}		
	}
	
	public Optional<Customer> creatCustomer(Customer customer) {
			customerRepository.save(customer);
			Optional<Customer> savedProduct = customerRepository.findById(customer.getId());
			return savedProduct;
	}
	
	public ResponseEntity<Object> customerByCustId(String Id) {

		try {
			
			Optional<Customer> foundCustomer = customerRepository.findById(Id);
			if (foundCustomer.isPresent()) {
				// Customer found, return it
				return ResponseEntity.ok().body(foundCustomer);
			} else {
				// Customer not found, return a JSON response
				Map<String, String> response = new HashMap<>();
				response.put("message", "No data found for the ID provided");
				response.put("status", "fail");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public ResponseEntity<Object> getAllCustomer() {

		try {

			List<Customer> foundCustomer = customerRepository.findAll();
			if (!foundCustomer.isEmpty()) {
				// Customer found, return it
				return ResponseEntity.ok().body(foundCustomer);
			} else {
				Map<String, String> response = new HashMap<>();
				response.put("message", "No data found ");
				response.put("status", "fail");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

}
