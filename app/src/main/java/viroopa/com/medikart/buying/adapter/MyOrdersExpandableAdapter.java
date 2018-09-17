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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.buying.CancelProduct;
import viroopa.com.medikart.buying.model.OrderItemDetail;
import viroopa.com.medikart.buying.model.OrdersCategory;
import viroopa.com.medikart.buying.refillitemForMyOrder;

public class MyOrdersExpandableAdapter extends BaseExpandableListAdapter {

	private List<OrdersCategory> catList;
	private TextView important_advisory_txt;
	private int itemLayoutId;
	private int groupLayoutId;
	private Context ctx;
	private String sInfo,SMoleculeInteraction,sdrugInteraction;
	private TabHost tabs;
	private LinearLayout lnrtest,lnrSecond,lnrthird,drug_molecule_linear_heading;
	private Button general_btn,drug_btn;
	private  Boolean question_Ans_available=false;
	private Integer statusImg=0;
	private ProgressDialog pDialog;
	private String sMemberId;
	private int nTotalCartCount,nCartId;
	static int mNotifCount = 0;
	private ExpandableListView exp;
	private String CLICKED;
	private com.nostra13.universalimageloader.core.ImageLoader imageLoader;

	DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss  MMM dd, cccc, yyyy");

	public MyOrdersExpandableAdapter(List<OrdersCategory> catList, Context ctx, String MemberId, ExpandableListView exp) {
		
		this.itemLayoutId =  R.layout.trackin_item_detial;
		this.groupLayoutId = R.layout.refill_detailed_item_my_order;
		this.catList = catList;
		this.ctx = ctx;
		this.exp=exp;
		this.sMemberId =  MemberId;
		pDialog = new ProgressDialog(ctx);
		initImageLoader();
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
			v = inflater.inflate(R.layout.trackin_item_detial, parent, false);
		}




		OrderItemDetail det = catList.get(groupPosition).getItemList().get(childPosition);

		final TextView tracking_time = (TextView) v.findViewById(R.id.tracking_time);
		final TextView tracking_description = (TextView) v.findViewById(R.id.tracking_description);

		if(det!=null)
		{

			Date current_date = Calendar.getInstance().getTime();
			try {
				current_date = dateFormat_query.parse(det.gettracking_date());
				tracking_time.setText(dateFormat.format(current_date));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			tracking_description.setText(det.gettracking_description());
		}

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
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			final View convertView, final ViewGroup parent) {

		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService    (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.refill_detailed_item_my_order, parent, false);
		}


		OrdersCategory cat = catList.get(groupPosition);

		TextView refi_product = (TextView) v.findViewById(R.id.refi_product);
		TextView refi_packsize = (TextView) v.findViewById(R.id.refi_packsize);
		TextView refi_amount = (TextView) v.findViewById(R.id.refi_amount);
		TextView refi_Qty = (TextView) v.findViewById(R.id.refi_Qty);
		TextView refi_status = (TextView) v.findViewById(R.id.Status);
		final TextView btntracking = (TextView) v.findViewById(R.id.btntracking);

		//btntracking.setTag(groupPosition);
		//Button cancelOrderBtn = (Button) convertView.findViewById(R.id. btncancel);



		ImageView thumbnail  =(ImageView)v.findViewById(R.id.thumbnail);

		TextView refi_assigned = (TextView) v.findViewById(R.id.txtStatus1);
		TextView refi_transisit = (TextView) v.findViewById(R.id.txt0Status2);
		TextView refi_Delivered = (TextView) v.findViewById(R.id.txt0Status3);
		TextView refi_notdelivered = (TextView) v.findViewById(R.id.txt0Status4);

		ImageView firstIcon = (ImageView) v.findViewById(R.id.txtStatus);
		ImageView secondIcon = (ImageView) v.findViewById(R.id.txt0Status);
		ImageView thirdIcon = (ImageView) v.findViewById(R.id.txt1Status);
		ImageView fourthIcon = (ImageView) v.findViewById(R.id.txt2Status);
		ImageView btn_cancel_product = (ImageView) v.findViewById(R.id.btn_cancel_product);

		/*btntracking.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {


										   @Override
										   public boolean onGroupClick(ExpandableListView parent, View v,
																	   int groupPosition, long id) {

										   }
									   });*/


		/*btntracking.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {

						break;
					}
					case MotionEvent.ACTION_UP: {
						v.setTag(CLICKED);
					}
					case MotionEvent.ACTION_CANCEL: {

						break;
					}
				}
				return true;
			}
		});
*/


