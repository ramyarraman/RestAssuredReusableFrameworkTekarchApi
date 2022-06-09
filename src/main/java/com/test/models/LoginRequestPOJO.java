package com.test.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class LoginRequestPOJO {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
		"username",
		"password"
		})
	@JsonProperty("username")
	String username;
	@JsonProperty("password")
	String password;
	public void setUsername(String userName) {
		this.username = userName;
	}
	public String getUsername() {
		return this.username;
	}
	public void setpassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return this.password;
	}
}
