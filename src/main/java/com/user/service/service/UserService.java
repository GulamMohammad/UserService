package com.user.service.service;

import java.util.List;

import com.user.service.entity.User;

public interface UserService {
	
	//create new User
	public User createUser(User user);
	
	//get Single User based on userId
	public User getSingleUser(String userId);
	
	//get all Users
	public List<User> getAllUsers();

}
