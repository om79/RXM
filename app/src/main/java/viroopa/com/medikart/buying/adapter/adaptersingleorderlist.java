package viroopa.com.medikart.buying.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.DecimalFormat;
import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.buying.GeneralProducyDetails;
import viroopa.com.medikart.buying.model.M_singleorder_detail;
import viroopa.com.medikart.buying.refillitem;

/**
 * Created by prakash on 19/08/15.
 */
public class adaptersingleorderlist extends BaseAdapter {

    private String sOrderId,sMemberId;
    private Activity currentactivity;
    private LayoutInflater inflater;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private Integer statusImg;
    private ProgressDialog pDialog;

    private int nTotalCartCount,nCartId;
    static int mNotifCount = 0;

    private List<M_singleorder_detail> singleorderitem;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public adaptersingleorderlist(Activity currentactivity, List<M_singleorder_detail> singleorderitem, String MemberId, String OrderId, Integer statusImage) {
        this.currentactivity = currentactivity;
        this.singleorderitem = singleorderitem;
        this.sMemberId =  MemberId;
        this.sOrderId =  OrderId;
        this.statusImg=statusImage;
        pDialog = new ProgressDialog(currentactivity);
    }

    @Override
    public int getCount() {
        return singleorderitem.size();
    }

    @Override
    public Object getItem(int location) {
        return singleorderitem.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        initImageLoader();


        if (inflater == null)
            inflater = (LayoutInflater) currentactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (statusImg==0)
        {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.order_placed_medines_list, null);
            }


            RelativeLayout Main_rel=(RelativeLayout)  convertView.findViewById(R.id.Main_rel);
            TextView refi_product = (TextView) convertView.findViewById(R.id.Medicine_Name);
            TextView refi_packsize = (TextView) convertView.findViewById(R.id.packSize);
            TextView refi_amount = (TextView) convertView.findViewById(R.id.price);

           ImageView refi_thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);




            M_singleorder_detail o_singleorder_detail = singleorderitem.get(position);
            if (o_singleorder_detail != null) {

                if (o_singleorder_detail.getImageUrl() != null) {
                    imageLoader.displayImage(o_singleorder_detail.getImageUrl(), refi_thumbnail);
                }

                refi_product.setText(o_singleorder_detail.getProduct());
                refi_packsize.setText("Pack size : " + o_singleorder_detail.getPacksize());

                Double double_price=0.00;
                DecimalFormat df = new DecimalFormat("#.00");
                if(o_singleorder_detail.getQuantity()!=null &&o_singleorder_detail.getQuantity()!="" && o_singleorder_detail.getPrice()!=null &&o_singleorder_detail.getPrice()!="") {
                    double_price =  Integer.parseInt(o_singleorder_detail.getQuantity()) * Double.parseDouble(o_singleorder_detail.getPrice());
                }


                //refi_amount.setText(sRupee + " " + nPrice.toString());
                refi_amount.setText("Rs." + " " + double_price.toString());
                refi_amount.setTextColor(Color.parseColor("#ed7d13"));
                //refi_product.setTextColor(Color.parseColor("#484848"));
               // refi_amount.setPadding(0,15,0,0);

                if(currentactivity.getClass().getSimpleName().equals("GeneralProducyDetails"))
                {

                    Main_rel.setTag(o_singleorder_detail.getProductId());

                    refi_amount.setText("Rs." + " " + o_singleorder_detail.getPrice());
                    refi_packsize.setVisibility(View.GONE);
                    refi_product.setTextColor(currentactivity.getResources().getColor(R.color.colorPrimary));
                    refi_amount.setPadding(0,10,0,10);

                    Main_rel.setOnClickListener( new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent Intenet_change = new Intent(currentactivity,GeneralProducyDetails.class);
                            Intenet_change.putExtra("Generalproductid",(String) v.getTag());
                            currentactivity.startActivity(Intenet_change);

                        }
                    });

                }


            }

        }else {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.refill_detailed_item, null);
            }

            // prakash
            //if (imageLoader == null)
            //    imageLoader = AppController.getInstance().getImageLoader();


            // getting Invoice data for the row
            M_singleorder_detail o_singleorder_detail = singleorderitem.get(position);
            if (o_singleorder_detail != null) {

                //NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
                TextView refi_product = (TextView) convertView.findViewById(R.id.refi_product);
                TextView refi_packsize = (TextView) convertView.findViewById(R.id.refi_packsize);
                TextView refi_amount = (TextView) convertView.findViewById(R.id.refi_amount);
                TextView refi_Qty = (TextView) convertView.findViewById(R.id.refi_Qty);

                String sRupee = currentactivity.getResources().getString(R.string.Rs);

                refi_product.setText(o_singleorder_detail.getProduct());
                refi_packsize.setText("Pack size : " + o_singleorder_detail.getPacksize());
                Float nPrice = Integer.parseInt(o_singleorder_detail.getQuantity()) * Float.parseFloat(o_singleorder_detail.getPrice());
                //refi_amount.setText(sRupee + " " + nPrice.toString());
                refi_amount.setText(sRupee + " " + String.format("%.0f", nPrice));
                refi_Qty.setText("Qty : " +o_singleorder_detail.getQuantity());

/*            Button btnaddtocart = (Button) convertView.findViewById(R.id.btnaddtocart);
            btnaddtocart.setTag(o_singleorder_detail.getProductId());*/

//            ImageView btnplus = (ImageView) convertView.findViewById(R.id.btnplus);
//            ImageView btnminus = (ImageView) convertView.findViewById(R.id.btnminus);
                final CheckBox btnaddcart = (CheckBox) convertView.findViewById(R.id.btnaddcart);


                convertView.setTag(R.id.key_product_id, o_singleorder_detail.getProductId());
                convertView.setTag(R.id.key_product_qty, o_singleorder_detail.getQuantity());
                btnaddcart.setTag(R.id.key_product_id, o_singleorder_detail.getProductId());
//            btnplus.setTag(o_singleorder_detail.getProductId());
//            btnminus.setTag(o_singleorder_detail.getProductId());
                btnaddcart.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        String gt_Product_id = (String) btnaddcart.getTag(R.id.key_product_id);
                        cb.setChecked(cb.isChecked());
                        ((refillitem)currentactivity).fill_checked_data(cb,gt_Product_id);
                    }
                });

              /*  btnaddcart.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        String gt_Product_id = (String) view.getTag(R.id.key_product_id);
                        String gt_Product_qty = (String) view.getTag(R.id.key_product_qty);
                       // get_add_to_cart(gt_Product_id, gt_Product_qty);
                    }
                });*/

              /*  btnaddcart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        String gt_Product_id = (String) buttonView.getTag(R.id.key_product_id);
                        //((refillitem)currentactivity).fill_checked_data(btnaddcart,gt_Product_id);
                    }
                });*/
                btnaddcart.setOnCheckedChangeListener(null);
            }



/*            btnplus.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    get_plus_minus("P", view.getTag().toString());
                }
            });

            btnminus.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    get_plus_minus("M", view.getTag().toString());
                }
            });*/
        }

        return convertView;
    }



    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            //pDialog = null;
        }
    }

    private void showPdialog(String sMessage) {
        if (pDialog != null) {
            pDialog.setMessage(sMessage);
            pDialog.show();
        }
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(currentactivity)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                currentactivity).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }
}


