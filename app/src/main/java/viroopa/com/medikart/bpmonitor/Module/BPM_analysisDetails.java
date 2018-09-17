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

public class BPM_analysisDetails implements Serializable {

	private long id;


	public String getSys_max() {
		return sys_max;
	}

	public void setSys_max(String sys_max) {
		this.sys_max = sys_max;
	}

	private String sys_max;

	public String getSys_min() {
		return sys_min;
	}

	public void setSys_min(String sys_min) {
		this.sys_min = sys_min;
	}

	public String getAvg_sys() {
		return avg_sys;
	}

	public void setAvg_sys(String avg_sys) {
		this.avg_sys = avg_sys;
	}

	public String getDia_max() {
		return dia_max;
	}

	public void setDia_max(String dia_max) {
		this.dia_max = dia_max;
	}

	public String getDia_min() {
		return dia_min;
	}

	public void setDia_min(String dia_min) {
		this.dia_min = dia_min;
	}

	public String getAvg_dia() {
		return avg_dia;
	}

	public void setAvg_dia(String avg_dia) {
		this.avg_dia = avg_dia;
	}

	public String getMax_pulse() {
		return max_pulse;
	}

	public void setMax_pulse(String max_pulse) {
		this.max_pulse = max_pulse;
	}

	public String getMin_pulse() {
		return min_pulse;
	}

	public void setMin_pulse(String min_pulse) {
		this.min_pulse = min_pulse;
	}

	public String getAvg_pulse() {
		return avg_pulse;
	}

	public void setAvg_pulse(String avg_pulse) {
		this.avg_pulse = avg_pulse;
	}

	public String getMax_wt() {
		return max_wt;
	}

	public void setMax_wt(String max_wt) {
		this.max_wt = max_wt;
	}

	public String getMin_wt() {
		return min_wt;
	}

	public void setMin_wt(String min_wt) {
		this.min_wt = min_wt;
	}

	public String getAvg_wt() {
		return avg_wt;
	}

	public void setAvg_wt(String avg_wt) {
		this.avg_wt = avg_wt;
	}

	private String sys_min;
	private String avg_sys;
	private String dia_max;
	private String dia_min;
	private String avg_dia;
	private String max_pulse;
	private String min_pulse;
	private String avg_pulse;
	private String max_wt;
	private String min_wt;
	private String avg_wt;


	public BPM_analysisDetails(long id, String sys_max, String sys_min, String avg_sys, String
			dia_max, String dia_min, String avg_dia, String max_pulse,
							   String min_pulse, String avg_pulse,String max_wt, String min_wt, String avg_wt)
	{
		this.id=id;
		this.sys_max=sys_max;
		this.sys_min=sys_min;
		this.avg_sys=avg_sys;
		this.dia_max=dia_max;
		this.dia_min=dia_min;
		this.avg_dia=avg_dia;
		this.max_pulse=max_pulse;
		this.min_pulse=min_pulse;
		this.avg_pulse=avg_pulse;
		this.max_wt=max_wt;
		this.min_wt=min_wt;
		this.avg_wt=avg_wt;

	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	
	
}
