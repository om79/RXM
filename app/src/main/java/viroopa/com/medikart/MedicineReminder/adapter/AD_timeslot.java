package viroopa.com.medikart.MedicineReminder.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import viroopa.com.medikart.R;


/**
 * Created by Administrator on 28/Aug/2015.
 */
public class AD_timeslot extends CursorAdapter {

    public AD_timeslot(Context context, Cursor c, int flags) {

        super(context, c,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        //LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //View retView = inflater.inflate(R.layout.bp_monitor_history_item, parent, false);

        // return retView;

        return LayoutInflater.from(context).inflate(R.layout.medrem_timeslot, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views


        TextView timslot = (TextView) view.findViewById(R.id.mdtimeslot);
        timslot.setText(cursor.getString(cursor.getColumnIndexOrThrow("time_slots")  ));
        TextView timquantity = (TextView) view.findViewById(R.id.mdquantity);
        timquantity.setText(cursor.getString(cursor.getColumnIndexOrThrow("medicine_quantity")  ));






    }

    @Override
    public int getCount() {
        if (getCursor() == null) {
            return 0;
        }
        return super.getCount();
    }
}