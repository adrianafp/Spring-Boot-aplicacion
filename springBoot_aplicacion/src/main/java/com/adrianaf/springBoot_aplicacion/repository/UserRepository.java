package com.adrianaf.springBoot_aplicacion.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.adrianaf.springBoot_aplicacion.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	public Optional<User> findByusername(String username);
	
	public Optional<User> findByIdAndPassword(Long id, String password);
	
}
