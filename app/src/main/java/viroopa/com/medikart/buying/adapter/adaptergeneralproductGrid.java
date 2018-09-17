package viroopa.com.medikart.buying.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.buying.GeneralProducyDetails;
import viroopa.com.medikart.buying.model.productlist;


public class adaptergeneralproductGrid extends BaseAdapter {


    private Context context;
    private List<productlist> LProductHeader;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private LayoutInflater inflater;

    public adaptergeneralproductGrid(Context context, List<productlist> cproductlist) {

        this.context = context;
        this.LProductHeader = cproductlist;
        initImageLoader();
    }

    @Override
    public int getCount() {

        // Number of times getView method call depends upon gridValues.length
        return LProductHeader.size();
    }

    @Override
    public Object getItem(int position) {

        return LProductHeader.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_view_general_products, parent,false);
        }




            ImageView img_pro=(ImageView)convertView.findViewById(R.id.img_pro);
            TextView txt_pro_name=(TextView)convertView.findViewById(R.id.txt_pro_name);
            TextView txt_pro_price=(TextView)convertView.findViewById(R.id.txt_pro_price);

            productlist oproductheader = LProductHeader.get(position);

            convertView.setTag(oproductheader.getId());

            txt_pro_name.setText(oproductheader.getProductname());
            txt_pro_price.setText("Rs."+oproductheader.getPrice());

            imageLoader.displayImage(oproductheader.getImagePath(), img_pro);

        convertView.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent Intenet_change = new Intent(context,GeneralProducyDetails.class);
                Intenet_change.putExtra("Generalproductid",(String) v.getTag());
                context.startActivity(Intenet_change);
            }
        });

        return convertView;
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }
}

