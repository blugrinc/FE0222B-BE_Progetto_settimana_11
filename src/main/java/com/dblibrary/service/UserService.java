package com.dblibrary.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.dblibrary.model.auth.Role;
import com.dblibrary.model.auth.Roles;
import com.dblibrary.model.auth.User;
import com.dblibrary.model.auth.dto.UserDTO;
import com.dblibrary.repository.RoleRepository;
import com.dblibrary.repository.UserRepository;
import com.dblibrary.util.exception.LibraryException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	public Optional<User> addUser(UserDTO newUserDTO) throws Exception {
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

		if (userRepository.findByUserName(newUserDTO.getUserName().toLowerCase()).isPresent()
				|| userRepository.existsByEmail(newUserDTO.getEmail())) {
			throw new LibraryException("username o email già esistenti");
		}

		Set<Role> roles = new HashSet<>();
		User newUser = new User();
		// ci controlliamo se sono stati specificati dei ruoli per l'utente
		if (newUserDTO.getRoles() != null && newUserDTO.getRoles().size() > 0) {
			// se specificati, ciclo e faccio un controllo
			for (String r : newUserDTO.getRoles()) {
				// se il ruolo attuale e' user
				if (r.toLowerCase().equals("user")) {
					// Controllo se è già presente nel database
					if (roleRepository.findByRoleName(Roles.ROLE_USER).isPresent()) {
						roles.add(roleRepository.findByRoleName(Roles.ROLE_USER).get());
					} else {
						// se non presente lo aggiunge
						roles.add(new Role(Roles.ROLE_USER));
					}
				}
				// se il ruolo attuale e' admin
				else if (r.toLowerCase().equals("admin")) {
					// controlla se il ruolo admin e' gia presente in database
					if (roleRepository.findByRoleName(Roles.ROLE_ADMIN).isPresent()) {
						roles.add(roleRepository.findByRoleName(Roles.ROLE_ADMIN).get());
					} else {
						// se non presente lo aggiunge
						roles.add(new Role(Roles.ROLE_ADMIN));
					}
				}
			}
		}
		if (newUserDTO.getRoles() != null && roles.size() > 0) {
		} else {
			roles.add(new Role(Roles.ROLE_USER));
		}
		newUser.setUserName(newUserDTO.getUserName().toLowerCase());
		newUser.setPassword(bCrypt.encode(newUserDTO.getPassword()));
		newUser.setEmail(newUserDTO.getEmail());
		newUser.setRoles(roles);
		newUser.setActive(true);
		userRepository.save(newUser);
		return Optional.of(newUser);
	}

}
