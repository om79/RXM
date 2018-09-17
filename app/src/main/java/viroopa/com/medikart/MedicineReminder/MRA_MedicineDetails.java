package viroopa.com.medikart.MedicineReminder;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

import viroopa.com.medikart.R;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.helper.SqliteMRHandler;

import static viroopa.com.medikart.R.id.Dosage_unit_selected;
import static viroopa.com.medikart.R.id.coordinatorLayout;

public class MRA_MedicineDetails extends AppCompatActivity {
    private String remind_id;
    private String medicine_id;
    private SqliteMRHandler db_mr;
    private String Medicine_name, Schedule_id;
    private Integer Image_id, f_color_id, s_color_id;
    private TextView txtlasttaken, txtnextreminder, txtmedname;
    private ImageView tick;
    private LayoutInflater inflater;
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");
    private LinearLayout med_img;
    private Integer refill_Packsize = -99;
    private int Refill_flag = 0;
    private Integer No_of_days;
    Integer days_medicine_complete = 0;
    Integer total_dosage = 0;
    private Integer Total_No_Of_Units;
    private int packQnty = 1;
    private Button btntake, btnsuspend, btnrefill;
    private ImageView homeBtn;
    private String setDosageunit;
    private String sMemberId;
    private ImageView img_first_part;
    private ImageView img_second_part;
    private TextView txtdoctor, txtMedfriends, txtRefill;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    DateFormat date_without_time = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat dateFormat_popup = new SimpleDateFormat("hh.mm a , MMM dd");
    SimpleDateFormat dateFormat_query_popup = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date current_date = Calendar.getInstance().getTime();
    Date current_date_refill = Calendar.getInstance().getTime();
    Animation scale;
    private String SelDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_reminder_medicine_details);
        db_mr = new SqliteMRHandler(getApplicationContext());
        inflater = LayoutInflater.from(this);
        scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        scale.reset();


        txtlasttaken = (TextView) findViewById(R.id.txtlasttaken);
        txtnextreminder = (TextView) findViewById(R.id.txtnextreminder);
        med_img = (LinearLayout) findViewById(R.id.med_img);
        tick = (ImageView) findViewById(R.id.tick);
        txtmedname = (TextView) findViewById(R.id.txtmedname);
        btntake = (Button) findViewById(R.id.btntake);
        btnsuspend = (Button) findViewById(R.id.btnsuspend);
        btnrefill = (Button) findViewById(R.id.btnrefill);
        homeBtn = (ImageView) findViewById(R.id.homeBtn);
        txtdoctor = (TextView) findViewById(R.id.txtdoctor);
        txtMedfriends = (TextView) findViewById(R.id.txtMedfriends);
        txtRefill = (TextView) findViewById(R.id.txtRefill);
        get_intent();
        getData_medicine_detail();

        btntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_mr.Update_Medicine_Schedule(Schedule_id, "T", dateFormat_query_popup.format(Calendar.getInstance().getTime()));
                ConstData.create_json_from_table("reminder_schedule", "E", "", Schedule_id, sMemberId, dateFormat_query_popup.format(Calendar.getInstance().getTime()), MRA_MedicineDetails.this);
                btntake.setText("Taken");
                btntake.setEnabled(false);
                tick.setVisibility(View.VISIBLE);

            }
        });


        btnsuspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overDrinkWarning();


            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MRA_MedicineDetails.this, MRA_ReminderMain.class);
                startActivity(i);
                finish();
            }
        });
        btnrefill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Refill_flag == 0) {
                    refill_reminder_selection_dialog();
                } else {
                    Toast.makeText(MRA_MedicineDetails.this, "Refill Reminder is already set", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    private void get_intent() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
        Intent int_id = getIntent();
        medicine_id = int_id.getStringExtra("Reminder_id");
        Medicine_name = int_id.getStringExtra("Medicine_name");
        Image_id = int_id.getIntExtra("Image_id", 0);
        f_color_id = int_id.getIntExtra("f_color_id", 0);
        s_color_id = int_id.getIntExtra("s_color_id", -99);
        getSupportActionBar().setTitle(Medicine_name);
        txtmedname.setText(Medicine_name);

        //med_img.setImageResource(Integer.parseInt(Image_id));

        add_image_views(med_img, Image_id, f_color_id, s_color_id, new LinearLayout.LayoutParams(15, 30));
    }

    private void getData_medicine_detail() {
        try {
            Cursor cursor_remid = db_mr.get_reminder_id_of_Medicine(medicine_id, sMemberId);


            if ((cursor_remid != null) || (cursor_remid.getCount() > 0)) {
                if (cursor_remid.moveToFirst()) {
                    do {
                        remind_id = cursor_remid.getString(cursor_remid.getColumnIndex("reminder_id"));
                    } while (cursor_remid.moveToNext());
                }
            }
            if (remind_id != null && !remind_id.equals("null")) {
                get_all_reminder_data(remind_id);
            }else {
                Toast.makeText(MRA_MedicineDetails.this,"No reminder set for this medicine",Toast.LENGTH_SHORT).show();
            }



            Cursor cursor_session_last_taken = db_mr.get_med_detail_last_taken(remind_id, sMemberId);
            Cursor cursor_session_next_pending = db_mr.get_med_detail_next_pending(remind_id, sMemberId);

            if ((cursor_session_last_taken != null) || (cursor_session_last_taken.getCount() > 0)) {
                if (cursor_session_last_taken.moveToFirst()) {
                    do {
                        String Ltaken = cursor_session_last_taken.getString(cursor_session_last_taken.getColumnIndex("lastTaken"));

                        try {
                            current_date = dateFormat.parse(Ltaken);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Ltaken = dateFormat_popup.format(current_date);
                        txtlasttaken.setText("Last Taken : " + Ltaken);

                    } while (cursor_session_last_taken.moveToNext());
                }
            }

            if ((cursor_session_next_pending != null) || (cursor_session_next_pending.getCount() > 0)) {
                if (cursor_session_next_pending.moveToFirst()) {
                    //  do {
                    Schedule_id = cursor_session_next_pending.getString(cursor_session_next_pending.getColumnIndex("schedule_id"));
                    String Npending = cursor_session_next_pending.getString(cursor_session_next_pending.getColumnIndex("nextPending"));
                    if (Npending == null) {
                        Npending = "";
                    }
                    try {
                        current_date = dateFormat.parse(Npending);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Npending = dateFormat_popup.format(current_date);
                    txtnextreminder.setText("Next Reminder : " + Npending);
                    // } while (cursor_session_next_pending.moveToNext());
                }
            }
            Cursor status_crs = db_mr.get_schedule_on_reminder_id(remind_id, sMemberId);

            if ((status_crs != null) || (status_crs.getCount() > 0)) {
                if (status_crs.moveToFirst()) {
                    do {
                        String status = status_crs.getString(status_crs.getColumnIndex("status"));
                        if (status.equals("SUS")) {
                            btntake.setVisibility(View.GONE);
                            btnrefill.setVisibility(View.GONE);
                            txtnextreminder.setText("medicine is suspended");
                            txtlasttaken.setVisibility(View.GONE);
                            btnsuspend.setEnabled(false);

                        }
                    } while (status_crs.moveToNext());
                }
            }
        } catch (Exception e) {
            e.toString();
        }

    }

    private void get_all_reminder_data(String Reminder_id) {

        String Doctor_id = "0";
        String PillBuddy_id = "0";

        Cursor cursor_med_master = db_mr.get_med_master_data(Reminder_id, sMemberId);

        if (cursor_med_master.getCount() > 0) {
            if (cursor_med_master.moveToFirst()) {
                do {
                    Doctor_id = cursor_med_master.getString(cursor_med_master.getColumnIndex("doctor_id"));
                    PillBuddy_id = cursor_med_master.getString(cursor_med_master.getColumnIndex("medfriend_id"));
                    setDosageunit = cursor_med_master.getString(cursor_med_master.getColumnIndex("dosage_type"));
                    refill_Packsize = cursor_med_master.getInt(cursor_med_master.getColumnIndex("PackSize"));
                    No_of_days = cursor_med_master.getInt(cursor_med_master.getColumnIndex("schedule_duration_value"));
                    Refill_flag = cursor_med_master.getInt(cursor_med_master.getColumnIndex("RefillRem"));


                    try {
                        String s_date=date_without_time.format(dateFormat.parse( cursor_med_master.getString(cursor_med_master.getColumnIndex("schedule_start_date"))));
                        total_dosage  = db_mr.get_total_quantity(remind_id, s_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }





                    //  frequency= cursor_med_master.getInt(cursor_med_master.getColumnIndex("reminder_type_id"));
                    //  dosage= cursor_med_master.getInt(cursor_med_master.getColumnIndex("dosage_value"));

                    SelDate = cursor_med_master.getString(cursor_med_master.getColumnIndex("schedule_start_date"));
                    if (Refill_flag == 1) {
                        txtRefill.setText("Refill set on :" + cursor_med_master.getString(cursor_med_master.getColumnIndex("RefillDate")));
                    }

                } while (cursor_med_master.moveToNext());
            }

        }

        Cursor cursor_med_doctor = db_mr.get_top_scheduled_time_get_doctor_data(Doctor_id, sMemberId);
        if (cursor_med_doctor.getCount() > 0) {
            if (cursor_med_doctor.moveToFirst()) {
                do {
                    String doc_name = cursor_med_doctor.getString(cursor_med_doctor.getColumnIndex("DoctorName"));
                    if (doc_name != null && !doc_name.equals("")) {
                        txtdoctor.setText(doc_name);
                    } else {
                        txtdoctor.setText("Not synced yet");
                    }
                } while (cursor_med_doctor.moveToNext());

            }

        }

        Cursor get_top_scheduled_time_get_medfriend_data = db_mr.get_top_scheduled_time_get_medfriend_data(PillBuddy_id);

        if (get_top_scheduled_time_get_medfriend_data.getCount() > 0) {
            if (get_top_scheduled_time_get_medfriend_data.moveToFirst()) {
                do {
                    String med_friend_name = get_top_scheduled_time_get_medfriend_data.getString(get_top_scheduled_time_get_medfriend_data.getColumnIndex("reminder_friendname"));
                    if (med_friend_name != null && !med_friend_name.equals("")) {
                        txtMedfriends.setText(med_friend_name);
                    } else {
                        txtMedfriends.setText("Not synced yet");
                    }
                } while (get_top_scheduled_time_get_medfriend_data.moveToNext());
            }
        }

    }

    private void add_image_views(LinearLayout lnr, Integer Image_id, Integer f_color_id, Integer s_color_id, LinearLayout.LayoutParams customparam) {
        lnr.removeAllViews();
        img_first_part = new ImageView(this);
        img_second_part = new ImageView(this);
        customparam.gravity = Gravity.CENTER_VERTICAL;
        lnr.setGravity(Gravity.CENTER);
        if (s_color_id != null) {
            if (s_color_id != -99) {
                img_first_part.setLayoutParams(customparam);
                img_second_part.setLayoutParams(customparam);
                img_first_part.setImageResource(R.drawable.med_img_part_frst);
                img_second_part.setImageResource(R.drawable.med_img_part_second);
                img_first_part.setColorFilter(ConstData.colr_array[f_color_id], PorterDuff.Mode.MULTIPLY);
                img_second_part.setColorFilter(ConstData.colr_array[s_color_id], PorterDuff.Mode.MULTIPLY);
                lnr.addView(img_first_part);
                lnr.addView(img_second_part);
            } else {
                img_first_part.setImageResource(ConstData.Medicine_array1[Image_id]);
                img_first_part.setColorFilter(ConstData.colr_array[f_color_id], PorterDuff.Mode.MULTIPLY);
                lnr.addView(img_first_part);
            }
        }


    }


    private void overDrinkWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogview = inflater.inflate(R.layout.mra_suspend_dialog, null);
        final TextView btncancel = (TextView) dialogview.findViewById(R.id.btn_cancel);
        final Button btn_Suspend = (Button) dialogview.findViewById(R.id.btn_Suspend);
        builder.setView(dialogview);
        final Dialog dlg = builder.create();


        btn_Suspend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                dlg.dismiss();

                db_mr.suspend_Medicine_Schedule(remind_id, "SUS", dateFormat_query_popup.format(Calendar.getInstance().getTime()));
                ConstData.create_json_from_table("reminder_schedule", "E", "", remind_id, sMemberId, dateFormat_query_popup.format(Calendar.getInstance().getTime()), MRA_MedicineDetails.this);
                btnsuspend.setText("Suspended");
                btnsuspend.setEnabled(false);
                //   Toast.makeText(MRA_MedicineDetails.this, Medicine_name+" is suspended", Toast.LENGTH_LONG).show();

                Snackbar snackbar = Snackbar.make(med_img, Medicine_name + " is suspended", Snackbar.LENGTH_LONG);

                snackbar.show();
                btntake.setVisibility(View.GONE);
                btnrefill.setVisibility(View.GONE);
                txtnextreminder.setText("medicine is suspended");
                txtlasttaken.setVisibility(View.GONE);
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                dlg.dismiss();

            }
        });
        dlg.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dlg.show();
    }

    private void refill_reminder_selection_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialog = inflater.inflate(R.layout.mr_refill_dialog, null);
        builder.setView(dialog);
        final Dialog dlg = builder.create();


        final Button btnok = (Button) dialog.findViewById(R.id.btn_ok);
        final Button btncancel = (Button) dialog.findViewById(R.id.btncancel);
        final TextView refill_units = (TextView) dialog.findViewById(R.id.refill_units);
        final LinearLayout lnr_pack_size = (LinearLayout) dialog.findViewById(R.id.lnr_pack_size);
        final TextView txt_refill_packsize = (TextView) dialog.findViewById(R.id.txt_refill_packsize);
        final TextView txt_select_qty = (TextView) dialog.findViewById(R.id.txt_select_qty);
        final TextView edt_total_units = (TextView) dialog.findViewById(R.id.edt_total_units);
        final LinearLayout lnr_qty = (LinearLayout) dialog.findViewById(R.id.lnr_qty);
        final Button btnplus = (Button) dialog.findViewById(R.id.btnplus);
        final Button btnminus = (Button) dialog.findViewById(R.id.btnminus);
        final TextView Qty = (TextView) dialog.findViewById(R.id.Qty);


        refill_units.setText(setDosageunit);

        if (refill_Packsize != -99) {
            lnr_pack_size.setVisibility(View.VISIBLE);
            txt_refill_packsize.setText(refill_Packsize + "");
            edt_total_units.setEnabled(false);
            txt_select_qty.setVisibility(View.VISIBLE);
            lnr_qty.setVisibility(View.VISIBLE);
            edt_total_units.setText("" + refill_Packsize);
        } else {
            lnr_pack_size.setVisibility(View.GONE);
            txt_select_qty.setVisibility(View.GONE);
            lnr_qty.setVisibility(View.GONE);
        }


        btnok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (!edt_total_units.getText().toString().isEmpty()) {
                    Total_No_Of_Units = Integer.parseInt(edt_total_units.getText().toString());

                    String refill_date = calculate_refill_date(Total_No_Of_Units, setDosageunit, No_of_days);

                    db_mr.addMedRemMaster_update_refill_reminder(remind_id, 1, refill_date, Total_No_Of_Units, 0);

                    dlg.cancel();
                    get_intent();
                    getData_medicine_detail();
                } else {
                    Toast.makeText(MRA_MedicineDetails.this, "Please Enter Units", Toast.LENGTH_LONG).show();
                }
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clear_refill_reminder();
                dlg.cancel();
            }
        });


        btnplus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                packQnty++;
                Qty.setText(String.valueOf(packQnty));
                Total_No_Of_Units = refill_Packsize * packQnty;
                edt_total_units.setText("" + Total_No_Of_Units);
            }
        });

        btnminus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (packQnty > 1) {
                    packQnty--;
                    Qty.setText(String.valueOf(packQnty));
                    Total_No_Of_Units = refill_Packsize * packQnty;
                    edt_total_units.setText("" + Total_No_Of_Units);
                } else {
                    Toast.makeText(MRA_MedicineDetails.this, "minimum 1 Quantity", Toast.LENGTH_LONG).show();
                }

            }
        });


     /* dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
          @Override
          public void onDismiss(DialogInterface dialog) {
              clear_refill_reminder();
          }
      });*/
        dlg.setCancelable(false);

        dlg.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dlg.show();
        refill_units.clearAnimation();
        refill_units.startAnimation(scale);

    }

    private void clear_refill_reminder() {
        Total_No_Of_Units = 0;
        // refill_Packsize=-99;
        Refill_flag = 0;
    }

    private String calculate_refill_date(Integer total_quantity, String UOM, Integer no_of_days) {

        String Refill_Date = "";
        Integer RefillDays = 0;


      /*  if(UOM.equals("ml")||UOM.equals("drops"))
        {

        }else
        {*/


        if(total_dosage==0)
        {
            total_dosage=total_quantity;
        }
        days_medicine_complete = total_quantity / total_dosage;
        //}


        try {
            current_date_refill = dateFormat_query.parse(SelDate);

            String Medicine_completed_date = dateFormat_query.format(date_addDays(current_date_refill, days_medicine_complete));

            long diff = (dateFormat_query.parse(Medicine_completed_date).getTime() -
                    dateFormat_query.parse(SelDate).getTime());


            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long Medicine_completed_days = hours / 24;


            if (Medicine_completed_days <= 14) {
                RefillDays = 3;
            } else {
                RefillDays = 7;
            }

            Refill_Date = dateFormat_query.format(date_addDays(dateFormat_query.parse(Medicine_completed_date), -RefillDays));
        } catch (Exception e) {
        }
        return Refill_Date;
    }

    public Date date_addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

}
