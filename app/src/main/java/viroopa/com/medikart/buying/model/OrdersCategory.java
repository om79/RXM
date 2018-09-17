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
import java.util.ArrayList;
import java.util.List;


public class OrdersCategory implements Serializable {

	private long id;
	private String OrderId;
	private String ProductId;
	private String Product;
	private String Price;
	private String Discount;
	private String Quantity;
	private String Packsize;
	private String Status;
	private Integer StatusCode;
	private String imageUrl;




	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getProductId() {
		return ProductId;
	}

	public void setProductId(String productId) {
		ProductId = productId;
	}

	public String getProduct() {
		return Product;
	}

	public void setProduct(String product) {
		Product = product;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getDiscount() {
		return Discount;
	}

	public void setDiscount(String discount) {
		Discount = discount;
	}

	public String getQuantity() {
		return Quantity;
	}

	public void setQuantity(String quantity) {
		Quantity = quantity;
	}

	public String getPacksize() {
		return Packsize;
	}

	public void setPacksize(String packsize) {
		Packsize = packsize;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public Integer getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(Integer statusCode) {
		StatusCode = statusCode;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	private List<OrderItemDetail> itemList = new ArrayList<OrderItemDetail>();

	public OrdersCategory(long id, String name, String descr, String OrderId, String ProductId, String Product, String Price, String Discount, String Quantity, String Packsize, String Status, Integer StatusCode, String imageUrl) {
		this.id = id;
		this.OrderId=OrderId;
		this.ProductId=ProductId;
		this.Product=Product;
		this.Price=Price;
		this.Discount=Discount;
		this.Quantity=Quantity;
		this.Packsize=Packsize;
		this.Status=Status;
		this.StatusCode=StatusCode;
		this.imageUrl=imageUrl;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}



	public List<OrderItemDetail> getItemList() {
		return itemList;
	}

	public void setItemList(List<OrderItemDetail> itemList) {
		this.itemList = itemList;
	}
	
	

}
