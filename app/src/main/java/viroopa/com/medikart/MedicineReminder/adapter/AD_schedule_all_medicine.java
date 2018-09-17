package viroopa.com.medikart.MedicineReminder.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;


import java.util.List;

import viroopa.com.medikart.MedicineReminder.MRA_MedicineDetails;
import viroopa.com.medikart.MedicineReminder.MRA_Schedule_All_Medicine;
import viroopa.com.medikart.MedicineReminder.MRA_reminder_notification;
import viroopa.com.medikart.MedicineReminder.Model.m_medicine_list;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.helper.SqliteMRHandler;

/**
 * Created by prakash on 18/08/15.
 */
public class AD_schedule_all_medicine extends BaseAdapter {

    private static final String TAG = AD_schedule_all_medicine.class.getSimpleName();

    private Activity currentactivity;
    private LayoutInflater inflater;

    private  String imgarrayName="";
    private Integer ImgResourceId;
    private ImageView img_first_part ;
    private ImageView img_second_part ;
    private String sMemberid;


    private List<m_medicine_list> medicine_list;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private SqliteMRHandler db_mr;

    public AD_schedule_all_medicine(Activity currentactivity, List<m_medicine_list> medicinelist) {
        this.currentactivity = currentactivity;
        this.medicine_list = medicinelist;

        db_mr = new SqliteMRHandler(currentactivity);

    }

    @Override
    public int getCount() {
        return medicine_list.size();
    }

    @Override
    public Object getItem(int location) {
        return medicine_list.get(location);
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
            convertView = inflater.inflate(R.layout.med_rem_all_medicine_list, null);
        }
        // prakash
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        SharedPreferences pref = currentactivity.getSharedPreferences("Global", currentactivity.MODE_PRIVATE);
        sMemberid = pref.getString("memberid", "");

        m_medicine_list O_medicine_list = medicine_list.get(position);
        if (O_medicine_list != null) {

            TextView medicine_name = (TextView) convertView.findViewById(R.id.medicine_name);
            LinearLayout thumbnail = (LinearLayout) convertView.findViewById(R.id.thumbnail);
            LinearLayout medicine_item = (LinearLayout) convertView.findViewById(R.id.medicine_item);
            ImageView img_delete= (ImageView) convertView.findViewById(R.id.img_delete);

            medicine_item.setTag(R.id.key_product_id, O_medicine_list.getMedicine_Id());
            img_delete.setTag(R.id.key_product_id, O_medicine_list.getMedicine_Id());
            medicine_item.setTag(R.id.key_product_name, O_medicine_list.getMedicine_Name());

            img_delete.setTag(R.id.key_product_id, O_medicine_list.getMedicine_Id());



            medicine_name.setText(O_medicine_list.getMedicine_Name());

            LinearLayout rl_medicine_item = (LinearLayout) convertView.findViewById(R.id.medicine_item);
            rl_medicine_item.setTag(O_medicine_list.getMedicine_Id());

            add_image_views( thumbnail,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),new LinearLayout.LayoutParams(20,40));


            medicine_item.setTag(R.id.key_MemberImage, O_medicine_list.getImd_id());
            medicine_item.setTag(R.id.key_first_color_id,O_medicine_list.getFirst_color_id());
            medicine_item.setTag(R.id.key_second_color_id,O_medicine_list.getSecond_color_id());


          //  medicine_item.setTag(R.id.key_MemberImage, String.valueOf(ImgResourceId));
           // thumbnail.setImageResource(ImgResourceId);

            img_delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    delete_medicine((String) view.getTag(R.id.key_product_id));
                }
            });

            rl_medicine_item.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    gotMEdicineDetails((String) view.getTag(R.id.key_product_id), (String) view.getTag(R.id.key_product_name), (Integer) view.getTag(R.id.key_MemberImage),(Integer) view.getTag(R.id.key_first_color_id),(Integer) view.getTag(R.id.key_second_color_id));
                }
            });
        }
        return convertView;
    }

    private void gotMEdicineDetails(String remider_id,String MedName,Integer Image_Id,Integer f_color_id,Integer s_color_id)
    {
        Intent intent = new Intent(currentactivity, MRA_MedicineDetails.class);
        intent.putExtra("Reminder_id",remider_id);
        intent.putExtra("Medicine_name",MedName);
        intent.putExtra("Image_id",Image_Id);
        intent.putExtra("f_color_id",f_color_id);
        intent.putExtra("s_color_id",s_color_id);
        currentactivity.startActivity(intent);
    }

    private void delete_medicine(final String p_id) {


        LayoutInflater inflater = LayoutInflater.from(currentactivity);
        AlertDialog.Builder builder = new AlertDialog.Builder(currentactivity);
        final View dialogview = inflater.inflate(R.layout.delete_medicine_master, null);
        final ListView lv = (ListView)dialogview. findViewById(R.id.list_view);

        builder.setView(dialogview);
        final Dialog dlg= builder.create();
        // Adding items to listview
        final Button okbtn = (Button) dialogview.findViewById(R.id.btnok);
        final Button btncancel = (Button) dialogview.findViewById(R.id.btncancel);




        btncancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                dlg.cancel();
            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                String reminder_id="0";

                Cursor cursor_remid=db_mr.get_reminder_id_of_Medicine(p_id,sMemberid);



                if ((cursor_remid != null) || (cursor_remid.getCount()> 0)) {
                    if (cursor_remid.moveToFirst()) {
                        do {
                            reminder_id=cursor_remid.getString(cursor_remid.getColumnIndex("reminder_id"));
                            db_mr.Delete_Medicine_master_details(reminder_id,sMemberid);
                        } while (cursor_remid.moveToNext());
                    }
                }

                db_mr.Delete_Medicine_master(p_id,sMemberid);

                ((MRA_Schedule_All_Medicine)currentactivity). get_all_medicine();
                dlg.cancel();

            }
        });

        dlg.show();
    }

    private void add_image_views(LinearLayout lnr, Integer Image_id, Integer f_color_id, Integer s_color_id, LinearLayout.LayoutParams customparam )
    {
        lnr.removeAllViews();
        img_first_part = new ImageView(currentactivity);
        img_second_part = new ImageView(currentactivity);
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