		/*btntracking.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				//int pos = (int) btntracking.getTag();
				v.setTag(CLICKED);


			}
		});*/






		String sRupee = "Rs.";

		refi_product.setText(cat.getProduct());
		refi_packsize.setText("Pack size : " + cat.getPacksize());
		Float nPrice = Integer.parseInt(cat.getQuantity()) * Float.parseFloat(cat.getPrice());
		//refi_amount.setText(sRupee + " " + nPrice.toString());


		Double double_price=0.00;
		DecimalFormat df = new DecimalFormat("#.00");

		double_price = Double.parseDouble(String.valueOf(nPrice));

		refi_amount.setText(sRupee + " " + df.format(double_price).toString());


		// refi_amount.setText(sRupee + " " + String.format("%.0f", nPrice));
		refi_Qty.setText("Qty : " +cat.getQuantity());

		if(cat.getImageUrl()!=null) {
			imageLoader.displayImage(cat.getImageUrl(), thumbnail);
		}




		final String Status=cat.getStatus();
		Integer Statuscode=cat.getStatusCode();

		if(Statuscode==1)
		{
			btn_cancel_product.setVisibility(View.VISIBLE);
			refi_status.setText("Order Placed");
			refi_assigned.setText("Order palced");
			// refi_transisit.setText("Confirmed");

			firstIcon.setImageResource(R.drawable.fill_circle_orange);
              /*  firstIcon.setCompoundDrawablesWithIntrinsicBounds(
                       , //left
                        0, //top
                        0, //right
                        0);*/

               /* secondIcon.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.greencircle, //left
                        0, //top
                        0, //right
                        0);*/



		} else if (Statuscode==2)
		{
			btn_cancel_product.setVisibility(View.VISIBLE);
			refi_assigned.setText("Order placed");
			refi_transisit.setText("Confirmed");
			refi_status.setText("Order Confirmed");
			//refi_Delivered.setText("Dispatched");
			firstIcon.setImageResource(R.drawable.fill_circle_orange);


			secondIcon.setImageResource(R.drawable.fill_circle_orange);
               /* thirdIcon.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.greencircle, //left
                        0, //top
                        0, //right
                        0);*/

			refi_assigned.setText("Order palced");
			refi_transisit.setText("Confirmed");
		}
		else if(Statuscode==3)
		{
			refi_assigned.setText("Order placed");
			refi_transisit.setText("Confirmed");
			refi_Delivered.setText("Dispatched");
			refi_status.setText("Order Dispatched");
			btn_cancel_product.setVisibility(View.VISIBLE);
			firstIcon.setImageResource(R.drawable.fill_circle_orange);

			secondIcon.setImageResource(R.drawable.fill_circle_orange);
			thirdIcon.setImageResource(R.drawable.fill_circle_orange);


		}else if(Statuscode==4)
		{
			if(Status.equals("DEL"))
			{
				refi_assigned.setText("Order placed");
				refi_transisit.setText("Confirmed");
				refi_Delivered.setText("Dispatched");
				refi_notdelivered.setText("Delivered");
				refi_status.setText(" Order Delivered");
				btn_cancel_product.setVisibility(View.GONE);

				firstIcon.setImageResource(R.drawable.fill_circle_orange);

				secondIcon.setImageResource(R.drawable.fill_circle_orange);
				thirdIcon.setImageResource(R.drawable.fill_circle_orange);
				fourthIcon.setImageResource(R.drawable.fill_circle_orange);




			}
			else if(Status.equals("NDEL")){
				btn_cancel_product.setVisibility(View.GONE);
				refi_assigned.setText("Order placed");
				refi_transisit.setText("Confirmed");
				refi_Delivered.setText("Dispatched");
				refi_notdelivered.setText("Not Delivered");
				refi_status.setText("Not Delivered");
				firstIcon.setImageResource(R.drawable.fill_circle_orange);
				secondIcon.setImageResource(R.drawable.fill_circle_orange);
				thirdIcon.setImageResource(R.drawable.fill_circle_orange);
				fourthIcon.setImageResource(R.drawable.fill_circle_orange);
			}

		}else if(Statuscode==5)
		{
			btn_cancel_product.setVisibility(View.GONE);
			refi_assigned.setText("Order placed");
			refi_transisit.setText("");
			refi_Delivered.setText("");
			refi_notdelivered.setText("Cancelled");
			refi_status.setText("Order Cancelled");

			firstIcon.setImageResource(R.drawable.fill_circle_orange);
			secondIcon.setImageResource(R.drawable.fill_circle_grey);
			thirdIcon.setImageResource(R.drawable.fill_circle_grey);
			fourthIcon.setImageResource(R.drawable.redcircle);

		}



/*            Button btnaddtocart = (Button) convertView.findViewById(R.id.btnaddtocart);
            btnaddtocart.setTag(o_singleorder_detail.getProductId());*/

//            ImageView btnplus = (ImageView) convertView.findViewById(R.id.btnplus);
//            ImageView btnminus = (ImageView) convertView.findViewById(R.id.btnminus);


