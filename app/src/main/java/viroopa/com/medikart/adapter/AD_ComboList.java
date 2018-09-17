package viroopa.com.medikart.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import viroopa.com.medikart.R;
import viroopa.com.medikart.model.M_objectItem;

/**
 * Created by prakash on 18/08/15.
 */
public class AD_ComboList extends ArrayAdapter<M_objectItem> {

    //public class ArrayAdapterItem extends ArrayAdapter<M_objectItem> {

    Context mContext;
    int layoutResourceId;
    M_objectItem data[] = null;
    M_objectItem data1[] = null;


    public AD_ComboList(Context mContext, int layoutResourceId, M_objectItem[] data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        //this.data = new data[data.length()];
        this.data = data;
        this.data1 = data;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {



        /*
31
         * The convertView argument is essentially a "ScrapView" as described is Lucas post
32
         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
33
         * It will have a non-null value when ListView is asking you recycle the row layout.
34
         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
35
         */
        if(convertView==null){

            // inflate the layout

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            convertView = inflater.inflate(layoutResourceId, parent, false);

        }

        // object item based on the position

        M_objectItem mobjectItem = data[position];

        // get the TextView and then set the text (item name) and tag (item ID) values

        TextView textViewItem = (TextView) convertView.findViewById(R.id.product_name);
        textViewItem.setText(mobjectItem.itemName);
        textViewItem.setTag(mobjectItem.itemId);

        return convertView;



    }

}

