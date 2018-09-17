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
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.buying.model.MoleculeCategory;
import viroopa.com.medikart.buying.model.MoleculeItemDetail;

public class MoleculeInfoExpandableAdapter extends BaseExpandableListAdapter {

	private List<MoleculeCategory> catList;
	private TextView important_advisory_txt;
	private int itemLayoutId;
	private int groupLayoutId;
	private Context ctx;
	private String sInfo,SMoleculeInteraction,sdrugInteraction;
	private TabHost tabs;
	private LinearLayout lnrtest,lnrSecond,lnrthird,drug_molecule_linear_heading;
	private Button general_btn,drug_btn;
	private  Boolean question_Ans_available=false;


	public MoleculeInfoExpandableAdapter(List<MoleculeCategory> catList, Context ctx) {
		
		this.itemLayoutId =  R.layout.page_molecule_info_detail;
		this.groupLayoutId = R.layout.page_molecule_info_master;
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
		v=null;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.page_molecule_info_detail, parent, false);
		}

		lnrtest=(LinearLayout) v.findViewById(R.id.lnrtest);

		lnrSecond=(LinearLayout) v.findViewById(R.id.lnrSecond);
		lnrthird=(LinearLayout) v.findViewById(R.id.lnrthird);
		drug_molecule_linear_heading=(LinearLayout) v.findViewById(R.id.drug_molecule_linear_heading);

		general_btn=(Button)v.findViewById(R.id.general_btn);
		drug_btn=(Button)v.findViewById(R.id.drug_btn);
		tabs = (TabHost) v.findViewById(R.id.dmTabHost);
		important_advisory_txt=(TextView)v.findViewById(R.id.important_advisory_txt);

		tabs.setup();

		MoleculeItemDetail det = catList.get(groupPosition).getItemList().get(childPosition);
		sInfo=det.getQuestion();
		SMoleculeInteraction=det.getAnswer();
		sdrugInteraction=det.getDrug_interaction_heading();
		important_advisory_txt.setText("Important Advisory for "+det.getMolecule_interaction_heading());
		show_question_answer(String.valueOf(det.getId()));
		show_molecule_interaction(String.valueOf(det.getId()));
		show_Drug_interaction(String.valueOf(det.getId()));

		TabHost.TabSpec tabpage1 = tabs.newTabSpec("one");
		tabpage1.setContent(R.id.lnrtest);
		tabpage1.setIndicator("General Information", ctx.getResources().getDrawable(R.drawable.orgbtn));
		TabHost.TabSpec tabpage2 = tabs.newTabSpec("two");
		tabpage2.setContent(R.id.second_layout);

		tabpage2.setIndicator("Drug-Drug interaction", ctx.getResources().getDrawable(R.drawable.arrowdwn));
		// setTabColor(tabs);

		tabs.setFocusable(false);
		tabs.addTab(tabpage1);
		tabs.addTab(tabpage2);
		final TextView tv = (TextView) tabs.getTabWidget().getChildAt(tabs.getCurrentTab()).findViewById(android.R.id.title);
		tv.setTextColor(Color.WHITE);

		for(int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
			View xy = tabs.getTabWidget().getChildAt(i);

			// Look for the title view to ensure this is an indicator and not a divider.
			TextView tvx = (TextView)xy.findViewById(android.R.id.title);
			if(tvx == null) {
				continue;

			}
			tvx.setAllCaps(false);
			tvx.setTextColor(Color.parseColor("#bdbdbd"));
			xy.setBackgroundResource(R.drawable.tab_selector);
		}

	/*	tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				final TextView tv = (TextView) tabs.getTabWidget().getChildAt(tabs.getCurrentTab()).findViewById(android.R.id.title);
				tv.setTextColor(Color.WHITE);

				for (int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
					TextView tvUnselect = (TextView) tabs.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
					tvUnselect.setTextColor(Color.parseColor("#08446a"));
				}
				TextView tvSelecterd = (TextView) tabs.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
				tvSelecterd.setTextColor(Color.WHITE);
				setTabColor(tabs);


			}
		});
		tabs.getTabWidget().getChildAt(tabs.getCurrentTab()).setBackgroundColor(Color.parseColor("#df6301"));


		for (int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
			TextView tvUnselect = (TextView) tabs.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
			tvUnselect.setTextColor(Color.parseColor("#08446a"));
		}

*/

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
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService    (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.page_molecule_info_master, parent, false);
		}
		
		TextView groupName = (TextView) v.findViewById(R.id.textViewName);
		TextView groupDescr = (TextView) v.findViewById(R.id.textViewmolecule);
		
			
		MoleculeCategory cat = catList.get(groupPosition);

		groupName.setText(cat.getName());
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




	private void show_question_answer(String mol_id)
	{
		try{
		lnrtest.removeAllViews();
		Integer ImgResourceId=0;

		JSONArray ja_objMedicinedetails = new JSONArray(sInfo);

			if (ja_objMedicinedetails.length()>0) {
				for (int i = 0; i < ja_objMedicinedetails.length(); i++) {

					JSONObject obj_json = ja_objMedicinedetails.getJSONObject(i);

					if (mol_id.equals(obj_json.getString("MedicineMoleculeId"))) {

						LayoutInflater inflater = null;

						inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View mLinearView = inflater.inflate(R.layout.question_answer_details, null);

						final TextView txt_question = (TextView) mLinearView.findViewById(R.id.textViewItemName);
						final TextView txt_answer = (TextView) mLinearView.findViewById(R.id.textViewItemPrice);


						if(obj_json.getString("ProductQues").equals("")||obj_json.getString("ProductQues").equals("null"))
						{
							txt_question.setText("No Information available");
						}else
						{
							question_Ans_available=true;
							txt_question.setText(obj_json.getString("ProductQues"));
						}

						if(obj_json.getString("ProductAns").equals("")||obj_json.getString("ProductAns").equals("null"))
						{
							txt_answer.setText("No Information available");
						}else
						{
							txt_answer.setText(obj_json.getString("ProductAns"));
						}




						lnrtest.addView(mLinearView);
					}
				}
			}else
			{
				LayoutInflater inflater = null;
				inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View mLinearView = inflater.inflate(R.layout.no_item_to_show, null);
				lnrtest.addView(mLinearView);
			}
		}catch (JSONException e)
		{
			e.toString();
		}




	}

	private void show_molecule_interaction(String mol_id)
	{
		try{
			lnrthird.removeAllViews();
			Integer ImgResourceId=0;

			JSONArray ja_objMedicinedetails = new JSONArray(SMoleculeInteraction);
			if (ja_objMedicinedetails.length()>0 ) {
				drug_molecule_linear_heading.setVisibility(View.VISIBLE);
				for (int i = 0; i < ja_objMedicinedetails.length(); i++) {

					JSONObject obj_json = ja_objMedicinedetails.getJSONObject(i);

					if (mol_id.equals(obj_json.getString("MoleculeId"))) {

						LayoutInflater inflater = null;

						inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View mLinearView = inflater.inflate(R.layout.molecule_interaction_item, null);
						final TextView mol_heading = (TextView) mLinearView.findViewById(R.id.mol_heading);
						final TextView mol_details = (TextView) mLinearView.findViewById(R.id.mol_details);

						if(obj_json.getString("DrugInteractionName").equals("")||obj_json.getString("DrugInteractionName").equals("null"))
						{
							mol_heading.setText("No Information available");
						}else
						{
							mol_heading.setText(obj_json.getString("DrugInteractionName"));
						}

						if(obj_json.getString("DrugInteractionDesc").equals("")||obj_json.getString("DrugInteractionDesc").equals("null"))
						{
							mol_details.setText("No Information available");
						}else
						{
							mol_details.setText(obj_json.getString("DrugInteractionDesc"));
						}





						lnrthird.addView(mLinearView);
					}
				}
			}else
			{
				//LayoutInflater inflater = null;
				//inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				//View mLinearView = inflater.inflate(R.layout.no_item_to_show, null);
				drug_molecule_linear_heading.setVisibility(View.INVISIBLE);
			}
		}catch (JSONException e)
		{
			e.toString();
		}




	}

	private void show_Drug_interaction(String mol_id)
	{
		try{
			lnrSecond.removeAllViews();
			Integer ImgResourceId=0;

			JSONArray ja_objMedicinedetails = new JSONArray(sdrugInteraction);


			if (ja_objMedicinedetails.length()>0) {

				for (int i = 0; i < ja_objMedicinedetails.length(); i++) {

					JSONObject obj_json = ja_objMedicinedetails.getJSONObject(i);

					if (mol_id.equals(obj_json.getString("MoleculeID"))) {

						LayoutInflater inflater = null;

						inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View mLinearView = inflater.inflate(R.layout.drug_interaction_detail, null);
						final TextView drug_heading = (TextView) mLinearView.findViewById(R.id.drug_heading);
						final TextView drug_detail = (TextView) mLinearView.findViewById(R.id.drug_detail);


						if(obj_json.getString("DrugDrugInteractionDesc").equals("")||obj_json.getString("DrugDrugInteractionDesc").equals("null"))
						{
							drug_heading.setText("No Information available");
						}else
						{
							drug_heading.setText(obj_json.getString("DrugDrugInteractionDesc"));
						}


						if(obj_json.getString("DrugInteractionMoleculeDetail").equals("")||obj_json.getString("DrugInteractionMoleculeDetail").equals("null"))
						{
							drug_detail.setText("No Information available");
						}else
						{
							drug_detail.setText(obj_json.getString("DrugInteractionMoleculeDetail"));
						}



						lnrSecond.addView(mLinearView);
					}
				}
			}else
			{
				TextView tv = new TextView(ctx);
				tv.setText("No Data Available");
				lnrSecond.addView(tv);
			}
		}catch (JSONException e)
		{
			e.toString();
		}




	}
	public void setTabColor(TabHost tabhost) {

		for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++)
			tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#dfe8f9"));

		if (tabhost.getCurrentTab() == 0)
			tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#df6301")); //1st tab selected

		else
			tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#df6301")); //2nd tab selected
	}
}
