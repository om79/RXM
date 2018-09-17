/*
 * android-spinnerwheel
 * https://github.com/ai212983/android-spinnerwheel
 *
 * based on
 *
 * Android Wheel Control.
 * https://code.google.com/p/android-wheel/
 *
 * Copyright 2011 Yuri Kanivets
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package viroopa.com.medikart.MedicineReminder.MedicineImageColorShape;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import viroopa.com.medikart.R;
import viroopa.com.medikart.common.ConstData;


/**
 * Abstract spinnerwheel adapter provides common functionality for adapters.
 */
public  class AbstractWheelTextAdapter extends AbstractWheelAdapter {
    
    /** Text view resource. Used as a default view for adapter. */
    public static final int TEXT_VIEW_ITEM_RESOURCE = -1;
    
    /** No resource constant. */
    public static final int NO_RESOURCE = 0;
    
    /** Default text color */
    public static final int DEFAULT_TEXT_COLOR = 0xFF101010;
    
    /** Default text color */
    public static final int LABEL_COLOR = 0xFF700070;
    
    /** Default text size */
    public static final int DEFAULT_TEXT_SIZE = 24;

    /// Custom text typeface
    private Typeface textTypeface;
    
    // Text settings
    private int textColor = DEFAULT_TEXT_COLOR;
    private int textSize = DEFAULT_TEXT_SIZE;
    public ImageView[] imgArray;
    
    // Current context
    public Context context;
    public String flag;
    public  int[]Image;

    public ImageView frst_part;
    public ImageView second_part;
    private LinearLayout double_layout;
    // Layout inflater
    public LayoutInflater inflater;
    
    // Items resources
    public int itemResourceId;
    public int itemTextResourceId;
    
    // Empty items resources
    public int emptyItemResourceId;


    /**
     * Constructor
     * @param context the current context
     */
    public AbstractWheelTextAdapter(Context context) {
        this(context, TEXT_VIEW_ITEM_RESOURCE);
    }

    /**
     * Constructor
     * @param context the current context
     * @param itemResource the resource ID for a layout file containing a TextView to use when instantiating items views
     */
    public AbstractWheelTextAdapter(Context context, int itemResource) {
        this(context, itemResource, NO_RESOURCE);
    }
    
    /**
     * Constructor
     * @param context the current context
     * @param itemResource the resource ID for a layout file containing a TextView to use when instantiating items views
     * @param itemTextResource the resource ID for a text view in the item layout
     */
    public AbstractWheelTextAdapter(Context context, int itemResource, int itemTextResource) {
        this.context = context;
        itemResourceId = itemResource;
        itemTextResourceId = itemTextResource;
        
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public AbstractWheelTextAdapter(Context context, int[]Image, String flag ) {
        this.context = context;
        this.Image=Image;
        this.flag=flag;
        this.imgArray=new ImageView[Image.length];

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * Gets text color
     * @return the text color
     */
    public int getTextColor() {
        return textColor;
    }
    
    /**
     * Sets text color
     * @param textColor the text color to set
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    /**
     * Sets text typeface
     * @param typeface typeface to set
     */
    public void setTextTypeface(Typeface typeface) {
        this.textTypeface = typeface;
    }


    public ImageView[] getImagearray(){
        return this.imgArray;
    }

    /**
     * Gets text size
     * @return the text size
     */
    public int getTextSize() {
        return textSize;
    }
    
    /**
     * Sets text size
     * @param textSize the text size to set
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
    
    /**
     * Gets resource Id for items views
     * @return the item resource Id
     */
    public int getItemResource() {
        return itemResourceId;
    }
    
    /**
     * Sets resource Id for items views
     * @param itemResourceId the resource Id to set
     */
    public void setItemResource(int itemResourceId) {
        this.itemResourceId = itemResourceId;
    }
    
    /**
     * Gets resource Id for text view in item layout 
     * @return the item text resource Id
     */
    public int getItemTextResource() {
        return itemTextResourceId;
    }
    
    /**
     * Sets resource Id for text view in item layout 
     * @param itemTextResourceId the item text resource Id to set
     */
    public void setItemTextResource(int itemTextResourceId) {
        this.itemTextResourceId = itemTextResourceId;
    }

    /**
     * Gets resource Id for empty items views
     * @return the empty item resource Id
     */
    public int getEmptyItemResource() {
        return emptyItemResourceId;
    }

    /**
     * Sets resource Id for empty items views
     * @param emptyItemResourceId the empty item resource Id to set
     */
    public void setEmptyItemResource(int emptyItemResourceId) {
        this.emptyItemResourceId = emptyItemResourceId;
    }

    public ImageView getfirstpart() {
       return this.frst_part;
    }
    public ImageView getsecondpart() {
        return this.second_part;
    }

    public LinearLayout getlnrImg() {
        return this.double_layout;
    }



    @Override
    public int getItemsCount() {
        return Image.length;
    }

   // public abstract CharSequence getItemText(int index);

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
       // FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(50,50);
       // params.setMargins(10,10,10,10);
        if (index >= 0 && index < getItemsCount()) {
            if (convertView == null) {
                convertView=inflater.inflate(R.layout.wheel_text_centered_dark_back, null);
               // convertView = getView(itemResourceId, parent);
            }
            ImageView textView = (ImageView)convertView.findViewById(R.id.text);

            imgArray[index]=textView;

            if(flag.equals("image"))
            {
                    textView.setImageResource(Image[index]);
                   // textView.setLayoutParams(params);

            }else {
                textView.setImageResource(R.drawable.rounded_button);
                textView.setColorFilter(ConstData.colr_array[index]);
                textView.setPadding(5,5,5,5);
              //  textView.setLayoutParams(params);
            }

            return convertView;
        }
        return null;
    }

    @Override
    public View getEmptyItem(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getView(emptyItemResourceId, parent);
        }
        if (convertView instanceof TextView) {
           // configureTextView((TextView)convertView);
        }
            
        return convertView;
    }

    /**
     * Configures text view. Is called for the TEXT_VIEW_ITEM_RESOURCE views.
     * @param view the text view to be configured
     */
    public void configureTextView(ImageView view) {
        if (itemResourceId == TEXT_VIEW_ITEM_RESOURCE) {
           // view.setTextColor(textColor);
           // view.setGravity(Gravity.CENTER);
          //  view.setTextSize(textSize);
          //  view.setLines(1);
        }
        if (textTypeface != null) {
           // view.setTypeface(textTypeface);
        } else {
           // view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        }
    }
    
    /**
     * Loads a text view from view
     * @param view the text view or layout containing it
     * @param textResource the text resource Id in layout
     * @return the loaded text view
     */
    private ImageView getTextView(View view, int textResource) {
        ImageView text = null;
        try {
            if (textResource == NO_RESOURCE && view instanceof TextView) {
                text = (ImageView) view;
            } else if (textResource != NO_RESOURCE) {
                text = (ImageView) view.findViewById(textResource);
            }
        } catch (ClassCastException e) {
            Log.e("AbstractWheelAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "AbstractWheelAdapter requires the resource ID to be a TextView", e);
        }
        
        return text;
    }
    
    /**
     * Loads view from resources
     * @param resource the resource Id
     * @return the loaded view or null if resource is not set
     */
    private View getView(int resource, ViewGroup parent) {
        switch (resource) {
        case NO_RESOURCE:
            return null;
        case TEXT_VIEW_ITEM_RESOURCE:
            return new TextView(context);
        default:
            return inflater.inflate(resource, parent, false);
        }
    }
}
