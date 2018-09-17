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

public class ItemDetail implements Serializable {
	
	private long id;
	private int imgId;
	private String ref_OrderDate;
	private String order_no;
	private String order_price;

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	private String OrderId;

	public String getOrder_price() {
		return order_price;
	}

	public void setOrder_price(String order_price) {
		this.order_price = order_price;
	}


	
	
	
	public ItemDetail(long id, String ref_OrderDate, String order_no, String order_price, String OrderId) {

		this.id=id;
		this.ref_OrderDate = ref_OrderDate;
		this.order_no = order_no;
		this.order_price=order_price;
		this.OrderId=OrderId;
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	public String getref_OrderDate() {
		return ref_OrderDate;
	}
	public void setref_OrderDate(String ref_OrderDate) {
		this.ref_OrderDate = ref_OrderDate;
	}
	public String getorder_no() {
		return order_no;
	}
	public void setorder_no(String order_no) {
		this.order_no = order_no;
	}
	
	
}
