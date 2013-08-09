package com.skula.iry.models;

public class Person {
	private String id;
	private String lastname;
	private String firstname;
	private String birthday;
	private String address;
	private String zip;
	private String city;
	private String fixnum;
	private String cellnum;
	private String email;
	private String group;

	public Person() {
	}

	public Person(String id, String lastname, String firstname, String birthday, String address, String zip, String city, String fixnum, String cellnum,
			String email, String group) {
		this.id = id;
		this.lastname = lastname;
		this.firstname = firstname;
		this.birthday = birthday;
		this.address = address;
		this.zip = zip;
		this.city = city;
		this.fixnum = fixnum;
		this.cellnum = cellnum;
		this.email = email;
		this.group = group;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFixnum() {
		return fixnum;
	}

	public void setFixnum(String fixnum) {
		this.fixnum = fixnum;
	}

	public String getCellnum() {
		return cellnum;
	}

	public void setCellnum(String cellnum) {
		this.cellnum = cellnum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
