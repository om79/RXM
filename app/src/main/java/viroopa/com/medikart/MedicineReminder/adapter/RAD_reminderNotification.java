package viroopa.com.medikart.MedicineReminder.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;


import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import viroopa.com.medikart.MedicineReminder.MRA_reminder_notification;
import viroopa.com.medikart.MedicineReminder.Model.mednotificationList;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.helper.SqliteMRHandler;

import static android.content.Context.MODE_PRIVATE;

public class RAD_reminderNotification extends RecyclerView.Adapter<recyclerRowHolder> implements      TimePickerDialog.OnTimeSetListener {


    private List<mednotificationList> feedItemList;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private Context mContext;
    private Activity act;
    private ProgressDialog pDialog;
    private String sMemberId;
    public static final int MRA_notifyID = 1234;
    AppController globalVariable;
    private TimePickerDialog dpd;
    Date current_date = Calendar.getInstance().getTime();
    DateFormat dateFormat_popup = new SimpleDateFormat("hh:mm a , MMM dd");
    private String picked_time="-99";
    SimpleDateFormat dateFormat_header = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat dateFormat_query_popup = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat dateFormat_reshedule = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat time_format = new SimpleDateFormat("HH:mm:ss");
    private  Integer  hour=0;
    private  Integer minutes=0;
    private SqliteMRHandler db;
    private ImageView img_first_part ;
    private ImageView img_second_part ;
    private String ns;
    final static  String SCHEDULED_DATE="sceduled_date";
    final static  String SCHEDULED_ID="schedule_id";
    private NotificationManager nMgr;
    private static String SNOOZE_COUNT="snooze_count";
    private int snooze_intervel;

    public RAD_reminderNotification(Context context,Activity act, List<mednotificationList> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        this.act=act;


        initImageLoader();
        pDialog = new ProgressDialog(mContext);
        globalVariable = (AppController)mContext.getApplicationContext();
        db = new SqliteMRHandler(mContext);
        SharedPreferences  pref = mContext.getSharedPreferences("Global", MODE_PRIVATE);
        snooze_intervel=Integer.parseInt(pref.getString(SNOOZE_COUNT,"5 minutes").replaceAll("\\D+",""));
    }

