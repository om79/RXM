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

package viroopa.com.medikart.bpmonitor.Module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BPM_Analysis_Heading implements Serializable {
	
	private long id;
	private String HeadingName;
	private String Min;

	public String getMax() {
		return Max;
	}

	public void setMax(String max) {
		Max = max;
	}

	public String getAvg() {
		return Avg;
	}

	public void setAvg(String avg) {
		Avg = avg;
	}

	private String Max;
	private String Avg;

	public String getWeight_unit() {
		return Weight_unit;
	}

	public void setWeight_unit(String weight_unit) {
		Weight_unit = weight_unit;
	}

	private String Weight_unit;


	
	private List<BPM_Analysis_Item_Detail> itemList = new ArrayList<BPM_Analysis_Item_Detail>();

	public BPM_Analysis_Heading(long id, String HeadingName, String Min, String Max,String Avg) {
		this.id = id;
		this.HeadingName = HeadingName;
		this.Min = Min;
		this.Max = Max;
		this.Avg = Avg;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHeadingName() {
		return HeadingName;
	}

	public void setHeadingName(String HeadingName) {
		this.HeadingName = HeadingName;
	}

	public String getMin() {
		return Min;
	}

	public void setMin(String Min) {
		this.Min = Min;
	}

	public List<BPM_Analysis_Item_Detail> getItemList() {
		return itemList;
	}

	public void setItemList(List<BPM_Analysis_Item_Detail> itemList) {
		this.itemList = itemList;
	}

}
