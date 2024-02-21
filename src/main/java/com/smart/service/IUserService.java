package com.smart.service;

import com.smart.model.User;

public interface IUserService {
	
	public User saveUser(User user);

	public User getData(String userName);

    Boolean changePassword(String oldpwd,String userpwd, String newpwd, User user);
}
