package com.corptech.com.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corptech.com.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	 public Optional<User> findById(Integer id);
	 public User findByUsername(String username);
	 public List<User> findByEmailid(String emailid);

}
