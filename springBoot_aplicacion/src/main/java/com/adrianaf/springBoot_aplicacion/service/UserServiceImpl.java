package com.adrianaf.springBoot_aplicacion.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adrianaf.springBoot_aplicacion.entity.User;
import com.adrianaf.springBoot_aplicacion.repository.UserRepository;

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
		if(user.getConfirmPassword() == null || user.getConfirmPassword().isEmpty()) {
			throw new Exception("El campo de confirmacion de Password es obligatorio");
		}
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
	
	public User getUserById(Long id) throws Exception{
		User user = userRepository.findById(id).orElseThrow(() -> new Exception("usuario inexistente"));
		return user;
	}

	@Override
	public User updateUser(User fromUser) throws Exception {
		User toUser = getUserById(fromUser.getId());
		mapUser(fromUser, toUser);
		
		return userRepository.save(toUser);
	}
	
	protected void mapUser(User from,User to) {
		to.setUsername(from.getUsername());
		to.setFirstname(from.getFirstname());
		to.setLastname(from.getLastname());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
	}

	@Override
	public void deleteUser(Long id) throws Exception {
		User user = userRepository.findById(id) .orElseThrow(()-> new Exception("Usuario no encontrado en deleteUser - "+ this.getClass().getName()));
		
		userRepository.delete(user);
	}
}
