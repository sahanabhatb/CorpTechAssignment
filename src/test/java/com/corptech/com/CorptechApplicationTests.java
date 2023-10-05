package com.corptech.com;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.corptech.com.model.Customer;
import com.corptech.com.service.CustomerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.junit.jupiter.api.BeforeEach;

@RunWith(SpringRunner.class)
@SpringBootTest
class CorptechApplicationTests {	


	String loginRequestBody = "{\"username\":\"sahana\", \"password\":\"1234\"}";
	String jwt_token;
	String customer_id;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

	@MockBean
	private CustomerService customerService;
    
    private static ObjectMapper mapper = new ObjectMapper();
	
    @BeforeEach
    public void setup() throws Exception {
      this.mockMvc =  MockMvcBuilders.webAppContextSetup(webApplicationContext)
		        .apply(springSecurity()) 
		        .build();
      this.jwt_token=getJWTToken();
    }
    
	@Test
    public void createCustomerTest() throws Exception{
		System.out.println("createCustomerTest");

		Customer customer = new Customer();
		customer.setName("Sahana");
        customer.setEmail("sahana@gmail.com");
        customer.setPhone("9797878787");

        Optional<Customer> optionalCustomer = Optional.of(customer);
        Mockito.when(customerService.creatCustomer(Mockito.any(Customer.class))).thenReturn(optionalCustomer);
        
        String exampleJson = "{\"name\":\"Sahana\",\"email\":\"sahana@gmail.com\",\"phone\":\"9797878787\"}";
        
     // Send course as body to /students/Student1/courses
 		RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/api/customers")
 				.accept(MediaType.APPLICATION_JSON).content(exampleJson)
 				.contentType(MediaType.APPLICATION_JSON)
 				.header(HttpHeaders.AUTHORIZATION, "Bearer "+this.jwt_token);
 		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
 		
		MockHttpServletResponse response = result.getResponse();
		
		String responseBody = response.getContentAsString();

		// Parse the JSON response body into a JSON object
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);

		assertEquals(HttpStatus.OK.value(), response.getStatus());	
		System.out.println(jsonNode);
		//check if passed name is same as the returned name , and similarly checking for
		//phone and email
		this.customer_id=jsonNode.get("id").asText();
		assertEquals("Sahana",jsonNode.get("name").asText());
		assertEquals("sahana@gmail.com",jsonNode.get("email").asText());
		assertEquals("9797878787",jsonNode.get("phone").asText());
    }

	 public String getJWTToken() throws Exception {
		 
		 MvcResult loginResult = mockMvc.perform(
		         MockMvcRequestBuilders.post("/authenticate") // Replace with your login endpoint URL
		             .contentType(MediaType.APPLICATION_JSON)
		             .content(loginRequestBody)
		     )
		     .andExpect(MockMvcResultMatchers.status().isOk()) // Adjust the expected status code as needed
		     .andReturn();
		 
		// Extract the response body as a string
		 String responseBody = loginResult.getResponse().getContentAsString();

		 // Parse the JSON response body to extract the token
		 ObjectMapper objectMapper = new ObjectMapper();
		 JsonNode jsonNode = objectMapper.readTree(responseBody);

		 // Get the token from the JSON response
		 String token = jsonNode.get("token").asText();
		 
		 return token;
		 
	 }
	    
}
