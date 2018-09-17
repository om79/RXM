package viroopa.com.medikart.MedicineReminder.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import viroopa.com.medikart.MedicineReminder.MRA_session_wise_medicines;
import viroopa.com.medikart.MedicineReminder.Model.mednotificationList;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.helper.SqliteMRHandler;

public class RAD_sessionMedicines extends RecyclerView.Adapter<recyclerRowHolder> {


    private List<mednotificationList> feedItemList;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private Context mContext;
    private ProgressDialog pDialog;
    private String sMemberId;
    AppController globalVariable;
    Date current_date = Calendar.getInstance().getTime();
    DateFormat dateFormat_popup = new SimpleDateFormat("hh:mm a , MMM dd");
    private String picked_time="-99";
    SimpleDateFormat dateFormat_header = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat dateFormat_query_popup = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat dateFormat_reshedule = new SimpleDateFormat("yyyy-MM-dd");
    private  Integer  hour=0;
    private  Integer minutes=0;
    private SqliteMRHandler db;

    private ImageView img_first_part ;
    private  ImageView img_second_part ;
    public RAD_sessionMedicines(Context context, List<mednotificationList> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;

        initImageLoader();
        pDialog = new ProgressDialog(mContext);
        globalVariable = (AppController)mContext.getApplicationContext();
        db = new SqliteMRHandler(mContext);
    }

    @Override
    public recyclerRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.med_reminder_card_view, null);
        recyclerRowHolder mh = new recyclerRowHolder(v);
        mh.setIsRecyclable(false);


        return mh;
    }

    @Override
    public void onBindViewHolder(final recyclerRowHolder recyclerRowHolder, int i) {

        if(i<feedItemList.size()) {
            int height = 0; //your textview height

            mednotificationList feedItem = feedItemList.get(i);

            recyclerRowHolder.Medicine_name.setText("  " + feedItem.getMedicineName());



            recyclerRowHolder.pop_taken.setTag(R.id.key_product_name,i);

            recyclerRowHolder.pop_reschedule.setTag(R.id.key_product_id,feedItem.getId());
            recyclerRowHolder.pop_taken.setTag(R.id.key_product_id,feedItem.getId());
            recyclerRowHolder.skip.setTag(R.id.key_product_id, feedItem.getId());

            recyclerRowHolder.status_layout.setVisibility(View.VISIBLE);
            add_image_views( recyclerRowHolder.img_lnr,feedItem.getImage_id(),feedItem.getF_color_id(),feedItem.getS_color_id(),new LinearLayout.LayoutParams(25,40));


            if(feedItem.getStatus()!=null)
            {
                if(feedItem.getStatus().equals("T"))
                {
                    recyclerRowHolder.pop_taken.setVisibility(View.GONE);
                    recyclerRowHolder.pop_reschedule.setVisibility(View.GONE);
                    recyclerRowHolder.skip.findViewById(R.id.skip).setVisibility(View.GONE);
                    recyclerRowHolder.img_status.setImageResource(R.drawable.taken_green);
                    recyclerRowHolder.status_txt.setText("Taken");
                    recyclerRowHolder.status_txt.setTextColor(Color.GREEN);
                }else  if(feedItem.getStatus().equals("S"))
                {

                    recyclerRowHolder.pop_taken.setText("Undo");
                    recyclerRowHolder.pop_reschedule.setVisibility(View.GONE);
                    recyclerRowHolder.skip.setVisibility(View.GONE);
                    recyclerRowHolder.img_status.setImageResource(R.drawable.orangecircle);
                    recyclerRowHolder.status_txt.setText("Skipped");
                }

                else{
                    recyclerRowHolder.img_status.setImageResource(R.drawable.pending);
                    recyclerRowHolder.status_txt.setText("Pending");
                    recyclerRowHolder.status_txt.setTextColor(Color.RED);
                }
            }

            try {
                current_date = dateFormat_query_popup.parse(feedItem.getSchedule_date_time());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            recyclerRowHolder.Scheduledatetimetxt.setText(dateFormat_popup.format(current_date));
            recyclerRowHolder.pop_dosage.setText(" Take "+feedItem.getDoasge());


            recyclerRowHolder.pop_reschedule.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String sch_id = (String) view.getTag(R.id.key_product_id);
                    med_rem_reschedule(sch_id);


                }
            });

            recyclerRowHolder.pop_taken.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if(!recyclerRowHolder.pop_taken.getText().equals("Undo")) {
                        String sch_id = (String) view.getTag(R.id.key_product_id);
                        Integer positon = (Integer) view.getTag(R.id.key_product_name);
                        db.delete_from_notification_table(sch_id);
                        db.Update_Medicine_Schedule(sch_id, "T", dateFormat_query_popup.format(Calendar.getInstance().getTime()));
                        ConstData.create_json_from_table("reminder_schedule", "E", "", sch_id, sMemberId, dateFormat_query_popup.format(Calendar.getInstance().getTime()), mContext);

                        ((MRA_session_wise_medicines) mContext).fill_Medicine_list();
                    }else
                    {
                        String sch_id = (String) view.getTag(R.id.key_product_id);
                        Integer positon = (Integer) view.getTag(R.id.key_product_name);
                        db.delete_from_notification_table(sch_id);
                        db.Update_Medicine_Schedule(sch_id, "SCH", dateFormat_query_popup.format(Calendar.getInstance().getTime()));
                        ConstData.create_json_from_table("reminder_schedule", "E", "", sch_id, sMemberId, dateFormat_query_popup.format(Calendar.getInstance().getTime()), mContext);
                        ((MRA_session_wise_medicines) mContext).fill_Medicine_list();
                    }

                }
            });

            recyclerRowHolder.skip.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String sch_id = (String) view.getTag(R.id.key_product_id);
                    Integer positon = (Integer) view.getTag(R.id.key_product_name);
                    db.delete_from_notification_table(sch_id);
                    db.Update_Medicine_Schedule(sch_id, "S",dateFormat_query_popup.format(Calendar.getInstance().getTime()));
                    ConstData.create_json_from_table("reminder_schedule","E","",sch_id,sMemberId,dateFormat_query_popup.format(Calendar.getInstance().getTime()),mContext);

                    ((MRA_session_wise_medicines)mContext).fill_Medicine_list();

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


    public  void med_rem_reschedule(final String Schedule_id) {

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
                  db.update_Reschedule_time(Schedule_id, picked_time);
                    db.delete_from_notification_table(Schedule_id);
                    ((MRA_session_wise_medicines)mContext).fill_Medicine_list();

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


    }
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

}
