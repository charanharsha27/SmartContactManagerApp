package com.smart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smart.dao.IUserDao;
import com.smart.model.User;

@Service
public class IUserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public User saveUser(User user) {
		return userDao.save(user);
	}

	@Override
	public User getData(String email) {
		return userDao.getUserByUserName(email);
	}

	@Override
	public Boolean changePassword(String oldpwd, String userpwd,String newpwd,User user) {

		if(passwordEncoder.matches(oldpwd,userpwd))
		{
			System.out.println("inside");
			user.setPassword(passwordEncoder.encode(newpwd));
			userDao.save(user);
			return true;
		}
		return false;
	}

}
