package viroopa.com.medikart.dmMonitor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;

public class DMA_WelcomeActivity extends AppCompatActivity {


    AppController globalVariable;
    private  Menu objMemberMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm_welcomeactivity);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        globalVariable = (AppController) getApplicationContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dm_welcome, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.circlularImage) {

            Show_BP_New_Entry();
            finish();

            return true;
        }

            if (id == R.id.action_settings) {
                Intent intent = new Intent(DMA_WelcomeActivity.this, DMA_Settings
                        .class);
                startActivity(intent);
                finish();

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void Show_BP_New_Entry(){
        Intent Intenet_add = new Intent(this, DMA_NewEntry.class);
        startActivity(Intenet_add);
    }

}
