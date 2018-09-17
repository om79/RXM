package viroopa.com.medikart.MedicineReminder.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.util.List;

import viroopa.com.medikart.MedicineReminder.MRA_MedicineDetails;
import viroopa.com.medikart.MedicineReminder.Model.m_medicine_list;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;

/**
 * Created by prakash on 18/08/15.
 */
public class AD_AllPillBuddyList extends BaseAdapter {


    private String sMemberId,sCardId;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private ProgressDialog pDialog;
    private  String imgarrayName="";
    private Integer ImgResourceId;
    DisplayImageOptions options;
    private Activity currentactivity;
    private LayoutInflater inflater;

    private List<m_medicine_list> medicneList;

    public AD_AllPillBuddyList(Activity currentactivity, List<m_medicine_list> MedList) {
        this.currentactivity = currentactivity;
        this.medicneList = MedList;
        pDialog = new ProgressDialog(currentactivity);
        pDialog.setCancelable(false);

        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(1000)) //rounded corner bitmap
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();


    }

    @Override
    public int getCount() {
        return medicneList.size();
    }

    @Override
    public Object getItem(int location) {
        return medicneList.get(location);
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
            convertView = inflater.inflate(R.layout.pillbuddy_list, null);
        }
        initImageLoader();
        // prakash
        //if (imageLoader == null)
        //    imageLoader = AppController.getInstance().getImageLoader();



        // getting Invoice data for the row
        m_medicine_list O_med = medicneList.get(position);
        if (O_med != null) {

            //NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);

            LinearLayout medlistlenear = (LinearLayout) convertView.findViewById(R.id.medlistlenear);
            TextView txtmedicine = (TextView) convertView.findViewById(R.id.txtmedicine);
            ImageView profile_pic = (ImageView) convertView.findViewById(R.id.profile_pic);

            //txtday.setText(O_med.getDate());

          String id=O_med.getMedicine_Id();
            medlistlenear.setTag(R.id.key_product_id, O_med.getMedicine_Id());
            medlistlenear.setTag(R.id.key_product_name, O_med.getMedicine_Name());


            if(O_med.getPill_buddy_image_name()!=null) {
                load_profile_pic(O_med.getPill_buddy_image_name(), profile_pic);
            }

            txtmedicine.setText(O_med.getMedicine_Name());

            convertView.setBackgroundColor(position % 2 == 0 ? Color.parseColor("#dfe8f9") : Color.WHITE);

            medlistlenear.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                  }
              });
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
private void gotMEdicineDetails(String remider_id,String MedName,String Image_Id)
{
    Intent intent = new Intent(currentactivity, MRA_MedicineDetails.class);
    intent.putExtra("Reminder_id",remider_id);
    intent.putExtra("Medicine_name",MedName);
    intent.putExtra("Image_id",Image_Id);
    currentactivity.startActivity(intent);
}

    private void load_profile_pic(String imgeName,ImageView thumbNail)
    {
        if (imgeName.startsWith("avtar")) {
            Resources res = currentactivity.getResources();

            int resourceId = res.getIdentifier(imgeName, "drawable", currentactivity.getPackageName());
            Drawable drawable = res.getDrawable(resourceId);
           thumbNail.setImageDrawable(drawable);
        } else {
            String iconsStoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + AppConfig.IMAGE_DIRECTORY_NAME;

            File pathfile = new File(iconsStoragePath + File.separator + imgeName);

            //Bitmap mybitmap = BitmapFactory.decodeFile(pathfile.getPath());
            if (pathfile.exists()) {
                //thumbNail.setImageBitmap(mybitmap);
                imageLoader.displayImage("file://" + pathfile.getPath(), thumbNail,options);
            } else {
              /*  try {
                    imageLoader.displayImage(feedItem.getImageurl(), thumbNail);
                } catch (Exception e) {
                }*/
            }

        }
    }

}

