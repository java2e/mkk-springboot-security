package com.example.mkksecurity.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {

	private String token;
	private String type="Bearer";
	private String refreshToken;
	private Long id;
	private String userName;
	private String email;
	private List<String> roles;

	public JwtResponse(String accesToken,String refreshToken,Long id,String userName,String email,List<String> roles){
		this.token = accesToken;
		this.refreshToken = refreshToken;
		this.id =id;
		this.userName = userName;
		this.email = email;
		this.roles =roles;
	}

}
