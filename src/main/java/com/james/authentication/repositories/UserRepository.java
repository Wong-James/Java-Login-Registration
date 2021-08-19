package com.james.authentication.repositories;


import com.james.authentication.models.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByEmail(String email);
}
