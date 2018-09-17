package viroopa.com.medikart.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import viroopa.com.medikart.R;


public class MemberRowHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbNail;
    protected TextView memberName;
    protected LinearLayout relativelayoutMain;
    protected TextView Relation;


    public MemberRowHolder(View view) {
        super(view);
        this.thumbNail = (ImageView) view.findViewById(R.id.thumbnail);
        this.memberName = (TextView) view.findViewById(R.id.productname);
        this.relativelayoutMain = (LinearLayout) view.findViewById(R.id.relativeFirst);
        this.Relation = (TextView) view.findViewById(R.id.relation);
    }

}