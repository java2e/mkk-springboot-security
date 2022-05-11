package com.example.mkksecurity.aop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
public class ErrorMessage {

	private HttpStatus httpStatus;

	private Date date;

	String message;

	String description;

	public ErrorMessage(HttpStatus status,Date date,String message,String description) {
		this.httpStatus = status;
		this.date = date;
		this.message = message;
		this.description = description;
	}
}
