package com.example.mkksecurity.repository;

import com.example.mkksecurity.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

	Optional<RefreshToken> findById(Long id);

	Optional<RefreshToken> findByToken(String token);

}
