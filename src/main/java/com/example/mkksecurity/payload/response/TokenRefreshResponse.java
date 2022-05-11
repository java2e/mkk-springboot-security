package com.example.mkksecurity.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshResponse {

	private String refreshToken;
	private String accessToken;
	private String tokenType="Bearer";

	public TokenRefreshResponse(String refreshToken,String accessToken){
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

}
