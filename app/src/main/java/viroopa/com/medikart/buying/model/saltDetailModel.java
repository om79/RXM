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


public class saltDetailModel {

	public String Name;
	public  String price;
	public String type;
	public String total_count;

	public String getPr_id() {
		return pr_id;
	}

	public void setPr_id(String pr_id) {
		this.pr_id = pr_id;
	}

	public String pr_id;

	public String getCount_combination() {
		return count_combination;
	}

	public void setCount_combination(String count_combination) {
		this.count_combination = count_combination;
	}

	public String count_combination;

	public String gettotal_count() {
		return total_count;
	}

	public void settotal_count(String total_count) {
		this.total_count = total_count;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}


	public String getprice() {
		return price;
	}

	public void setprice(String price) {
		this.price = price;
	}

	public String gettype() {
		return type;
	}

	public void settype(String type) {
		this.type = type;
	}


}
