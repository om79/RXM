package viroopa.com.medikart.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.util.List;

import viroopa.com.medikart.AddMember;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.model.M_memberlist;

public class MemberRecyclerAdapter extends RecyclerView.Adapter<MemberRowHolder> {


    private List<M_memberlist> feedItemList;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private Context mContext;
    private ProgressDialog pDialog;
    private String sMemberId;
    private String productid,productname;
    AppController globalVariable;
    private  String MemberNameforTag;
    DisplayImageOptions options;

    public MemberRecyclerAdapter(Context context, List<M_memberlist> feedItemList, String memberid) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        this.sMemberId = memberid;
        initImageLoader();
        pDialog = new ProgressDialog(mContext);
        globalVariable = (AppController)mContext.getApplicationContext();

        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(1000)) //rounded corner bitmap
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
    }

    @Override
    public MemberRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_add_member_item, null);
        MemberRowHolder mh = new MemberRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(MemberRowHolder MemberRowHolder, int i) {

        M_memberlist feedItem = feedItemList.get(i);

        String ImagePath=feedItem.getImageurl();

        MemberRowHolder.relativelayoutMain.setTag(R.id.key_product_name,feedItem.getPatientId());
        String imgeName=ImagePath.substring(ImagePath.lastIndexOf('/') + 1, ImagePath.length());




        if (imgeName.startsWith("avtar")) {
            Resources res = mContext.getResources();

            int resourceId = res.getIdentifier(imgeName, "drawable", mContext.getPackageName());
            Drawable drawable = res.getDrawable(resourceId);
            MemberRowHolder.thumbNail.setImageDrawable(drawable);
        } else {
            String iconsStoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + AppConfig.IMAGE_DIRECTORY_NAME;

            File pathfile = new File(iconsStoragePath + File.separator + imgeName);

            //Bitmap mybitmap = BitmapFactory.decodeFile(pathfile.getPath());
            if (pathfile.exists()) {
                //thumbNail.setImageBitmap(mybitmap);
                imageLoader.displayImage("file://" + pathfile.getPath(), MemberRowHolder.thumbNail,options);
            } else {
                try {
                    imageLoader.displayImage(feedItem.getImageurl(), MemberRowHolder.thumbNail,options);
                } catch (Exception e) {
                }
            }

        }


        MemberRowHolder.memberName.setText(feedItem.getMemberName());
        Integer cv=feedItem.getRelationshipId();

       /* if(feedItem.getRelationshipId().equals("1.0")) {
            MemberRowHolder.Relation.setText("Mother");
        }
        if(feedItem.getRelationshipId().equals("2.0")) {
            MemberRowHolder.Relation.setText("Father");
        }
        if(feedItem.getRelationshipId().equals("3.0")) {
            MemberRowHolder.Relation.setText("Brother");
        } if(feedItem.getRelationshipId().equals("4.0")) {
            MemberRowHolder.Relation.setText("Sister");
        }
        if(feedItem.getRelationshipId().equals("5.0")) {
            MemberRowHolder.Relation.setText("Wife");
        }
        if(feedItem.getRelationshipId().equals("6.0")) {
            MemberRowHolder.Relation.setText("Son");
        }
        if(feedItem.getRelationshipId().equals("7.0")) {
            MemberRowHolder.Relation.setText("Daughter");
        }

        if(feedItem.getRelationshipId().equals("9.0")) {
            MemberRowHolder.Relation.setText("Friend");
        }*/

        MemberRowHolder.Relation.setText(feedItem.getRelation_name());

        MemberRowHolder.relativelayoutMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //productid = view.getTag().toString();


                MemberNameforTag = (String) view.getTag(R.id.key_product_name);

                edit_memeber_profile(MemberNameforTag);
            }
        });



    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }
    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                mContext).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }



    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    private void showPdialog(String sMessage) {
        if (pDialog != null) {
            pDialog.setMessage(sMessage);
            pDialog.show();
        }
    }
    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(mContext)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private  void edit_memeber_profile(String memberid)
    {
        SharedPreferences pref = mContext.getSharedPreferences("Global", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString("memberIdforEdit", memberid);

        prefsEditor.commit();

        Intent Intenet_change = new Intent(mContext,AddMember.class);
        Intenet_change.putExtra("Mode","E");
        Intenet_change.putExtra("memberIdforEdit",memberid);
        mContext.startActivity(Intenet_change);
    }
}
