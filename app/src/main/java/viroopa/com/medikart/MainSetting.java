package viroopa.com.medikart;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.model.M_memberlist;

/**
 * Created by Administrator on 03/Sep/2015.
 */
public class MainSetting extends AppCompatActivity implements OnItemClickListener {
    String get_def_site;
    String get_def_pos;
    String set_def_site;
    String set_def_pos;
    String set_his_len;
    Integer lstWeight;
    String sort_order;
    String classifictn;
    Integer postn;
    Integer testint;
    AppController globalVariable;
    private Menu objMemberMenu;
    private View positiveAction;
    private CheckBox input_chk_lastWeightf;
    private CheckBox input_chk_lastWeights;
    private CheckBox input_chk_average_value;
    private CheckBox input_chk_compact_layout;
    private EditText edtname;
    private String sMemberId;
    private ListView lv;
    List< M_memberlist > MemberData = new ArrayList< M_memberlist > ( );

    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //      WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView ( R.layout.main_setting );
        globalVariable = ( AppController ) getApplicationContext ( );


        getIntenet ( );


        ArrayList< ContentItem > objects = new ArrayList< ContentItem > ( );
        objects.add ( new ContentItem ( "GENERAL", "" ) );
        objects.add ( new ContentItem ( "Classification Standard", "Blood pressure classification standard" ) );

        objects.add ( new ContentItem ( "Diabets Monitor", "" ) );
        objects.add ( new ContentItem ( "Glucose Unit", "Select default glucose unit" ) );
        objects.add ( new ContentItem ( "Weight Unit", "Select default weight unit" ) );
        objects.add ( new ContentItem ( "Use Last Entered Weight", "Clear weight field when adding entry" ) );


        objects.add ( new ContentItem ( "BP Moonitor", "" ) );
        objects.add ( new ContentItem ( "Defualt Site", "Select default reading site" ) );
        objects.add ( new ContentItem ( "Defaul Position", "Select default reading position" ) );
        objects.add ( new ContentItem ( "Use Last Entered Weight", "Clear weight field when adding entry" ) );
        objects.add ( new ContentItem ( "HISTORY", "" ) );
        objects.add ( new ContentItem ( "History Length", "Maximum Number of entries to show in history tab" ) );
        objects.add ( new ContentItem ( "Sort Order", "Which entries to show first" ) );
        objects.add ( new ContentItem ( "Use Compact Layout", "Enable Simple history List Layout" ) );
        objects.add ( new ContentItem ( "CHARTS", "" ) );
        objects.add ( new ContentItem ( "Average Values", "Show daily average in charts" ) );
        objects.add ( new ContentItem ( "TOOLS", "" ) );
        objects.add ( new ContentItem ( "Wipe Data", "" ) );
        objects.add ( new ContentItem ( "Wipe Backups", "" ) );


        MyAdapter adapter = new MyAdapter ( this, objects );

        lv = ( ListView ) findViewById ( R.id.listChart );


