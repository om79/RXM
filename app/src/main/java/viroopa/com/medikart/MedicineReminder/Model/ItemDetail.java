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

package viroopa.com.medikart.MedicineReminder.Model;

import java.io.Serializable;

public class ItemDetail implements Serializable {
	
	private long id;
	
	private String Scheduled_Start_Date;



	private String Scheduled_End_Date;
	private String Medicine_name;

	public String getImage_id() {
		return image_id;
	}

	public void setImage_id(String image_id) {
		this.image_id = image_id;
	}

	public String getImg_array_name() {
		return img_array_name;
	}

	public void setImg_array_name(String img_array_name) {
		this.img_array_name = img_array_name;
	}

	private String image_id;
	private String img_array_name;

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	private String OrderId;

	public String getMedicine_name() {
		return Medicine_name;
	}

	public void setMedicine_name(String Medicine_name) {
		this.Medicine_name = Medicine_name;
	}


	
	
	
	public ItemDetail(long id, String Scheduled_Start_Date, String Scheduled_End_Date, String Medicine_name, String OrderId) {

		this.id=id;
		this.Scheduled_Start_Date = Scheduled_Start_Date;
		this.Scheduled_End_Date = Scheduled_End_Date;
		this.Medicine_name=Medicine_name;
		this.OrderId=OrderId;
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getScheduled_Start_Date() {
		return Scheduled_Start_Date;
	}
	public void setScheduled_Start_Date(String Scheduled_Start_Date) {
		this.Scheduled_Start_Date = Scheduled_Start_Date;
	}
	public String getScheduled_End_Date() {
		return Scheduled_End_Date;
	}
	public void setScheduled_End_Date(String Scheduled_End_Date) {
		this.Scheduled_End_Date = Scheduled_End_Date;
	}
	
	
}
