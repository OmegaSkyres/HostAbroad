package com.business;

public class TUser {
	private String nickname;
	private double rating;
	private String description;
	private boolean host;
	private String email;
	private String password;
	
	public TUser() {};
	
	public TUser(String nickname, double rating, String description, boolean host, String email, String password){
		this.nickname = nickname;
		this.rating = rating;
		this.description = description;
		this.host = host;
		this.email = email;
		this.password = password;
	}
	
	public TUser(String nickname, double rating, String description){
		this.nickname = nickname;
		this.rating = rating;
		this.description = description;
	}

	public TUser(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getNickname() {
		return this.nickname;
	}
	
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public double getRating() {
		return this.rating;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setHost(boolean host) {
		this.host = host;
	}
	
	public boolean getHost() {
		return this.host;
	}

	public void setEmail(String email) {this.email = email;}

	public String getEmail() { return this.email;}

	public void setPassword(String password) {this.password = password;}

	public String getPassword() {return this.password;}
}

