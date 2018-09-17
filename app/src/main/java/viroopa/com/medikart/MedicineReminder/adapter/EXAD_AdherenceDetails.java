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

package viroopa.com.medikart.MedicineReminder.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import viroopa.com.medikart.MedicineReminder.MRA_ReminderMain;
import viroopa.com.medikart.MedicineReminder.Model.Category;
import viroopa.com.medikart.MedicineReminder.Model.ItemDetail;
import viroopa.com.medikart.R;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.helper.SqliteMRHandler;

public class EXAD_AdherenceDetails extends BaseExpandableListAdapter {

	private List<Category> catList;
	private int itemLayoutId;
	private int groupLayoutId;
	private Context ctx;
	private SqliteMRHandler db_mr;
	private  LinearLayout lnrtest;
	private String sMemberId;
	private int clr=0;
	Date current_date = Calendar.getInstance().getTime();
	DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd");
	private TextView main_message,second_message;
	private ImageView img_first_part ;
	private ImageView img_second_part ;

	private Boolean attach_child_view=false;

	SimpleDateFormat dateFormat_header = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	DateFormat dateFormat_display = new SimpleDateFormat("dd");
	DateFormat dateFormat_heading_display = new SimpleDateFormat("LLL dd,EEEE");
	DateFormat dateFormat_letterIn_day_display = new SimpleDateFormat("EEE", Locale.getDefault());
	DateFormat dateFormat_time = new SimpleDateFormat("hh.mm a");

	int[] color_arr={Color.parseColor("#C2DDF7"),Color.parseColor("#A4CBF2"),Color.parseColor("#8FBBE7"),
			Color.parseColor("#7EABD9"),Color.parseColor("#3D84CA"),Color.parseColor("#3175B9"),Color.parseColor("#336494"),
			Color.parseColor("#29547F"),Color.parseColor("#1D446C")};

	public EXAD_AdherenceDetails(List<Category> catList, Context ctx, String sMemberId) {

		this.itemLayoutId =  R.layout.adherence_details;
		this.groupLayoutId = R.layout.adherence_heading;
		this.sMemberId=sMemberId;
		this.catList = catList;
		this.ctx = ctx;
		getIntenet();
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
		v=null;
		if(childPosition==0) {
			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.adherence_details, parent, false);
			}
			lnrtest= (LinearLayout) v.findViewById(R.id.lnrtest);
			main_message= (TextView) v.findViewById(R.id.txtawsome);
			second_message= (TextView) v.findViewById(R.id.txtstatusmessage);
			ItemDetail det = catList.get(groupPosition).getItemList().get(childPosition);

