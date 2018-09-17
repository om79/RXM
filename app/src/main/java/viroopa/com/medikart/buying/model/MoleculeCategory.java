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


public class MoleculeCategory implements Serializable {

	private long id;
	private String name;
	private String descr;

	private List<MoleculeItemDetail> itemList = new ArrayList<MoleculeItemDetail>();

	public MoleculeCategory(long id, String name, String descr) {
		this.id = id;
		this.name = name;
		this.descr=descr;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public List<MoleculeItemDetail> getItemList() {
		return itemList;
	}

	public void setItemList(List<MoleculeItemDetail> itemList) {
		this.itemList = itemList;
	}
	
	

}
