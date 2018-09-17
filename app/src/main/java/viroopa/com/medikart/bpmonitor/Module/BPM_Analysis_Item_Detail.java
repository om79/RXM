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

public class BPM_Analysis_Item_Detail implements Serializable {

	private long id;

	private String GroupId;
	private String FilterCondition;
	private String ToDate;

	public String getToDate() {
		return ToDate;
	}

	public void setToDate(String toDate) {
		ToDate = toDate;
	}



	public String getSetting_weight_unit() {
		return setting_weight_unit;
	}

	public void setSetting_weight_unit(String setting_weight_unit) {
		this.setting_weight_unit = setting_weight_unit;
	}

	private String setting_weight_unit;

	public String getFilterAMPMCondition() {
		return FilterAMPMCondition;
	}

	public void setFilterAMPMCondition(String filterAMPMCondition) {
		FilterAMPMCondition = filterAMPMCondition;
	}

	private String FilterAMPMCondition;
	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	private String checkFlag;





	public BPM_Analysis_Item_Detail(long id, String GroupId, String FilterCondition,String
			FilterAMPMCondition,String setting_weight_unit,String ToDate) {
		this.id=id;
		this.ToDate=ToDate;
		this.GroupId = GroupId;
		this.FilterCondition = FilterCondition;
		this.FilterAMPMCondition=FilterAMPMCondition;
		this.setting_weight_unit=setting_weight_unit;
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getGroupId() {
		return GroupId;
	}
	public void setGroupId(String GroupId) {
		this.GroupId = GroupId;
	}
	public String getFilterCondition() {
		return FilterCondition;
	}
	public void setFilterCondition(String FilterCondition) {
		this.FilterCondition = FilterCondition;
	}
	
	
}
