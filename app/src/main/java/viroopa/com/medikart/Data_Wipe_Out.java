package viroopa.com.medikart;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Data_Wipe_Out extends AppCompatActivity {

    private Button btnDelete;
    private TextView lblmemberId;
    private EditText memberId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data__wipe__out);

        btnDelete=(Button)findViewById(R.id.btnDelete);
        lblmemberId=(TextView) findViewById(R.id.lblmemberid);
        memberId=(EditText) findViewById(R.id.memberId);


        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showpanel();
            }
        });

    }

    private void showpanel(){

        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.data_wipe_popup, null);
        builder.setView(dialogview);
        final Dialog dlg=builder.create();
        final TextView username=(TextView)dialogview.findViewById(R.id.maintitle);
        final Button yes=(Button)dialogview.findViewById(R.id.btnYeswipe);
        final Button No=(Button)dialogview.findViewById(R.id.btnNowipe);

        yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (username.getText().toString().isEmpty()) {

                } else {

                    dlg.cancel();
                }
            }
        });
        dlg.show();
    }
}
