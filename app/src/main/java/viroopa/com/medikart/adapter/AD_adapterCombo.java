package viroopa.com.medikart.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import viroopa.com.medikart.ObjectItem;
import viroopa.com.medikart.R;

import java.util.Locale;

/**
 * Created by prakash on 18/08/15.
 */
public class AD_adapterCombo extends ArrayAdapter<ObjectItem> {

    //public class ArrayAdapterItem extends ArrayAdapter<M_objectItem> {

    Context mContext;
    int layoutResourceId;
    ObjectItem data[] = null;
    ObjectItem data1[] = null;

    public AD_adapterCombo(Context mContext, int layoutResourceId, ObjectItem[] data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        //this.data = new data[data.length()];
        this.data = data;
        this.data1 = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        ObjectItem objectItem = data[position];

        TextView textViewItem = (TextView) convertView.findViewById(R.id.product_name);
        textViewItem.setText(objectItem.itemName);
        textViewItem.setTag(objectItem.itemId);
        convertView.setTag(objectItem.getEmail_id());

        return convertView;
    }
}