		btn_cancel_product.setTag(R.id.key_product_id, cat.getProductId());
		btn_cancel_product.setTag(R.id.key_order_id, cat.getOrderId());


//            btnplus.setTag(o_singleorder_detail.getProductId());
//            btnminus.setTag(o_singleorder_detail.getProductId());

		// tracking_layout.setVisibility(View.INVISIBLE);

		btn_cancel_product.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String gt_Product_id = (String) view.getTag(R.id.key_product_id);
				String order_id = (String) view.getTag(R.id.key_order_id);

				Intent intent= new Intent(ctx, CancelProduct.class);
				intent.putExtra("Order_id", order_id);
				intent.putExtra("Product_id", gt_Product_id);
				ctx.startActivity(intent);

			}
		});
		
			



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





	private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				ctx).defaultDisplayImageOptions(defaultOptions).memoryCache(
				new WeakMemoryCache());

		ImageLoaderConfiguration config = builder.build();
		imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
		imageLoader.init(config);
	}


	private void Success_addtocart(JSONObject response) {
		try {
			nTotalCartCount = Integer.parseInt(response.getString("TotalCartCount"));
			nCartId = Integer.parseInt(response.getString("iCartId"));

			SharedPreferences pref = ctx.getSharedPreferences("Global", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString("cartid", response.getString("iCartId"));
			editor.putString("addtocart_count",response.getString("TotalCartCount"));
			editor.commit();

			setNotifCount(nTotalCartCount);

			f_alert_ok("Information", "Add Cart Successfully");
		}
		catch (Exception e) {
			hidePDialog();
			e.printStackTrace();
		}
	}
	private void f_alert_ok(String p_title, String p_msg) {
		new AlertDialog.Builder(ctx)
				.setTitle(p_title)
				.setMessage(p_msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	private void Error_addtocart(VolleyError error) {
		f_alert_ok("Error : ", error.getMessage());
	}
	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			//pDialog = null;
		}
	}

	private void showPdialog(String sMessage) {
		if (pDialog != null) {
			pDialog.setMessage(sMessage);
			pDialog.show();
		}
	}


	private void setNotifCount(int count){
		mNotifCount = count;

		((refillitemForMyOrder)ctx).invalidateOptionsMenu();
	}
	private void setListViewHeight(ExpandableListView listView) {
		ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			View groupView = listAdapter.getGroupView(i, true, null, listView);
			groupView.measure(0, View.MeasureSpec.UNSPECIFIED);
			totalHeight += groupView.getMeasuredHeight();

			if (listView.isGroupExpanded(i)){
				for(int j = 0; j < listAdapter.getChildrenCount(i); j++){
					View listItem = listAdapter.getChildView(i, j, false, null, listView);
					listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
					totalHeight += listItem.getMeasuredHeight();
				}
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
	private void setListViewHeight(ExpandableListView listView,
                               int group) {
    ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
    int totalHeight = 0;
    int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
            View.MeasureSpec.EXACTLY);
    for (int i = 0; i < listAdapter.getGroupCount(); i++) {
        View groupItem = listAdapter.getGroupView(i, false, null, listView);
        groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

        totalHeight += groupItem.getMeasuredHeight();

        if (((listView.isGroupExpanded(i)) && (i != group))
                || ((!listView.isGroupExpanded(i)) && (i == group))) {
            for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                View listItem = listAdapter.getChildView(i, j, false, null,
                        listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                totalHeight += listItem.getMeasuredHeight();

            }
        }
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    int height = totalHeight
            + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
    if (height < 10)
        height = 200;
    params.height = height;
    listView.setLayoutParams(params);
    listView.requestLayout();

}
}
