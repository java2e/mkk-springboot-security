package com.example.mkksecurity.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupRequest {

	@NotBlank
	@Size(min=3,max=20)
	private String username;

	@NotBlank
	@Size(max=50)
	@Email
	private String email;

	@NotBlank
	@Size(min=6,max=15)
	private String password;
}
