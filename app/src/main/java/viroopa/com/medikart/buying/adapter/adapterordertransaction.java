package viroopa.com.medikart.buying.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.model.M_orderlist;
import viroopa.com.medikart.buying.refillitemForMyOrder;

/**
 * Created by prakash on 18/08/15.
 */
public class adapterordertransaction extends BaseAdapter {


    private Activity currentactivity;
    private LayoutInflater inflater;
    Integer x=0,y=0;
    Integer viec=1;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
    private TreeSet mSeparatorsSet = new TreeSet();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat dateFormat_display = new SimpleDateFormat("dd");
    DateFormat dateFormat_letterIn_day_display = new SimpleDateFormat("EEE",Locale.getDefault());

    private ArrayList<String> HeaderSectionArray = new ArrayList<String>();
  String monthorderheader=" ",monthorderSection="";
    private List<M_orderlist> orderitem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public adapterordertransaction(Activity currentactivity, List<M_orderlist> orderlists, ArrayList<String> HeaderSecArray, Integer viewcount) {
        this.currentactivity = currentactivity;
        this.orderitem = orderlists;
        this.HeaderSectionArray=HeaderSecArray;
        this.viec=viewcount;
    }




    @Override
    public int getCount() {
        return orderitem.size()+(HeaderSectionArray.size());
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
    public int getItemViewType(int position) {
        return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        int i=1;
        do  {
            viec++;
            return viec;
        }while(i<=(orderitem.size()+(HeaderSectionArray.size())));
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) currentactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        // prakash
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        // getting Invoice data for the row


        try {

            if (convertView == null) {

                if (HeaderSectionArray.size() > y && !monthorderheader.equals(monthorderSection)) {

                    convertView = inflater.inflate(R.layout.header_for_order, null);
                    TextView header = (TextView) convertView.findViewById(R.id.text);
                    String montnu = HeaderSectionArray.get(y).toString();
                    monthorderheader=montnu.substring(0, 2);
                    String year=montnu.substring(3, 7);
                    String month=getMonth(Integer.parseInt(monthorderheader));
                    header.setText(month+" "+year);
                    y++;

                }

                if (x < orderitem.size()) {
                    M_orderlist O_orderlist = orderitem.get(x);
                    monthorderSection = O_orderlist.getOrderMonth();
                    if (monthorderheader.equals(monthorderSection) && convertView == null) {

                        if (O_orderlist != null) {
                            convertView = inflater.inflate(R.layout.refill_order_item, null);
                            TextView ref_OrderNo = (TextView) convertView.findViewById(R.id.ref_OrderNo);
                            TextView ref_OrderDate = (TextView) convertView.findViewById(R.id.ref_OrderDate);
                            ref_OrderNo.setText(O_orderlist.getOrderNo());

                            try {
                                Date current_date = Calendar.getInstance().getTime();
                                current_date = dateFormat.parse(O_orderlist.getOrderDate());
                                String dayLetter=(dateFormat_letterIn_day_display.format(current_date));
                                ref_OrderDate.setText(dateFormat_display.format(current_date)+"  "+dayLetter);
                            }catch(Exception e)
                            {

                            }

                            //ref_OrderDate.setText(O_orderlist.getOrderDate().substring(0, 10));
                            TextView ref_OrderPrice = (TextView) convertView.findViewById(R.id.ref_OrderPrice);

                            String sRupee = currentactivity.getResources().getString(R.string.Rs);
                            ref_OrderPrice.setText(sRupee + " " + O_orderlist.getNetAmount());
                            LinearLayout ll_refil_order_item = (LinearLayout) convertView.findViewById(R.id.ll_refil_order_item);
                            ll_refil_order_item.setTag(O_orderlist.getOrderId());
                            ll_refil_order_item.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    get_orderdetails(view.getTag().toString());
                                }
                            });
                        }
                        //monthorderSection = O_orderlist.getOrderMonth();
                        x++;
                    } else if (convertView == null) {
                        {

                            if (HeaderSectionArray.size() > y && !monthorderheader.equals(monthorderSection)) {

                                convertView = inflater.inflate(R.layout.header_for_order, null);
                                TextView header = (TextView) convertView.findViewById(R.id.text);
                                String montnu = HeaderSectionArray.get(y).toString();
                                monthorderheader=montnu.substring(0, 2);
                                String year=montnu.substring(3, 7);
                                String month=getMonth(Integer.parseInt(monthorderheader));
                                header.setText(month+" "+year);
                                y++;

                            }

                        }


                    }

                }
            }
        }catch(Exception e){
            convertView = inflater.inflate(R.layout.header_for_order, null);
        }

            //NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);

            //TextView ref_status = (TextView) convertView.findViewById(R.id.ref_status);
           //TextView ref_ExpectedDeliveryDate = (TextView) convertView.findViewById(R.id.ref_ExpectedDeliveryDate);



            //ref_status.setText(O_orderlist.getStatus());
            //ref_ExpectedDeliveryDate.setText(O_orderlist.getExpectedDeliveryDate());


            /*Button btnorderview = (Button) convertView.findViewById(R.id.btnorderview);
            btnorderview.setTag(O_orderlist.getOrderId());*/



           /* btnorderview.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    get_orderdetails(view.getTag().toString());
                }
            });*/



        return convertView;
    }

    private void get_orderdetails(String p_orderid) {
        Intent intent_refill = new Intent(currentactivity, refillitemForMyOrder.class);
        intent_refill.putExtra("orderid", p_orderid);
        currentactivity.startActivity(intent_refill);
    }
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

}


