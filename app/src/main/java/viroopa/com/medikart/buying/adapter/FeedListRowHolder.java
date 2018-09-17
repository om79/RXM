package viroopa.com.medikart.buying.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import viroopa.com.medikart.R;

public class FeedListRowHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbnail;
    protected TextView productname;
    protected RelativeLayout LnrClick;
    protected TextView formName;
    protected TextView packSize;
    protected TextView price;
    protected TextView group_heading,result_count;
    protected TextView btn_showall;
    protected TextView catalog;
    protected LinearLayout lnr_heading,footer;
    protected RelativeLayout rl_product_item,main_data_layout;
    protected TextView line_view;
  //  protected TextView space;


    public FeedListRowHolder(View view) {
        super(view);
        this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        this.productname = (TextView) view.findViewById(R.id.productname);
        this.LnrClick = (RelativeLayout) view.findViewById(R.id.product_item);
        this.formName = (TextView) view.findViewById(R.id.formName);
        this.packSize = (TextView) view.findViewById(R.id.packSize);
        this.price = (TextView) view.findViewById(R.id.price);

        this.group_heading = (TextView) view.findViewById(R.id.group_heading);
        this.result_count = (TextView) view.findViewById(R.id.result_count);
        this.lnr_heading = (LinearLayout) view.findViewById(R.id.heading);
        this.btn_showall = (TextView) view.findViewById(R.id.btn_showall);
        this. catalog= (TextView) view.findViewById(R.id.catalog);
        this.main_data_layout = (RelativeLayout) view.findViewById(R.id.main_data_layout);
        this.rl_product_item = (RelativeLayout) view.findViewById(R.id.product_item);
        this.footer= (LinearLayout) view.findViewById(R.id.footer);
        this.line_view= (TextView) view.findViewById(R.id.view2);
      //  this.space= (TextView) view.findViewById(R.id.space);
    }


}