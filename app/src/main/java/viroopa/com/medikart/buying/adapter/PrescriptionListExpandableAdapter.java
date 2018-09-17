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
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import viroopa.com.medikart.R;
import viroopa.com.medikart.buying.PrescriptionDetails;
import viroopa.com.medikart.buying.model.Category;
import viroopa.com.medikart.buying.model.ItemDetail;

public class PrescriptionListExpandableAdapter extends BaseExpandableListAdapter {

	private List<Category> catList;
	private int itemLayoutId;
	private int groupLayoutId;
	private Context ctx;

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	SimpleDateFormat dateFormat_header = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
	DateFormat dateFormat_display = new SimpleDateFormat("dd");
	DateFormat dateFormat_heading_display = new SimpleDateFormat("LLLL");
	DateFormat dateFormat_letterIn_day_display = new SimpleDateFormat("LLLL", Locale.getDefault());


	public PrescriptionListExpandableAdapter(List<Category> catList, Context ctx) {
		
		this.itemLayoutId =  R.layout.refill_order_item;
		this.groupLayoutId = R.layout.header_for_order;
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
			v = inflater.inflate(R.layout.prescription_list_expandable, parent, false);
		}

		TextView ref_prescDate = (TextView) v.findViewById(R.id.ref_prescDate);

		TextView ref_prescDateSecond = (TextView) v.findViewById(R.id.ref_prescDateSecond);
		TextView ref_presc_req_no = (TextView) v.findViewById(R.id.ref_presc_req_no);

		ItemDetail det = catList.get(groupPosition).getItemList().get(childPosition);
		LinearLayout ll_refil_order_item = (LinearLayout) v.findViewById(R.id.ll_refil_order_item);
		try {
			Date current_date = Calendar.getInstance().getTime();
			current_date = dateFormat.parse(det.getref_OrderDate());
			String dayLetter = (dateFormat_letterIn_day_display.format(current_date));
			ref_prescDate.setText(dateFormat_display.format(current_date));
			ref_prescDateSecond.setText(dayLetter);
		}catch(Exception e)
		{

		}


		ref_presc_req_no.setText("Request no : "+ " " +det.getorder_no());

		ll_refil_order_item.setTag(det.getOrderId());
		ll_refil_order_item.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				get_orderdetails(view.getTag().toString());
			}
		});
		return v;
		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int size = catList.get(groupPosition).getItemList().size();
		//System.out.println("Child for group [" + groupPosition + "] is [" + size + "]");
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
			v = inflater.inflate(R.layout.header_for_order, parent, false);
		}
		
		TextView groupName = (TextView) v.findViewById(R.id.text);
		//TextView groupDescr = (TextView) v.findViewById(R.id.groupDescr);
		
			
		Category cat = catList.get(groupPosition);

		try {

			String order_m=cat.getName();
			String order_y=cat.getDescr();


			groupName.setText(order_m+" "+order_y);
		}catch(Exception e)
		{
              e.toString();
		}
		
		//groupName.setText(cat.getName());
		//groupDescr.setText(cat.getDescr());
		
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
	public String getMonth(int month) {
		return new DateFormatSymbols().getMonths()[month-1];
	}


	private void get_orderdetails(String p_id) {
		Intent intent_refill = new Intent(ctx, PrescriptionDetails.class);
		intent_refill.putExtra("Prescption_id", p_id);
		ctx.startActivity(intent_refill);
	}

}
