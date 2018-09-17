package viroopa.com.medikart.bpmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.helper.SqliteBPHandler;


/**
 * Created by IT Spare on 12-08-2015.
 */
public class BPA_WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "BPA_WelcomeActivity";
    private SqliteBPHandler db_bp;
    private String sMemberId;
    private Integer getSelectedRelationshipId;
    AppController globalVariable;
    private Menu objMemberMenu;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.bp_welcomeactivity );
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        globalVariable = ( AppController ) getApplicationContext ( );
        db_bp= new SqliteBPHandler(getApplicationContext());
        getIntenet();
        Showbp();
    }


    @Override
    public boolean onCreateOptionsMenu ( Menu menu ) {

        try {
            MenuInflater inflater = getMenuInflater ( );
            inflater.inflate ( R.menu.bp_menu_dummy, menu );
            this.objMemberMenu = menu;
            return true;

        }
        catch ( Exception e )
        {
            return false ;
        }
    }

    public void onCircle_Menuclick ( MenuItem mi ) {
        // handle click here


        try {
            Show_BP_New_Entry ( );
            finish ( );

        } catch ( Exception e ) {

        }
    }


    @Override
    public boolean onOptionsItemSelected ( MenuItem item ) {

        int id = item.getItemId ( );

        if ( id == R.id.action_settings ) {
            Show_BP_Settings ( );
            return true;
        }



        return super.onOptionsItemSelected ( item );
    }

    private void Show_BP_New_Entry ( ) {

        try {

            Intent Intenet_add = new Intent ( this, BPA_NewEntry.class );
            startActivity ( Intenet_add );

        } catch ( Exception e ) {
            //log.d(TAG e.printStackTrace ( ));
        }

    }

    private void Show_BP_Settings ( ) {

        try {

            Intent Intenet_add = new Intent ( this, BPA_MonitorSetting.class );
            startActivity ( Intenet_add );

        } catch ( Exception e ) {
            //log.d(TAG e.printStackTrace ( ));
        }

    }

    private void Showbp(){


        Cursor cursor_data;
        cursor_data = db_bp.getBPMonitorCount(Integer.parseInt(sMemberId),getSelectedRelationshipId);

        if ((cursor_data != null) && (cursor_data.getCount() > 0)) {
            if (cursor_data.moveToFirst()) {
                Intent Intenet_dm = new Intent(this, BPA_AnalysisDisplayActivity.class);
                startActivity(Intenet_dm);
            }
        }



    }
    private void getIntenet() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

        if (globalVariable.getRealationshipId() != null) {
            getSelectedRelationshipId = Integer.parseInt(globalVariable.getRealationshipId());
        } else {
            getSelectedRelationshipId = 8;
        }

    }

}
