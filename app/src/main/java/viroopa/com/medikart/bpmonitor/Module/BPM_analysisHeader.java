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


public class BPM_analysisHeader implements Serializable {
	
	private long id;
	private String Heading;


	private List<BPM_analysisDetails> itemList = new ArrayList<BPM_analysisDetails>();

	public BPM_analysisHeader(long id, String Heading) {
		this.id = id;
		this.Heading = Heading;


	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHeading() {
		return Heading;
	}

	public void setHeading(String Heading) {
		this.Heading = Heading;
	}



	public List<BPM_analysisDetails> getItemList() {
		return itemList;
	}

	public void setItemList(List<BPM_analysisDetails> itemList) {
		this.itemList = itemList;
	}
	
	

}
