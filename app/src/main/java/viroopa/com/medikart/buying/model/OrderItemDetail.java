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

import java.io.Serializable;

public class OrderItemDetail implements Serializable {
	
	private long id;

	private String tracking_date;
	private String tracking_description;

	
	
	
	
	public OrderItemDetail(long id, String tracking_date, String tracking_description) {

		this.id=id;
		this.tracking_date = tracking_date;
		this.tracking_description = tracking_description;
	
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String gettracking_date() {
		return tracking_date;
	}
	public void settracking_date(String tracking_date) {
		this.tracking_date = tracking_date;
	}
	public String gettracking_description() {
		return tracking_description;
	}
	public void settracking_description(String tracking_description) {
		this.tracking_description = tracking_description;
	}
	
	
}
