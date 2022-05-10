package com.example.mkksecurity.controller;

import com.example.mkksecurity.models.ERole;
import com.example.mkksecurity.models.Role;
import com.example.mkksecurity.models.User;
import com.example.mkksecurity.payload.request.LoginRequest;
import com.example.mkksecurity.payload.request.SignupRequest;
import com.example.mkksecurity.payload.response.JwtResponse;
import com.example.mkksecurity.payload.response.MessageResponse;
import com.example.mkksecurity.repository.RoleRepository;
import com.example.mkksecurity.repository.UserRepository;
import com.example.mkksecurity.security.JwtUtils;
import com.example.mkksecurity.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;


	@PostMapping("/signin")
	public ResponseEntity<?> signIn(LoginRequest request) {

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(),request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		List<String> roles = userDetails.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getId(),userDetails.getUsername(),userDetails.getUsername(),roles));


	}


	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest request) {

		if(userRepository.existsByUserName(request.getUserName())) {

			return ResponseEntity.badRequest().body(new MessageResponse("Error : username is already taken!"));

		}

		if(userRepository.existsByEmail(request.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error : email is already is use!"));
		}

		User user = new User(request.getUserName(),request.getEmail(),passwordEncoder.encode(request.getPassword()));

		Set<String> strRoles = new HashSet<>();
		strRoles.add("ROLE_ADMIN");

		Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(()-> new RuntimeException("Error: Role is not found!"));

		roles.add(userRole);

		user.setRoles(roles);

		userRepository.save(user);


		return ResponseEntity.ok(new MessageResponse("user registered successfully!"));

	}

}
