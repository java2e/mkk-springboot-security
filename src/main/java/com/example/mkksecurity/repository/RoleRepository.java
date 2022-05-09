package com.example.mkksecurity.repository;

import com.example.mkksecurity.models.ERole;
import com.example.mkksecurity.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

	Optional<Role> findByName(ERole name);

}
