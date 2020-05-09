package com.adrianaf.springBoot_aplicacion.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adrianaf.springBoot_aplicacion.entity.User;
import com.adrianaf.springBoot_aplicacion.repository.UserRepository;

@SuppressWarnings("unused")
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	
	@SuppressWarnings("rawtypes")
	public Iterable getAllUsers() {
		return userRepository.findAll();
	}

	private Boolean checkUsernameAvaliable(User user) throws Exception{
		Optional<?> userFound = userRepository.findByusername(user.getUsername());
		if(userFound.isPresent()) {
			throw new Exception("username inválido");
		}
		return true;
	}
	
	private Boolean checkPasswordValid(User user) throws Exception{
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			throw new Exception("Password y Confirmación no coinciden");
		}
		return true;
	}
	
	public User createUser(User user) throws Exception{
		if(checkUsernameAvaliable(user) && checkPasswordValid(user)) {
			user = userRepository.save(user);
		}
		return user;
	}
}
