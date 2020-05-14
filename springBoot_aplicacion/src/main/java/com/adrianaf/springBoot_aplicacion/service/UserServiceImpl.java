package com.adrianaf.springBoot_aplicacion.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.adrianaf.springBoot_aplicacion.Exceptions.CustomFieldValidationException;
import com.adrianaf.springBoot_aplicacion.Exceptions.UsernameOrIdNotFound;
import com.adrianaf.springBoot_aplicacion.dto.ChangePasswordForm;
import com.adrianaf.springBoot_aplicacion.entity.User;
import com.adrianaf.springBoot_aplicacion.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@SuppressWarnings("rawtypes")
	public Iterable getAllUsers() {
		return userRepository.findAll();
	}

	private Boolean checkUsernameAvaliable(User user) throws Exception{
		Optional<?> userFound = userRepository.findByusername(user.getUsername());
		if(userFound.isPresent()) {
			throw new CustomFieldValidationException("username inválido", "username");
		}
		return true;
	}
	
	private Boolean checkPasswordValid(User user) throws Exception{
		if(user.getConfirmPassword() == null || user.getConfirmPassword().isEmpty()) {
			throw new CustomFieldValidationException("El campo de confirmacion de Password es obligatorio", "confirmPassword");
		}
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			throw new CustomFieldValidationException("Password y Confirmación no coinciden", "password");
		}
		return true;
	}
	
	public User createUser(User user) throws Exception{
		if(checkUsernameAvaliable(user) && checkPasswordValid(user)) {
			String encodePass = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodePass);

			user = userRepository.save(user);
		}
		return user;
	}
	
	public User getUserById(Long id) throws UsernameOrIdNotFound{
		User user = userRepository.findById(id).orElseThrow(() -> new UsernameOrIdNotFound("Id del usuario no encontrado"));
		return user;
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
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
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public void deleteUser(Long id) throws UsernameOrIdNotFound {
		User user = getUserById(id);
		userRepository.delete(user);
	}

	public boolean isLoggedUserADMIN(){
		 return loggedUserHasRole("ROLE_ADMIN");
	}

	public boolean loggedUserHasRole(String role) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUser = null;
		Object roles = null; 
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		
			roles = loggedUser.getAuthorities().stream()
					.filter(x -> role.equals(x.getAuthority() ))      
					.findFirst().orElse(null); //loggedUser = null;
		}
		return roles != null ?true :false;
	}
	
	private User getLoggedUser() throws Exception {
		//Obtener el usuario logeado
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserDetails loggedUser = null;

		//Verificar que ese objeto traido de sesion es el usuario
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		}
		
		User myUser = userRepository
				.findByusername(loggedUser.getUsername()).orElseThrow(() -> new Exception(""));
		
		return myUser;
	}


	@Override
	public User changePassword(ChangePasswordForm form) throws Exception {
		User user = userRepository.findById(form.getId()) .orElseThrow(() -> new Exception("Usuario no encontrado en ChangePassword - " + this.getClass().getName()));
		
		if ( !isLoggedUserADMIN() && !bCryptPasswordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
			throw new Exception ("Current Password invalido.");
		}
		
		if(bCryptPasswordEncoder.matches(form.getNewPassword(), user.getPassword())) {
			throw new Exception("La nueva Password no debe ser igual a la anterior");
		}
		
		if(!form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception("La password nueva y su confirmacion no coinciden!");
		}
		
		String encodePass = bCryptPasswordEncoder.encode(form.getNewPassword());
		user.setPassword(encodePass);
		
		return userRepository.save(user);
	}
}
