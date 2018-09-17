package viroopa.com.medikart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import viroopa.com.medikart.util.PageIndicator;


public class SlidingScreens extends Activity {
    private PageIndicator mIndicator;
    private ViewPager mViewPager;
    private Handler handler;
    private Snackbar snackbar;
    private RelativeLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sliding_screens);
        mIndicator=(PageIndicator)findViewById(R.id.indicator);
        mViewPager=(ViewPager)findViewById(R.id.view_pager);
        coordinatorLayout = (RelativeLayout) findViewById(R.id.coordinatorLayout);
        mIndicator.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnPageChangeListener(new PageChangeListener());

        mViewPager.setAdapter(new MyPagesAdapter());
        mIndicator.setViewPager(mViewPager);

        final int pageMargin = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 4, getResources() .getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);


    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {


            if (arg0 >= 4) {

                   snackbar = Snackbar
                            .make(coordinatorLayout, "", Snackbar.LENGTH_INDEFINITE).setAction("Get Started", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent Intenet_change = new Intent(SlidingScreens.this, LoginActivity.class);
                                    startActivity(Intenet_change);

                                    snackbar.dismiss();
                                    finish();
                                    //snackbar.dismiss();
                                }
                            });;

                      View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
               // android.widget.Button button = (  android.widget.Button) sbView.findViewById(android.support.design.R.id.snackbar_action);
               // textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setText("Rx Medikart");
                textView.setTextColor(getResources().getColor(R.color.white));
               // button.setTextColor(getResources().getColor(R.color.white));
               // button.setText("Get Started");
               /* button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {

                    }
                });*/


                sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                   snackbar.setDuration(snackbar.LENGTH_INDEFINITE);
                    snackbar.show();




            } else {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
            }
        }
    }

        class MyPagesAdapter extends PagerAdapter {
            @Override
            public int getCount() {
                //Return total pages, here one for each data item
                return 5;
            }

            //Create the given page (indicated by position)
            @Override
            public Object instantiateItem(ViewGroup container, int position) {


                LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.vp_image, container, false);

                ImageView mImageView = (ImageView) view
                        .findViewById(R.id.image_display);

                switch (position) {
                    case 0:

                        mImageView.setImageResource(R.drawable.slide_screen1);
                        break;
                    case 1:
                        mImageView.setImageResource(R.drawable.slide_screen2);
                        break;
                    case 2:
                        mImageView.setImageResource(R.drawable.slide_screen3);
                        break;
                    case 3:
                        mImageView.setImageResource(R.drawable.slide_screen4);
                        break;
                    case 4:
                        mImageView.setImageResource(R.drawable.slide_screen5);
                        break;



                }

                container.addView(view);
                return view;
            }


            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                //See if object from instantiateItem is related to the given view
                //required by API
                return arg0 == (View) arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView((View) object);
                object = null;
            }


        }



}
