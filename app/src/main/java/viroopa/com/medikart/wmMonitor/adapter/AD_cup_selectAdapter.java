package viroopa.com.medikart.wmMonitor.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import viroopa.com.medikart.R;


/**
 * Created by prakash on 18/08/15.
 */
public class AD_cup_selectAdapter extends BaseAdapter {


    private String sMemberId,sCardId;

    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private ProgressDialog pDialog;


    private Activity currentactivity;
    private LayoutInflater inflater;
    private String[]Names;
    private int[]ImagNames;



    public AD_cup_selectAdapter(Activity currentactivity, String[]Name, int[]imgName) {
        this.currentactivity = currentactivity;
        this.ImagNames = imgName;
        this.Names = Name;
        pDialog = new ProgressDialog(currentactivity);
        pDialog.setCancelable(false);
    }

    @Override
    public int getCount() {
        return ImagNames.length;
    }

    @Override
    public Object getItem(int location) {
        return ImagNames[location];
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
            convertView = inflater.inflate(R.layout.wm_cupsize_select_list, null);
        }
        initImageLoader();
            TextView productname = (TextView) convertView.findViewById(R.id.editText6);
        productname.setText(Names[position]);
            ImageView thumbnail = (ImageView) convertView.findViewById(R.id.imageView5);

       thumbnail.setImageResource(ImagNames[position]);
            //    imageLoader.displayImage(currentactivity.getResources().getResourceName(ImagNames[position]), thumbnail);


        return convertView;
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

