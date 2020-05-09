package com.adrianaf.springBoot_aplicacion.service;

import com.adrianaf.springBoot_aplicacion.entity.User;


public interface UserService {

	@SuppressWarnings("rawtypes")
	public Iterable getAllUsers();
	
	public User createUser(User formUser) throws Exception;
}
