package viroopa.com.medikart;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TermsCondition extends AppCompatActivity
         {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_condition);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        getIntenet();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(TermsCondition.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void getIntenet() {

    }
             @Override
             protected void attachBaseContext(Context newBase) {
                 super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
             }
}