        lv.setAdapter ( adapter );
        lv.setItemsCanFocus ( true );
//
        lv.setOnItemClickListener ( this );
        input_chk_lastWeightf = ( CheckBox ) findViewById ( R.id.cbBox );


    }

    @Override
    public boolean onCreateOptionsMenu ( Menu menu ) {
        /// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ( ).inflate ( R.menu.menu_main_setting, menu );
        this.objMemberMenu = menu;
        LayoutInflater mInflater = LayoutInflater.from ( this );
        View mCustomView = mInflater.inflate ( R.layout.circula_image, null );
        objMemberMenu.findItem ( R.id.circlularImage ).setActionView ( mCustomView );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected ( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ( );


       /* if (id == R.id.action_allmeds) {
            gotoAllMedicines();
            return true;
        }*/

        return super.onOptionsItemSelected ( item );
    }










    private void showInputDialog ( ) {
      /*  new AlertDialog.Builder(MainSetting.this)
                .setTitle("History Length")

                .inputType(InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_CLASS_NUMBER)
                .inputMaxLength(6)

                .positiveText("ok")
                .input(set_his_len, "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        //showToast("Hello, " + input.toString() + "!");
                        set_setttings("Bp_History_length",input.toString());
                    }

                })
                .negativeText("Cancel").show();*/


    }


    @Override
    public void onItemClick ( AdapterView< ? > av, View v, int pos, long arg3 ) {

        Intent i;
        CheckBox cbBuy = ( CheckBox ) v.findViewById ( R.id.cbBox );

        switch ( pos ) {
            case 0:
                //i = new Intent(this, LineChartActivity1.class);
//                startActivity(i);
                break;
            case 1:
                classfctn_std ( );
//
                break;
            case 3:
                // i = new Intent(this, LineChartActivity2.class);
//                startActivity(i);
//                break;
                //f_alert_ok("","u clicked me");
                defualtsite ( );
                break;
            case 4:
                // f_alert_ok("","u clicked me");
//                i = new Intent(this, BarChartActivity.class);
//                startActivity(i);
                defualtposition ( );
                break;
            case 5:
//                i = new Intent(this, HorizontalBarChartActivity.class);
//                startActivity(i);


                check_bp ( input_chk_lastWeightf );
                //f_alert_ok("","u clicked me");


                break;
            case 7:


                //f_alert_ok("","");
                showInputDialog ( );
                //f_history_length();
                //test_dialog();
                // f_add_doctor();
                break;
            // f_alert_ok("","u clicked me");
            case 8:
                Sort_order ( );
                break;
            case 9:
                CheckBox ckh_bp = input_chk_compact_layout;
                if ( ! ckh_bp.isChecked ( ) ) {
                    ckh_bp.setChecked ( true );
                    set_setttings ( "Bp_use_comlayout", "cTrue" );

                } else {
                    ckh_bp.setChecked ( false );
                    set_setttings ( "Bp_use_comlayout", "cFalse" );
                }


                break;
            case 11:
                CheckBox avg_chk = input_chk_average_value;
                if ( ! avg_chk.isChecked ( ) ) {
                    avg_chk.setChecked ( true );
                    set_setttings ( "Bp_use_averagevalue", "valueTrue" );

                } else {
                    avg_chk.setChecked ( false );
                    set_setttings ( "Bp_use_averagevalue", "valueFalse" );
                }


                break;
            case 13:
                f_bp_wipe_data ( );
                break;
            case 14:
                f_bp_wipe_backup ( );
                break;

            //overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        }
    }


    private class ContentItem {
        String name;
        String desc;

        public ContentItem ( String n, String d ) {
            name = n;
            desc = d;
        }
    }

    private void check_bp ( CheckBox chk ) {
        CheckBox ckh_bp = chk;
        if ( ! ckh_bp.isChecked ( ) ) {
            ckh_bp.setChecked ( true );
            set_setttings_Integer ( "Bp_use_last_weight", 1 );

        } else {
            ckh_bp.setChecked ( false );
            set_setttings_Integer ( "Bp_use_last_weight", 0 );
        }

    }


//        private void showInputDialogCustomInvalidation() {
//        new MaterialDialog.Builder(this)
//                .title("akhil")
//                .content("test")
//                .inputType(InputType.TYPE_CLASS_TEXT |
//                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
//                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
//                .positiveText("submit")
//                .alwaysCallInputCallback() // this forces the callback to be invoked with every input change
//                .input("hint", 0, false, new MaterialDialog.InputCallback() {
//                    @Override
//                    public void onInput(MaterialDialog dialog, CharSequence input) {
//                        if (input.toString().equalsIgnoreCase("hello")) {
//                            dialog.setContent("I told you not to type that!");
//                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
//                        } else {
//                            //dialog.setContent("I told you not to type that!");
//                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
//                        }
//                    }
//                }).show();
//        }


    private void f_alert_ok ( String p_title, String p_msg ) {

        new AlertDialog.Builder ( MainSetting.this )
                .setTitle ( p_title )
                .setMessage ( p_msg )

                .setPositiveButton ( "OK", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialog, int which ) {
                        dialog.dismiss ( );
                    }
                } ).show ( );
    }


    private void f_bp_wipe_data ( ) {

        new AlertDialog.Builder ( MainSetting.this )
                .setTitle ( "Wipe data" )
                .setMessage ( "This operation will delete all stored entries.\n Continue?" )

                .setPositiveButton ( "OK", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialog, int which ) {
                        dialog.dismiss ( );
                    }
                } )
                .setNegativeButton ( "cancel", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialog, int which ) {
                        dialog.dismiss ( );
                    }
                } ).show ( );
    }

    private void f_bp_wipe_backup ( ) {

        new AlertDialog.Builder ( MainSetting.this )
                .setTitle ( "Wipe backups" )
                .setMessage ( "This operation will delete all backup copies.\n Continue?" )

                .setPositiveButton ( "OK", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialog, int which ) {
                        dialog.dismiss ( );
                    }
                } )
                .setNegativeButton ( "cancel", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialog, int which ) {
                        dialog.dismiss ( );
                    }
                } ).show ( );
    }


    private class MyAdapter extends ArrayAdapter< ContentItem > {

        public MyAdapter ( Context context, List< ContentItem > objects ) {
            super ( context, 0, objects );
        }

        @Override
        public int getViewTypeCount ( ) {

            return getCount ( );
        }

        @Override
        public int getItemViewType ( int position ) {

            return position;
        }

        @Override
        public View getView ( int position, View convertView, ViewGroup parent ) {

            ContentItem c = getItem ( position );
            LinearLayout rowLayout = null;

            ViewHolder holder = null;
            Integer x = position;
            //postn=position;
            if ( convertView == null ) {

                holder = new ViewHolder ( );

                convertView = LayoutInflater.from ( getContext ( ) ).inflate ( R.layout.itemone, null );
                holder.tvName = ( TextView ) convertView.findViewById ( R.id.tvDescr );
                holder.tvDesc = ( TextView ) convertView.findViewById ( R.id.tvPrice );
                final LinearLayout mainlnrsetting = ( LinearLayout ) convertView.findViewById ( R.id.mainlnrsetting );
                if ( x == 5 ) {
                    input_chk_lastWeightf = ( CheckBox ) convertView.findViewById ( R.id.cbBox );


                }

                if ( x == 9 ) {
                    input_chk_compact_layout = ( CheckBox ) convertView.findViewById ( R.id.cbBox );
                }
                if ( x == 11 ) {
                    input_chk_average_value = ( CheckBox ) convertView.findViewById ( R.id.cbBox );
                }

                if ( x == 0 || x == 2 || x == 6 || x == 10 || x == 12 ) {
                    convertView.setBackgroundColor ( Color.rgb ( 255, 153, 0 ) );
                    convertView.setLayoutParams ( new AbsListView.LayoutParams ( lv.getWidth ( ), 30 ) );
                    holder.tvName.setTextSize ( 15 );
                    holder.tvName.setTextColor ( Color.WHITE );
                    //mainlnrsetting


                }
                if ( x == 5 || x == 9 || x == 11 ) {
                    CheckBox cbBuy = ( CheckBox ) convertView.findViewById ( R.id.cbBox );

                    cbBuy.setVisibility ( convertView.VISIBLE );


                }


            } else {
                holder = ( ViewHolder ) convertView.getTag ( );
            }

            holder.tvName.setText ( c.name );
            holder.tvDesc.setText ( c.desc );
            convertView.setTag ( holder );
            return convertView;
        }

        private class ViewHolder {

            TextView tvName, tvDesc;
        }
    }


    private void defualtposition ( ) {
       /* new MaterialDialog.Builder(this)
                .title("Default Position")
                .items(R.array.array_positiontype)
                .itemsCallbackSingleChoice(2, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                       // Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
//                        get_def_site=//text.toString();
//                        f_alert_ok("",);
                        set_setttings_Integer("Bp_Default_Pos", which);
                        String xy=String.valueOf(which);
                        //set_setttings_bp_defSite();
                       // f_alert_ok("",xy);
                        return true; // allow selection
                    }
                })

                .negativeText("Cancel").show();*/

    }

    private void defualtsite ( ) {
     /*   new MaterialDialog.Builder(this)
                .title("Default Site")
                .titleColor(Color.GREEN)
                .items(R.array.array_position)
                .itemsCallbackSingleChoice(2, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        get_def_pos = text.toString();
                        //set_setttings_bp_defPos();
                        set_setttings_Integer("Bp_Default_Site", which );
                        String xy=String.valueOf(which);
                      //  f_alert_ok("", xy);
                        return true; // allow selection
                    }
                })
                .negativeText("Cancel").show();
//                .positiveText("Choose")*/
//                .show();

    }

    private void Sort_order ( ) {
      /*  new MaterialDialog.Builder(this)
                .title("Sort Order")
                .items(R.array.BPsetting_Sort)
                .itemsCallbackSingleChoice(2, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        get_def_site= text.toString();

                        set_setttings("Bp_Sort_order",get_def_site);
                        //set_setttings_bp_defSite();
                        //f_alert_ok("",test);
                        return true; // allow selection
                    }
                })

                .negativeText("Cancel").show();*/

    }

    private void classfctn_std ( ) {
       /* new MaterialDialog.Builder(this)
                .title("Classification Standard")
                .items(R.array.BPsetting_classification)
                .itemsCallbackSingleChoice(2, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        classifictn= text.toString();

                        set_setttings("Bp_Classification",classifictn);
                        //set_setttings_bp_defSite();
                        //f_alert_ok("",test);
                        return true; // allow selection
                    }
                })

                .negativeText("Cancel").show();*/

    }

    private void set_setttings ( String s_name, String sttng ) {
        SharedPreferences pref = getApplicationContext ( ).getSharedPreferences ( "Global", MODE_PRIVATE );
        SharedPreferences.Editor editor = pref.edit ( );
        //editor.putString("memberid", response.optString("UserID"));
        String shrd_name;
        String shrd_sttng;
        shrd_name = s_name;
        shrd_sttng = sttng;
        editor.putString ( shrd_name, shrd_sttng );
        // editor.putString("Bp_Default_Position", def_pos);
        editor.commit ( );
    }

    private void set_setttings_Integer ( String s_name, Integer sttng ) {
        SharedPreferences pref = getApplicationContext ( ).getSharedPreferences ( "Global", MODE_PRIVATE );
        SharedPreferences.Editor editor = pref.edit ( );
        //editor.putString("memberid", response.optString("UserID"));
        String shrd_name;

        shrd_name = s_name;

        editor.putInt ( shrd_name, sttng );
        // editor.putString("Bp_Default_Position", def_pos);
        editor.commit ( );
    }

    private void getIntenet ( ) {
        Intent intent_buymainactivity = getIntent ( );
        //sMemberId = intent_buymainactivity.getStringExtra("memberid");

        SharedPreferences pref = getApplicationContext ( ).getSharedPreferences ( "Global", MODE_PRIVATE );
        set_def_pos = pref.getString ( "Bp_Default_Position", "" );
        Integer i = pref.getInt ( "Bp_Default_Site", 0 );
        String xy = String.valueOf ( i );
        //set_def_site = pref.getInt("Bp_Default_Site", );
        // lstWeight=pref.getInt("Bp_use_last_weight", 0);
        set_his_len = pref.getString ( "Bp_History_length", "" );
        sort_order = pref.getString ( "Bp_Sort_order", "" );
        sMemberId = pref.getString ( "memberid", "" );
        //f_alert_ok(lstWeight,set_his_len);
        //  f_alert_ok(set_def_pos,set_def_site);
        // f_alert_ok("",xy);

    }

}


