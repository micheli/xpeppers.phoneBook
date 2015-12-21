/*
This file is part of xpeppers.phoneBook

   Copyright 2015 Micheli Luca

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.xpeppers.phonebook.model;

import java.io.Serializable;

public class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4902062694160973038L;

	// private variables
	int _id;
	String _name;
	String _surname;
	String _phone_number;

	// Empty constructor
	public Contact() {

	}

	// constructor
	public Contact(int id, String name, String surname, String _phone_number) {
		this._id = id;
		this._name = name;
		this._surname = surname;
		this._phone_number = _phone_number;
	}

	// constructor
	public Contact(String name, String surname, String _phone_number) {
		this._name = name;
		this._surname = surname;
		this._phone_number = _phone_number;
	}

	// getting ID
	public int getID() {
		return this._id;
	}

	// setting id
	public void setID(int id) {
		this._id = id;
	}

	// getting name
	public String getName() {
		return this._name;
	}

	// setting name
	public void setName(String name) {
		this._name = name;
	}

	// getting surname
	public String getSurname() {
		return this._surname;
	}

	// setting surname
	public void setSurname(String surname) {
		this._surname = surname;
	}

	// getting phone number
	public String getPhoneNumber() {
		return this._phone_number;
	}

	// setting phone number
	public void setPhoneNumber(String phone_number) {
		this._phone_number = phone_number;
	}
}