			try {

				current_date = dateFormat.parse(det.getScheduled_Start_Date());
				String Start_Date = (dateFormat_query.format(current_date));

				current_date = dateFormat.parse(det.getScheduled_End_Date());
				String End_Date = (dateFormat_query.format(current_date));
				show_message_on_performance(Start_Date);

				  show_all_medicines(Start_Date, End_Date, det.getMedicine_name());

			} catch (Exception e) {
               e.toString();
			}



		}
		return v;

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int size = catList.get(groupPosition).getItemList().size();
		System.out.println("Child for group [" + groupPosition + "] is [" + size + "]");
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
		db_mr = new SqliteMRHandler(ctx);

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService    (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.adherence_heading, parent, false);
		}

		//TextView groupDescr = (TextView) v.findViewById(R.id.groupDescr);
		final LinearLayout linearFirst=(LinearLayout) v.findViewById(R.id.linearFirst);
		final TextView txtDate = (TextView) v.findViewById(R.id.txtDate);
		final TextView txttaken = (TextView) v.findViewById(R.id.txttaken);
		final TextView txttakennumber = (TextView) v.findViewById(R.id.txttakennumber);
		final TextView txtmissed = (TextView) v.findViewById(R.id.txtmissed);
		final TextView txtmissednumber = (TextView) v.findViewById(R.id.txtmissednumber);






		Category cat = catList.get(groupPosition);
		if(cat!=null) {

			if(Integer.parseInt(cat.getTaken()) < 10)
			{
				txttakennumber.setText(" "+cat.getTaken());
			}else
			{
				txttakennumber.setText(cat.getTaken());
			}

			if(Integer.parseInt(cat.getPending()) < 10)
			{
				txtmissednumber.setText(" "+cat.getPending());
			}else
			{
				txtmissednumber.setText(cat.getPending());
			}


			try {
				current_date = dateFormat.parse(cat.getScheduled_med_date());
				String dayLetter = (dateFormat_heading_display.format(current_date));
				txtDate.setText(dayLetter);
			} catch (Exception e) {

			}
		}

		//groupName.setText(cat.getName());
		//groupDescr.setText(cat.getDescr());
	/*	switch (groupPosition) {
			case 0:
				linearFirst.setBackgroundColor(color_arr[groupPosition]);
				break;
			case 1:
				linearFirst.setBackgroundColor(color_arr[groupPosition]);
				break;
			case 2:
				linearFirst.setBackgroundColor(color_arr[groupPosition]);
				break;
			case 3:
				linearFirst.setBackgroundColor(color_arr[groupPosition]);
				break;
			case 4:
				linearFirst.setBackgroundColor(color_arr[groupPosition]);
				break;
			case 5:
				linearFirst.setBackgroundColor(color_arr[groupPosition]);
				break;
			case 6:
				linearFirst.setBackgroundColor(color_arr[groupPosition]);
				break;


		}
*/		return v;

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


	private void get_orderdetails(String p_orderid) {
		Intent intent_refill = new Intent(ctx, MRA_ReminderMain.class);
		intent_refill.putExtra("orderid", p_orderid);
		ctx.startActivity(intent_refill);
	}
	private void show_all_medicines(String p_start_date,String p_end_date,String MedicineName)
	{
		lnrtest.removeAllViews();
		Integer ImgResourceId=0;

		Cursor cursor_session = db_mr.get_med_rem_all_schedule_medicine(p_start_date,p_end_date,MedicineName,sMemberId);
		int c=cursor_session.getCount();

		if ((cursor_session != null) || (cursor_session.getCount()> 0))
		{
			if (cursor_session.moveToFirst()) {
				do {

						LayoutInflater inflater = null;

						inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View mLinearView = inflater.inflate(R.layout.med_rem_header_listview, null);
						final TextView txtmedicine = (TextView) mLinearView.findViewById(R.id.txtmedicine);
						final TextView txtstatus = (TextView) mLinearView.findViewById(R.id.txtstatus);
						final TextView txttime = (TextView) mLinearView.findViewById(R.id.txttime);
						final ImageView imgStatus = (ImageView) mLinearView.findViewById(R.id.imgStatus);
						final LinearLayout imgmedicine = (LinearLayout) mLinearView.findViewById(R.id.imgmedicine);

						try {

							current_date = dateFormat_header.parse(cursor_session.getString(cursor_session.getColumnIndex("datetime_set")));
							String dayLetter = (dateFormat_time.format(current_date));
							txttime.setText(dayLetter);
						} catch (Exception e) {

						}


						add_image_views(imgmedicine, cursor_session.getInt(cursor_session.getColumnIndex("image_id")), cursor_session.getInt(cursor_session.getColumnIndex("first_color_id")), cursor_session.getInt(cursor_session.getColumnIndex("second_color_id")), new LinearLayout.LayoutParams(15, 30));


						txtmedicine.setText(cursor_session.getString(cursor_session.getColumnIndex("medicine_name")));
						txtstatus.setText(cursor_session.getString(cursor_session.getColumnIndex("status")));

							if (cursor_session.getString(cursor_session.getColumnIndex("status")).equals("p")) {
							imgStatus.setImageResource(R.drawable.orangecircle);
						} else if (cursor_session.getString(cursor_session.getColumnIndex("status")).equals("taken")) {
							imgStatus.setImageResource(R.drawable.greencircle);
						}

						lnrtest.addView(mLinearView);

					} while (cursor_session.moveToNext()) ;

					//adapter_daywise_session.notifyDataSetChanged();

			}
		}




	}

	private void show_message_on_performance(String Sch_DateTime) {





		Double total_taken_count = 0.0;
		Double total_pending_count = 0.0;

		Cursor cursor_session = db_mr.get_med_rem_sch_daywise_session("all", Sch_DateTime,sMemberId);
		int numm = cursor_session.getCount();
		if ((cursor_session != null) || (cursor_session.getCount() > 0)) {


			if (cursor_session.moveToFirst()) {
				do {


					if(cursor_session.getString(cursor_session.getColumnIndex("status")).equals("T"))
					{
						total_taken_count++;
					}else {
						total_pending_count++;
					}


				} while (cursor_session.moveToNext());

			}

			try {
				// pending_all_count=taken_all_count-pending_all_count;

				Double adhenrence_first = (total_taken_count / (total_taken_count+total_pending_count));

				if(adhenrence_first.isInfinite()==true)
				{
					adhenrence_first=0.0;
				}
				Double adherence_percent = adhenrence_first * 100;

				if(adherence_percent<1 )
				{
					main_message.setText("Bad !!");
					second_message.setText("Please start your medicines");
				}
				else if(adherence_percent>0 && adherence_percent<35)
				{
					main_message.setText("Poor !!");
					second_message.setText("Take your medicines");
				}else if(adherence_percent>35 && adherence_percent<70)
				{
					main_message.setText("good !!");
					second_message.setText("Try to take all medicines on time");
				}
				else if( adherence_percent>70)
				{
					main_message.setText("Awsome !!");
					second_message.setText("You doing well");
				}

			}catch (Exception e)
			{}
		}
	}
	private void add_image_views(LinearLayout lnr, Integer Image_id, Integer f_color_id, Integer s_color_id, LinearLayout.LayoutParams customparam )
	{
		lnr.removeAllViews();
		img_first_part = new ImageView(ctx);
		img_second_part = new ImageView(ctx);
		customparam.gravity= Gravity.CENTER_VERTICAL;
		lnr.setGravity(Gravity.CENTER);
		if(s_color_id!=null)
		{
			if(s_color_id!=-99)
			{
				img_first_part.setLayoutParams(customparam);
				img_second_part.setLayoutParams(customparam);
				img_first_part.setImageResource(R.drawable.med_img_part_frst);
				img_second_part.setImageResource(R.drawable.med_img_part_second);
				img_first_part.setColorFilter(ConstData.colr_array[f_color_id], PorterDuff.Mode.MULTIPLY);
				img_second_part.setColorFilter(ConstData.colr_array[s_color_id], PorterDuff.Mode.MULTIPLY);
				lnr.addView(img_first_part);
				lnr.addView(img_second_part);
			}else
			{
				LinearLayout.LayoutParams one_img_param=new LinearLayout.LayoutParams(30,30);
				img_first_part.setImageResource(ConstData.Medicine_array1[Image_id]);
				img_first_part.setLayoutParams(one_img_param);
				img_first_part.setColorFilter(ConstData.colr_array[f_color_id], PorterDuff.Mode.MULTIPLY);
				lnr.addView(img_first_part);
			}
		}



	}
	private void getIntenet() {

		SharedPreferences pref = ctx.getSharedPreferences("Global", ctx.MODE_PRIVATE);
		//sMemberId = pref.getString("memberid", "");

	}
}
