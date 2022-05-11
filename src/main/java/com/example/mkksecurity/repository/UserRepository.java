package com.example.mkksecurity.repository;

import com.example.mkksecurity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	// Select * from User where username="?" and password

	Optional<User> findByUsername(String userName);

	Boolean existsByUsername(String userName);

	Boolean existsByEmail(String email);

}
