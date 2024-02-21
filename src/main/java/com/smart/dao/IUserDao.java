package com.smart.dao;

import com.smart.model.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smart.model.User;

import java.util.List;

public interface IUserDao extends JpaRepository<User, Integer>{
	
	@Query(value="select * from user u where u.email=:email",nativeQuery=true)
	public User getUserByUserName(String email);

}
