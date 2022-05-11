package com.example.mkksecurity.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.mkksecurity.aop.ExecutionTime;
import com.example.mkksecurity.models.ERole;
import com.example.mkksecurity.models.RefreshToken;
import com.example.mkksecurity.models.Role;
import com.example.mkksecurity.models.User;
import com.example.mkksecurity.payload.request.LoginRequest;
import com.example.mkksecurity.payload.request.SignupRequest;
import com.example.mkksecurity.payload.request.TokenRefreshRequest;
import com.example.mkksecurity.payload.response.JwtResponse;
import com.example.mkksecurity.payload.response.MessageResponse;
import com.example.mkksecurity.payload.response.TokenRefreshResponse;
import com.example.mkksecurity.repository.RoleRepository;
import com.example.mkksecurity.repository.UserRepository;
import com.example.mkksecurity.security.JwtUtils;
import com.example.mkksecurity.service.RefreshTokenService;
import com.example.mkksecurity.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		RefreshToken refreshToken =refreshTokenService.createRefreshToken(userDetails.getId());

		return ResponseEntity.ok(new JwtResponse(jwt,refreshToken.getToken(),
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail(),
				roles));
	}

	@PostMapping("/signup")
	@ExecutionTime
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(),
				signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));


		Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@ExecutionTime
	@PostMapping("/refreshToken")
	public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {

		String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

		RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken);

		if(refreshToken != null && refreshTokenService.verifyExpritation(refreshToken) !=null) {

			String token = jwtUtils.generateJwtTokenWithUsername(refreshToken.getUser().getUsername());
			return ResponseEntity.ok(new TokenRefreshResponse(token,requestRefreshToken));

		}
		else {
			return ResponseEntity.badRequest().body("Refresh token is not in database!");
		}

	}
}