    @Override
    public recyclerRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.med_reminder_card_view, null);
        recyclerRowHolder mh = new recyclerRowHolder(v);
        mh.skip.setTag(R.id.key_view,v);
        mh.pop_taken.setTag(R.id.key_view,v);
        mh.setIsRecyclable(false);


        return mh;
    }

    @Override
    public void onBindViewHolder(final recyclerRowHolder o_recyclerRowHolder, int i) {

        if(i<feedItemList.size()) {
            int height = 0; //your textview height


            mednotificationList feedItem = feedItemList.get(i);

            o_recyclerRowHolder.Medicine_name.setText("  "+feedItem.getMedicineName());


            o_recyclerRowHolder.pop_taken.setTag(R.id.key_product_name,i);

            o_recyclerRowHolder.pop_reschedule.setTag(R.id.key_product_id,feedItem.getId());
            o_recyclerRowHolder.pop_taken.setTag(R.id.key_product_id,feedItem.getId());

            o_recyclerRowHolder.skip.setTag(R.id.key_product_id, feedItem.getId());
            o_recyclerRowHolder.skip.setTag(R.id.key_medicine_name, feedItem.getMedicineName());
            o_recyclerRowHolder.skip.setTag(R.id.key_product_qty, feedItem.getSchedule_date_time());
            o_recyclerRowHolder.skip.setTag(R.id.key_product_snooze_count, feedItem.getSnooze_count());


            add_image_views( o_recyclerRowHolder.img_lnr,feedItem.getImage_id(),feedItem.getF_color_id(),feedItem.getS_color_id(),new LinearLayout.LayoutParams(25,40));


            try {
                current_date = dateFormat_query_popup.parse(feedItem.getSchedule_date_time());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            o_recyclerRowHolder.Scheduledatetimetxt.setText(dateFormat_popup.format(current_date));
            o_recyclerRowHolder.pop_dosage.setText(" Take "+feedItem.getDoasge());

            o_recyclerRowHolder.skip.setText("Snooze");
            o_recyclerRowHolder.pop_reschedule.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String sch_id = (String) view.getTag(R.id.key_product_id);
                    select_time(sch_id);



                }
            });

            o_recyclerRowHolder.skip.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String sch_id = (String) view.getTag(R.id.key_product_id);
                    String med_name = (String) view.getTag(R.id.key_medicine_name);
                    String sch_date = (String) view.getTag(R.id.key_product_qty);
                    Integer snooze_count = (Integer) view.getTag(R.id.key_product_snooze_count);

                    snooze_medicine(med_name,snooze_count,sch_date,sch_id,view);

                }
            });

            o_recyclerRowHolder.pop_taken.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String sch_id = (String) view.getTag(R.id.key_product_id);
                    Integer positon = (Integer) view.getTag(R.id.key_product_name);
                    db.delete_from_notification_table(sch_id);
                    db.Update_Medicine_Schedule(sch_id, "T", dateFormat_query_popup.format(Calendar.getInstance().getTime()));
                    ConstData.create_json_from_table("reminder_schedule","E","",sch_id,sMemberId,dateFormat_query_popup.format(Calendar.getInstance().getTime()),mContext);
                    ((MRA_reminder_notification)mContext).fill_notification_list();
                    ((MRA_reminder_notification) mContext).removeListItem((View)view.getTag(R.id.key_view)); ;
                    ns = mContext.NOTIFICATION_SERVICE;
                    nMgr = (NotificationManager) mContext.getSystemService(ns);
                    nMgr.cancel(MRA_notifyID);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                mContext).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }









    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    private void showPdialog(String sMessage) {
        if (pDialog != null) {
            pDialog.setMessage(sMessage);
            pDialog.show();
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        //String time = hourOfDay+"h"+minute;


        final String sch_id = dpd.getArguments().getString(SCHEDULED_ID);


        picked_time = dpd.getArguments().getString(SCHEDULED_DATE)+" "+getTime_Format(hourOfDay,minute);

        try {
            if(picked_time.equals("-99"))
            {
                picked_time= dateFormat_query_popup.format(current_date);
            }
            db.update_Reschedule_time(sch_id,picked_time);
            db.delete_from_notification_table(sch_id);
            ((MRA_reminder_notification)mContext).fill_notification_list();
            ((MRA_reminder_notification) mContext).finish();
            ns = mContext.NOTIFICATION_SERVICE;
            nMgr = (NotificationManager) mContext.getSystemService(ns);
            nMgr.cancel(MRA_notifyID);
        } catch (Exception e) {
            ns = mContext.NOTIFICATION_SERVICE;
            nMgr = (NotificationManager) mContext.getSystemService(ns);
            nMgr.cancel(MRA_notifyID);
            e.toString();
        }

    }
    public String getTime_Format(int Hour, int Minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, Hour);
        cal.set(Calendar.MINUTE, Minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return  time_format.format(cal.getTime());
    }

    private void select_time(final String Schedule_id)
    {

        Calendar c = Calendar.getInstance();

        dpd = TimePickerDialog.newInstance(
                this,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
        );


        Bundle b= new Bundle();
        b.putString(SCHEDULED_DATE,dateFormat_reshedule.format(current_date));
        b.putString(SCHEDULED_ID,Schedule_id);
        dpd.setArguments(b);
        dpd.show(act.getFragmentManager(), "Datepickerdialog");

    }


 /*   public  void med_rem_reschedule(final String Schedule_id) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogview = inflater.inflate(R.layout.med_rem_onitem_click, null);
        current_date = Calendar.getInstance().getTime();

        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(mContext);
        //dialogbuilder.setTitle("Set Time and Quantity");

        dialogbuilder.setView(dialogview);
        final TimePicker timePkr = (TimePicker) dialogview.findViewById(R.id.timePicker1);
        final NumberPicker nbrselectdays= (NumberPicker) dialogview.findViewById(R.id.numberPicker3);
        nbrselectdays.setVisibility(View.GONE);
        final Button btnok = (Button) dialogview.findViewById(R.id.btnok);
        final Button btncancel = (Button) dialogview.findViewById(R.id.btncancel);
        final Dialog dialog =dialogbuilder.create();

        btnok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try {
                    if(picked_time.equals("-99"))
                    {
                        picked_time= dateFormat_query_popup.format(current_date);
                    }
                  db.update_Reschedule_time(Schedule_id,picked_time);
                    db.delete_from_notification_table(Schedule_id);
                    ((MRA_reminder_notification)mContext).fill_notification_list();
                } catch (Exception e) {
                    e.toString();
                }

                dialog.dismiss();

            }


        });

        btncancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                dialog.dismiss();
            }


        });
        timePkr.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                hour = hourOfDay;
                minutes = minute;




                picked_time = dateFormat_reshedule.format(current_date)+" "+hourOfDay + ":" + minute+":00";





            }
        });


        timePkr.setIs24HourView(true);

        dialog.show();


    }*/
    private void add_image_views(LinearLayout lnr, Integer Image_id, Integer f_color_id, Integer s_color_id, LinearLayout.LayoutParams customparam )
    {
        lnr.removeAllViews();
        img_first_part = new ImageView(mContext);
        img_second_part = new ImageView(mContext);
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
                img_first_part.setImageResource(ConstData.Medicine_array1[Image_id]);
                img_first_part.setColorFilter(ConstData.colr_array[f_color_id], PorterDuff.Mode.MULTIPLY);
                lnr.addView(img_first_part);
            }
        }



    }
    private void snooze_medicine(String Medname,Integer Snooze_count,String Schedule_date,String Schedule_id,View v)
    {
        try {
            current_date = dateFormat_query_popup.parse(Schedule_date);
        } catch (ParseException e) {
            e. printStackTrace();
        }
        if(Snooze_count>=3)
        {
            //send sms
            String msg="Hello Your friend does not take medicine "+Medname+" on time "+Schedule_date+".It may effect his health.";
            send_sms(msg);
        }

        db.Snooze_Medicine_Schedule(Schedule_id,dateFormat_query_popup.format(date_addMinutes(current_date, 5)),Snooze_count+1);
        db.delete_from_notification_table(Schedule_id);
        ConstData.create_json_from_table("reminder_schedule","E","",Schedule_id,sMemberId,dateFormat_query_popup.format(date_addMinutes(current_date, 5)),mContext);
        Toast.makeText(mContext, "Snoozed for "+snooze_intervel+ "mins", Toast.LENGTH_SHORT).show();
        ns = mContext.NOTIFICATION_SERVICE;
        nMgr = (NotificationManager) mContext.getSystemService(ns);
        nMgr.cancel(MRA_notifyID);
        ((MRA_reminder_notification)mContext).fill_notification_list();
        ((MRA_reminder_notification) mContext).removeListItem((View)v.getTag(R.id.key_view)); ;
       // close_all_activity(mContext);
      //  MRA_reminder_notification.objsecondActivity.closeActivity();
    }
    public Date date_addMinutes(Date date, int Minute)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, Minute);
        return c.getTime();
    }
    private void send_sms(String Msg) {
        SmsManager sms = SmsManager.getDefault();

        Cursor cursor_all_medicine = db.get_all_pill_buddyr(sMemberId);


        if ((cursor_all_medicine != null) || (cursor_all_medicine.getCount() > 0)) {

            if (cursor_all_medicine.moveToFirst()) {
                do {
                    String phone_number=cursor_all_medicine.getString(cursor_all_medicine.getColumnIndex("reminder_phone_no"));
                    if(phone_number!=null)
                    {
                        if(StringUtils.isNumeric(phone_number))
                        {
                            sms.sendTextMessage(phone_number, null, Msg, null, null);
                        }
                    }

                } while (cursor_all_medicine.moveToNext());
            }


        }
    }
    private  void close_all_activity(Context ctx)
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

}
