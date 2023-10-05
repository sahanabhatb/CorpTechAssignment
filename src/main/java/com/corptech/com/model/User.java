package com.corptech.com.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
	    private Integer id;
	    private String name;   
	    private String emailid;
	    private String username;
	    @JsonIgnore
	    private String password;
	    private String role;
	    
	    @Column(name = "jwt_token")
	    private String jwt_token;
	    
		public String getJwt_token() {
			return jwt_token;
		}
		public void setJwt_token(String jwt_token) {
			this.jwt_token = jwt_token;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmailid() {
			return emailid;
		}
		public void setEmailid(String emailid) {
			this.emailid = emailid;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
	    
	    
}
