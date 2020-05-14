package com.adrianaf.springBoot_aplicacion.service;


import com.adrianaf.springBoot_aplicacion.Exceptions.UsernameOrIdNotFound;
import com.adrianaf.springBoot_aplicacion.dto.ChangePasswordForm;
import com.adrianaf.springBoot_aplicacion.entity.User;


public interface UserService {

	@SuppressWarnings("rawtypes")
	public Iterable getAllUsers();
	
	public User getUserById(Long id) throws UsernameOrIdNotFound;
	
	public User createUser(User formUser) throws Exception;

	public User updateUser(User user) throws Exception;

	public void deleteUser(Long id) throws UsernameOrIdNotFound;

	public User changePassword(ChangePasswordForm form) throws Exception;
}
