package viroopa.com.medikart.MedicineReminder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import viroopa.com.medikart.R;


public class recyclerRowHolder extends RecyclerView.ViewHolder {



    protected TextView Scheduledatetimetxt;
    protected TextView pop_dosage;
    protected LinearLayout img_lnr;
    protected TextView Refill_reminder;
    protected Button pop_reschedule;
    protected Button pop_taken;
    protected Button skip;
    protected LinearLayout status_layout;
    protected ImageView img_status;
    protected TextView status_txt,Medicine_name;


    public recyclerRowHolder(View view) {
        super(view);


        this.pop_dosage= (TextView) view.findViewById(R.id.pop_dosage);
        this.Medicine_name= (TextView) view.findViewById(R.id.Medicine_name);
        this.Scheduledatetimetxt= (TextView) view.findViewById(R.id.Scheduledatetimetxt);
        this.img_lnr= (LinearLayout) view.findViewById(R.id.img_lnr);
        this.Refill_reminder= (TextView) view.findViewById(R.id.pop_txt3);
        this.pop_reschedule= (Button) view.findViewById(R.id.pop_reschedule);
        this.pop_taken= (Button) view.findViewById(R.id.pop_taken);
        this.skip= (Button) view.findViewById(R.id.skip);
        this.status_layout= (LinearLayout) view.findViewById(R.id.status_layout);
        this.img_status= (ImageView) view.findViewById(R.id.img_status);
        this.status_txt= (TextView) view.findViewById(R.id.status_txt);
    }


}