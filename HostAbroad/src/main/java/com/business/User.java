package com.business;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class User {
	
	@Id
	private String nickname;
	private double rating;
	private String description;
	private boolean host;
	private boolean traveler;
	
	public User() {};
	
	public User(String nickname, double rating, String description, boolean host, boolean traveler){
		this.nickname = nickname;
		this.rating = rating;
		this.description = description;
		this.host = host;
		this.traveler = traveler;
	}
	
	public User(String nickname, double rating, String description){
		this.nickname = nickname;
		this.rating = rating;
		this.description = description;
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

	public boolean getTraveler() {
		return traveler;
	}

	public void setTraveler(boolean traveler) {
		this.traveler = traveler;
	}
}
