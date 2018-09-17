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
import java.util.ArrayList;
import java.util.List;


public class Category implements Serializable {
	
	private long id;
	private String Scheduled_med_date;
	private String Taken;

	public String getPending() {
		return Pending;
	}

	public void setPending(String pending) {
		Pending = pending;
	}

	private String Pending;
	
	private List<ItemDetail> itemList = new ArrayList<ItemDetail>();

	public Category(long id, String Scheduled_med_date, String Taken, String Pending ) {
		this.id = id;
		this.Scheduled_med_date = Scheduled_med_date;
		this.Taken=Taken;
		this.Pending=Pending;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getScheduled_med_date() {
		return Scheduled_med_date;
	}

	public void setScheduled_med_date(String Scheduled_med_date) {
		this.Scheduled_med_date = Scheduled_med_date;
	}

	public String getTaken() {
		return Taken;
	}

	public void setTaken(String Taken) {
		this.Taken = Taken;
	}

	public List<ItemDetail> getItemList() {
		return itemList;
	}

	public void setItemList(List<ItemDetail> itemList) {
		this.itemList = itemList;
	}
	
	

}
