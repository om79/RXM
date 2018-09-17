package viroopa.com.medikart.buying.gallery;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import viroopa.com.medikart.R;
import viroopa.com.medikart.buying.uploadprescriptionActivity;

/**
 * Created by ABCD on 05/11/2015.
 */
public class GalleryAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater infalter;
    private ArrayList<CustomGallery> data = new ArrayList<CustomGallery>();
    ImageLoader imageLoader;
    private  Activity actvity;

    Integer nCount = 0;
    private boolean isActionMultiplePick;
    private String sType ;

    public GalleryAdapter(Context c, ImageLoader imageLoader, String cType, Activity act) {
        infalter = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = c;
        sType = cType ;
        actvity=act;
        this.imageLoader = imageLoader;
// clearCache();
    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public CustomGallery getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setMultiplePick(boolean isMultiplePick) {
        this.isActionMultiplePick = isMultiplePick;
    }

    public void selectAll(boolean selection) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).isSeleted = selection;

        }
        notifyDataSetChanged();
    }

    public boolean isAllSelected() {
        boolean isAllSelected = true;

        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).isSeleted) {
                isAllSelected = false;
                break;
            }
        }

        return isAllSelected;
    }

    public boolean isAnySelected() {
        boolean isAnySelected = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                isAnySelected = true;
                break;
            }
        }

        return isAnySelected;
    }

    public ArrayList<CustomGallery> getSelected() {
        ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted  ) {
                if( dataT.size()<4) {
                    dataT.add(data.get(i));
                }else {
                   // Toast.makeText(mContext, "", Toast.LENGTH_LONG).show();
                }
            }
        }

        return dataT;
    }

    // Added by prakash
    public Integer getSelectedcount() {

        Integer Mycount = 0 ;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                Mycount = Mycount + 1;
            }
        }

        return Mycount;
    }

    public void addAll(ArrayList<CustomGallery> files) {

        try {

            data.clear();
            data.addAll(files);

        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    public void changeSelection(View v, int position) {
           Integer selectionCount=0;
        for(int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                selectionCount++;
            }
        }

        if (data.get(position).isSeleted ) {
            data.get(position).isSeleted = false;
        } else if(selectionCount<4){
            data.get(position).isSeleted = true;
        }else{
            Toast.makeText(mContext, "you can select only  four Images",
                    Toast.LENGTH_SHORT).show();
        }

//if (data.size() <= 4) { // added by prakash
        ((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(data.get(position).isSeleted);
       /* Toast.makeText(mContext, "you clicked" + String.valueOf(position),
                Toast.LENGTH_SHORT).show();*/
//}
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {

            convertView = infalter.inflate(R.layout.gallery_item, null);

            holder = new ViewHolder();
            holder.imgQueue = (ImageView) convertView.findViewById(R.id.imgQueue);
            holder.canceltxt = (TextView) convertView.findViewById(R.id.txtcancel);
           /* holder.frmlayout = (FrameLayout) convertView.findViewById(R.id.frmQueue);
            holder.frmlayout.setTag(position);*/
           // holder.canceltxt.setTag(position);


            holder.imgQueueMultiSelected = (ImageView) convertView
                    .findViewById(R.id.imgQueueMultiSelected);

            if (isActionMultiplePick) {
                holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
            } else {
                holder.imgQueueMultiSelected.setVisibility(View.GONE);
            }

            convertView.setTag(holder);

            if (sType.equals("V")) {
                holder.canceltxt.setVisibility(convertView.VISIBLE);
                holder.canceltxt.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        String pos = view.getTag().toString();
                        /*Toast.makeText(mContext, pos, Toast.LENGTH_LONG)
                                .show();*/
                        //clear();
                        ((uploadprescriptionActivity)actvity).clear_array(Integer.parseInt(pos));
                      //  mMyActivity.save_to_database(Your ArrayList);


                    }
                });
            }

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imgQueue.setTag(position);
        holder.canceltxt.setTag(position);

        try {

            imageLoader.displayImage("file://" + data.get(position).sdcardPath,
                    holder.imgQueue, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.imgQueue.setImageResource(R.drawable.no_media);
                            super.onLoadingStarted(imageUri, view);
                        }
                    });

            if (isActionMultiplePick) {

                holder.imgQueueMultiSelected.setSelected(data.get(position).isSeleted);

            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public class ViewHolder {
        ImageView imgQueue;
        ImageView imgQueueMultiSelected;
        TextView canceltxt;
        //FrameLayout frmlayout;
    }

    public void clearCache() {
        imageLoader.clearDiscCache();
        imageLoader.clearMemoryCache();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }


}

