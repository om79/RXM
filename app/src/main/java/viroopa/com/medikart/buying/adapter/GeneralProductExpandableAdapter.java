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

package viroopa.com.medikart.buying.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.buying.model.Category;
import viroopa.com.medikart.buying.model.ItemDetail;

public class GeneralProductExpandableAdapter extends BaseExpandableListAdapter {

	private List<Category> catList;
	private int itemLayoutId;
	private int groupLayoutId;
	private Context ctx;


	public GeneralProductExpandableAdapter(List<Category> catList, Context ctx) {
		
		this.itemLayoutId =  R.layout.general_product_items;
		this.groupLayoutId = R.layout.genaral_p_group_heading;
		this.catList = catList;
		this.ctx = ctx;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return catList.get(groupPosition).getItemList().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return catList.get(groupPosition).getItemList().get(childPosition).hashCode();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		View v = convertView;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.general_product_items, parent, false);
		}

		TextView txt_heading = (TextView) v.findViewById(R.id.txt_heading);
		TextView txt_detail = (TextView) v.findViewById(R.id.txt_detail);

		ItemDetail det = catList.get(groupPosition).getItemList().get(childPosition);

		if(det.getref_OrderDate().equals("NoData"))
		{
			txt_heading.setText("");
			txt_detail.setText("No data available");
		}
		else {
			txt_heading.setText(det.getref_OrderDate());
			txt_detail.setText(det.getorder_no());
		}
		return v;
		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int size = catList.get(groupPosition).getItemList().size();
		return size;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return catList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
	  return catList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return catList.get(groupPosition).hashCode();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService    (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.genaral_p_group_heading, parent, false);
		}
		Category cat = catList.get(groupPosition);
		
		TextView txt_main_heading = (TextView) v.findViewById(R.id.txt_main_heading);
		txt_main_heading.setText(cat.getName());
		
		return v;

	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}




}
