package viroopa.com.medikart.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.model.M_memberlist;

import java.io.File;
import java.util.List;

public class AD_memberAdapter extends BaseAdapter {

    private ProgressDialog pDialog;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private  String sMemberId;
    AppController globalVariable;
    private Activity currentactivity;
    private LayoutInflater inflater;
    private  List<M_memberlist> memberList;
    DisplayImageOptions options;

    public AD_memberAdapter(Activity currentactivity, List<M_memberlist> MemberData, String MemberId) {
        this.currentactivity = currentactivity;
        this.sMemberId = MemberId;
        globalVariable = (AppController)currentactivity.getApplicationContext();
        this.memberList=MemberData;
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
        return memberList.size();
    }

    @Override
    public Object getItem(int location) {
        return memberList.get(location);
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
            convertView = inflater.inflate(R.layout.edit_add_member_item, null);
        }

        initImageLoader();

        M_memberlist O_member = memberList.get(position);

        if(O_member!= null){

            ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
            TextView memberName = (TextView) convertView.findViewById(R.id.productname);
            TextView Relation = (TextView) convertView.findViewById(R.id.relation);

            convertView.setTag(R.id.key_RelationShipId,O_member.getPatientId());
            convertView.setTag(R.id.key_MemberName,O_member.getMemberName());
            convertView.setTag(R.id.key_MemberImage,O_member.getImageurl());

            String ImagePath=O_member.getImageurl();
            String imgeName=ImagePath.substring(ImagePath.lastIndexOf('/') + 1, ImagePath.length());

            if (imgeName.startsWith("avtar")) {
                Resources res = currentactivity.getResources();

                int resourceId = res.getIdentifier(imgeName, "drawable", currentactivity.getPackageName());
                Drawable drawable = res.getDrawable(resourceId);
                thumbNail.setImageDrawable(drawable);
                //imageLoader.displayImage(pathfile.getPath(),thumbNail);
            } else {
                String iconsStoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + AppConfig.IMAGE_DIRECTORY_NAME;

                File pathfile = new File(iconsStoragePath + File.separator + imgeName);
                String imageUri="file:/"+pathfile.getPath();
                imageLoader.displayImage(imageUri,thumbNail,options);
            }

            memberName.setText(O_member.getMemberName());

            if(O_member.getRelationshipId() == 1) {
                Relation.setText("Mother");
            }
            if(O_member.getRelationshipId() == 2) {
                Relation.setText("Father");
            }
            if(O_member.getRelationshipId() == 3) {
                Relation.setText("Brother");
            }
            if(O_member.getRelationshipId() == 4) {
                Relation.setText("Sister");
            }
            if(O_member.getRelationshipId() == 5) {
                Relation.setText("Wife");
            }
            if(O_member.getRelationshipId() == 6) {
                Relation.setText("Son");
            }
            if(O_member.getRelationshipId() == 7) {
                Relation.setText("Daughter");
            }
            if(O_member.getRelationshipId() == 8) {
                Relation.setText("Self");
            }
            if(O_member.getRelationshipId() == 9) {
                Relation.setText("Friend");
            }
        }
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

