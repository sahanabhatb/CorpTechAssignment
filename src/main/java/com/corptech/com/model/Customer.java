package com.corptech.com.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {
	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
    private String id;
    private String name;
    private String email;
    private String phone;

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}


	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int idx = -1;
        if (id != null) {
            sb.append(++idx > 0 ? ", " : "").append("id=").append(id);
        }
        if (name != null) {
            sb.append(++idx > 0 ? ", " : "").append("code=").append(name);
        }
        if (email != null) {
            sb.append(++idx > 0 ? ", " : "").append("name=").append(email);
        }
        if (phone != null) {
            sb.append(++idx > 0 ? ", " : "").append("category=").append(phone);
        }
        return sb.toString();
    }
	
 
}
