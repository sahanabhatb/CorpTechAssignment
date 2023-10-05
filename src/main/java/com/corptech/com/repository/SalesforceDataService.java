package com.corptech.com.repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.corptech.com.config.Constants;
import com.corptech.com.config.SalesforceAuthenticator;
import com.corptech.com.model.Customer;

@Service
public interface SalesforceDataService  extends JpaRepository<Customer, String>{

	public default Map getSalesforceData(String query) {
		SalesforceAuthenticator salesforceAuthenticator = SalesforceAuthenticator.getSalesforceToken();
		try {
			RestTemplate restTemplate = new RestTemplate();
			String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
			final String baseUrl = salesforceAuthenticator.instanceUrl + Constants.SALESFORCE_QUERY_APPENDER
					+ encodedQuery;
			URI uri = new URI(baseUrl);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", salesforceAuthenticator.accessToken));
			HttpEntity<?> request = new HttpEntity<Object>(headers);
			ResponseEntity<Map> response = null;
			try {
				response = restTemplate.exchange(uri, HttpMethod.GET, request, Map.class);
			} catch (HttpClientErrorException e) {
				System.out.println(e.getMessage());
			}
			return response.getBody();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return Collections.emptyMap();
	}
	public default void createDataForSingleCustomer() {
		SalesforceAuthenticator salesforceAuthenticator = SalesforceAuthenticator.getSalesforceToken();
		try {
			RestTemplate restTemplate = new RestTemplate();
			final String baseUrl = salesforceAuthenticator.instanceUrl + "/services/data/v52.0/sobjects/CorpCustomer__c";
			URI uri = new URI(baseUrl);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", salesforceAuthenticator.accessToken));
			Map<String, Object> customerData = new HashMap<>();
			customerData.put("Name", "SampleCustomer");
			//productData.put("Description", "Description of Customer");
			//productData.put("Price__c", 100.0);

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(customerData, headers);

			ResponseEntity<String> response = null;
			
			try {
				response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
			} catch (HttpClientErrorException e) {
				System.out.println(e.getMessage());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public default void createDataForCustomers(List<Customer> foundCustomers) {
		SalesforceAuthenticator salesforceAuthenticator = SalesforceAuthenticator.getSalesforceToken();
		try {
			RestTemplate restTemplate = new RestTemplate();
			final String baseUrl = salesforceAuthenticator.instanceUrl + "/services/data/v52.0/composite/tree/CorpCustomer__c";
			URI uri = new URI(baseUrl);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", salesforceAuthenticator.accessToken));
			// Create the outer JSON object
	        Map<String, Object> customerData = new HashMap<>();

	        // Create the "records" array
	        List<Map<String, Object>> records = new ArrayList<>();
	        
	        for (Customer customer : foundCustomers) {
	        	
	        	Map<String, Object> attributes = new HashMap<>();
		        attributes.put("type", "CorpCustomer__c");
		        attributes.put("referenceId", customer.getId());
		        
		        Map<String, Object> record = new HashMap<>();
		        record.put("attributes", attributes);
		        record.put("Name", customer.getName());
		        record.put("Email__c", customer.getEmail());
		        record.put("Phone__c", customer.getPhone());
		        record.put("Customer_id__c", customer.getId());
		        records.add(record);
	        	
	        }

	        // Add the "records" array to the outer JSON object
	        customerData.put("records", records);
	        
			HttpEntity<Map<String, Object>> request = new HttpEntity<>(customerData, headers);

			ResponseEntity<String> response = null;
			
			try {
				response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
			} catch (HttpClientErrorException e) {
				System.out.println(e.getMessage());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public default void deleteCustomerdata(String recordId) {
		SalesforceAuthenticator salesforceAuthenticator = SalesforceAuthenticator.getSalesforceToken();
		try {
			RestTemplate restTemplate = new RestTemplate();
			final String baseUrl = salesforceAuthenticator.instanceUrl + "/services/data/v52.0/sobjects/CorpCustomer__c/"+ recordId;
			URI uri = new URI(baseUrl);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", salesforceAuthenticator.accessToken));

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);

			ResponseEntity<String> response = null;
			
			try {
				response = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
			} catch (HttpClientErrorException e) {
				System.out.println(e.getMessage());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
