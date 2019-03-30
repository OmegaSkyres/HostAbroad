package com.business;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table
public class Traveler{

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private int id; 
	@Version
	private int version;
	@OneToOne 
	private User user;
	private ArrayList<CountriesEnum> listOfCountries;
	private ArrayList<KnowledgesEnum> listOfKnowledges;
	private DurationOfStayEnum durationOfStay;

	public Traveler() {};

	public Traveler(int id, int version, User user, ArrayList<CountriesEnum> listOfCountries, 
			ArrayList<KnowledgesEnum> listOfKnowledges, DurationOfStayEnum durationOfStay) {
		this.id = id;
		this.version = version;
		this.user = user;
		this.listOfCountries = listOfCountries;
		this.listOfKnowledges = listOfKnowledges;
		this.durationOfStay = durationOfStay;
	}

	public Traveler(int id, User user, ArrayList<CountriesEnum> listOfCountries, 
			ArrayList<KnowledgesEnum> listOfKnowledges, DurationOfStayEnum durationOfStay) {
		this.id = id;
		this.user = user;
		this.listOfCountries = listOfCountries;
		this.listOfKnowledges = listOfKnowledges;
		this.durationOfStay = durationOfStay;
	}
	
	public Traveler(User user, ArrayList<CountriesEnum> listOfCountries, 
			ArrayList<KnowledgesEnum> listOfKnowledges, DurationOfStayEnum durationOfStay) {
		this.user = user;
		this.listOfCountries = listOfCountries;
		this.listOfKnowledges = listOfKnowledges;
		this.durationOfStay = durationOfStay;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setListOfCountries(ArrayList<CountriesEnum> listOfCountries) {
		this.listOfCountries = listOfCountries;
	}

	public ArrayList<CountriesEnum> getListOfCountries() {
		return this.listOfCountries;
	}

	public void setListOfKnowledges(ArrayList<KnowledgesEnum> listOfKnowledges) {
		this.listOfKnowledges = listOfKnowledges;
	}

	public ArrayList<KnowledgesEnum> getListOfKnowledges() {
		return this.listOfKnowledges;
	}

	public void setDurationOfStay(DurationOfStayEnum durationOfStay) {
		this.durationOfStay = durationOfStay;
	}

	public DurationOfStayEnum getDurationOfStay() {
		return this.durationOfStay;
	}
}