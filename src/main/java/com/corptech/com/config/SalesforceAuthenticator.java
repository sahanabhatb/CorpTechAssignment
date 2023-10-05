package com.corptech.com.config;
import java.net.URI;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class SalesforceAuthenticator {
	
    private static SalesforceAuthenticator salesforceAuthenticator = null; 
    public static String accessToken;
    public static String instanceUrl;
    
    private SalesforceAuthenticator() {
    	try {
    		final String baseUrl = Constants.SALESFORCE_BASE_URL;
			URI uri = new URI(baseUrl);
HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
			params.add("username", Constants.SALESFORCE_USERNAME);
			params.add("password", Constants.SALESFORCE_PASSWORD);
			params.add("client_secret", Constants.SALESFORCE_CLIENT_SECRET);
			params.add("client_id", Constants.SALESFORCE_CLIENT_ID);
			params.add("grant_type",Constants.SALESFORCE_GRANTTYPE);
			
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(params, headers);
			
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Map> response = restTemplate.postForEntity(uri, request, Map.class);
			
			Map<String,String> responseBody = response.getBody();
	
			accessToken = responseBody.get("access_token");
			instanceUrl = responseBody.get("instance_url");
    	}catch(Exception e) {
        	System.out.println(e.getMessage()); 		
    	}
    }
 public static SalesforceAuthenticator getSalesforceToken() 
    { 
        try {
        	if (salesforceAuthenticator == null) 
	        { 
        		salesforceAuthenticator = new SalesforceAuthenticator();
	        	return salesforceAuthenticator;
	        }else {
	        	return salesforceAuthenticator;
	        }
        }catch(Exception e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
        }
        return null;
    }
}