package viroopa.com.medikart.buying.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.model.M_orderlist;
import viroopa.com.medikart.buying.refillitem;

/**
 * Created by prakash on 18/08/15.
 */
public class adapterorderlist extends BaseAdapter {


    private Activity currentactivity;
    private LayoutInflater inflater;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateFormat_display = new SimpleDateFormat("dd LLLL yyyy");
    DateFormat dateFormat_letterIn_day_display = new SimpleDateFormat("EEE", Locale.getDefault());


    private List<M_orderlist> orderitem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public adapterorderlist(Activity currentactivity, List<M_orderlist> orderlists) {
        this.currentactivity = currentactivity;
        this.orderitem = orderlists;
    }

    @Override
    public int getCount() {
        return orderitem.size();
    }

    @Override
    public Object getItem(int location) {
        return orderitem.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) currentactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.refill_order_item, null);
        }
        // prakash
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        // getting Invoice data for the row
        M_orderlist O_orderlist = orderitem.get(position);
        if (O_orderlist != null) {

            //NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
            TextView ref_OrderNo = (TextView) convertView.findViewById(R.id.ref_OrderNo);
            TextView ref_OrderDate = (TextView) convertView.findViewById(R.id.ref_OrderDate);
            TextView ref_OrderDateSecond = (TextView) convertView.findViewById(R.id.ref_OrderDateSecond);

            //TextView ref_status = (TextView) convertView.findViewById(R.id.ref_status);
           //TextView ref_ExpectedDeliveryDate = (TextView) convertView.findViewById(R.id.ref_ExpectedDeliveryDate);
            TextView ref_OrderPrice = (TextView) convertView.findViewById(R.id.ref_OrderPrice);



            String sRupee ="Rs.";

            ref_OrderNo.setText(O_orderlist.getOrderNo());

            try {
                Date current_date = Calendar.getInstance().getTime();
                current_date = dateFormat.parse(O_orderlist.getOrderDate());
                String dayLetter=(dateFormat_display.format(current_date));
                String dayname=(dateFormat_letterIn_day_display.format(current_date));
                ref_OrderDateSecond.setText(dayname);
                ref_OrderDate.setText(dayLetter);
                ref_OrderDate.setGravity(Gravity.CENTER);
            }catch(Exception e)
            {

            }


            //ref_OrderDate.setText(O_orderlist.getOrderDate().substring(0,10));
            //ref_status.setText(O_orderlist.getStatus());
            //ref_ExpectedDeliveryDate.setText(O_orderlist.getExpectedDeliveryDate());
            ref_OrderPrice.setText(sRupee + " " + O_orderlist.getNetAmount());

            /*Button btnorderview = (Button) convertView.findViewById(R.id.btnorderview);
            btnorderview.setTag(O_orderlist.getOrderId());*/

            LinearLayout ll_refil_order_item = (LinearLayout) convertView.findViewById(R.id.ll_refil_order_item);
            ll_refil_order_item.setTag(O_orderlist.getOrderId());

           /* btnorderview.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    get_orderdetails(view.getTag().toString());
                }
            });*/

            ll_refil_order_item.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    get_orderdetails(view.getTag().toString());
                }
            });
        }
        return convertView;
    }

    private void get_orderdetails(String p_orderid) {
        Intent intent_refill = new Intent(currentactivity, refillitem.class);
        intent_refill.putExtra("orderid", p_orderid);
        currentactivity.startActivity(intent_refill);
    }

}


