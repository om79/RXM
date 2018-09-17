/*
 * Copyright (C) 2013 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package viroopa.com.medikart.buying.model;


public class addressModel {


	public String getAdressHeadin() {
		return adressHeadin;
	}

	public void setAdressHeadin(String adressHeadin) {
		this.adressHeadin = adressHeadin;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	private String adressHeadin;
	private String Address;

	public String getLandMark() {
		return LandMark;
	}

	public void setLandMark(String landMark) {
		LandMark = landMark;
	}

	private String LandMark;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	private String Name;
	private  String pincode;
	private  String city;
	private  String State;
	private  String phone_number;

	public String getDistrict_id() {
		return District_id;
	}

	public void setDistrict_id(String district_id) {
		District_id = district_id;
	}

	private  String District_id;

	public String getAdressid() {
		return Adressid;
	}

	public void setAdressid(String adressid) {
		Adressid = adressid;
	}

	private String Adressid;



}
