package viroopa.com.medikart.dmMonitor.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.dmMonitor.DMA_NewEntry;
import viroopa.com.medikart.model.M_memberlist;

import java.io.File;
import java.util.List;

/**
 * Created by prakash on 18/08/15.
 */
public class DMAD_Entrymember extends BaseAdapter {

 

    private ProgressDialog pDialog;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private  String MemberNameforTag,sMemberId;
    AppController globalVariable;
    private Activity currentactivity;
    private LayoutInflater inflater;
    private  List<M_memberlist> memberList;

    public DMAD_Entrymember(Activity currentactivity, List<M_memberlist> MemberData, String MemberId) {
        this.currentactivity = currentactivity;
        this.sMemberId = MemberId;
        globalVariable = (AppController)currentactivity.getApplicationContext();
        this.memberList=MemberData;
        pDialog = new ProgressDialog(currentactivity);
        pDialog.setCancelable(false);
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
            LinearLayout relativelayoutMain = (LinearLayout) convertView.findViewById(R.id.relativeFirst);
            TextView memberName = (TextView) convertView.findViewById(R.id.productname);
            TextView Relation = (TextView) convertView.findViewById(R.id.relation);


            relativelayoutMain.setTag(R.id.key_product_name,O_member.getPatientId());
            relativelayoutMain.setTag(R.id.key_MemberName,O_member.getMemberName());
            relativelayoutMain.setTag(R.id.key_MemberImage,O_member.getImageurl());
            relativelayoutMain.setTag(R.id.key_RelationShipId, O_member.getRelationshipId());
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
                    imageLoader.displayImage(imageUri,thumbNail);

/*
                    Bitmap mybitmap = BitmapFactory.decodeFile(pathfile.getPath());
                    if (mybitmap != null) {
                        thumbNail.setImageBitmap(mybitmap);
                    } else {
                        try {

                            imageLoader.displayImage(O_member.getImageurl(), thumbNail);
                        } catch (Exception e) {
                        }
                    }
*/

            }


            memberName.setText(O_member.getMemberName());
            if(O_member.getRelationshipId() == 1 ) {
                Relation.setText("Mother");
            }
            if(O_member.getRelationshipId() == 2 ) {
                Relation.setText("Father");
            }
            if(O_member.getRelationshipId() == 3 ) {
                Relation.setText("Brother");
            }
            if(O_member.getRelationshipId() == 4 ) {
                Relation.setText("Sister");
            }
            if(O_member.getRelationshipId() == 5 ) {
                Relation.setText("Wife");
            }
            if(O_member.getRelationshipId() == 6 ) {
                Relation.setText("Son");
            }
            if(O_member.getRelationshipId() == 7 ) {
                Relation.setText("Daughter");
            }
            if(O_member.getRelationshipId() == 8 ) {
                Relation.setText("Self");
            }
            if(O_member.getRelationshipId() == 9 ) {
                Relation.setText("Friend");
            }

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    //productid = view.getTag().toString();


                    MemberNameforTag = (String) view.getTag(R.id.key_product_name);
                    globalVariable.setMemberName((String) view.getTag(R.id.key_MemberName));
                    globalVariable.setMemberImage((String) view.getTag(R.id.key_MemberImage));
                    edit_memeber_profile(MemberNameforTag);
                }
            });



        }
        return convertView;
    }



    private  void edit_memeber_profile(String RelationshipId)
    {

        globalVariable.setRealationshipId(RelationshipId);
       // Intent Intenet_change = new Intent(currentactivity,BPMonitorMain.class);
        Intent Intenet_change = new Intent(currentactivity,DMA_NewEntry.class);
        currentactivity.finish();
        currentactivity.startActivity(Intenet_change);
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

