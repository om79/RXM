package viroopa.com.medikart.MedicineReminder;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

import viroopa.com.medikart.R;
import viroopa.com.medikart.common.Add_Doctor_Dialog;
import viroopa.com.medikart.common.ConstData;


public class MRA_SendReprtToDoctor extends AppCompatActivity   implements Add_Doctor_Dialog.OnDoctorSelectListener {
    private  Menu objMemberMenu;
    private TextView pick_doctor;
    private EditText txt_email;
    private Button btnsendreport,btnviewpdf;
    private File file;
    private CheckBox chk_agree;
    private String Mail_subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_reminder_send_reprt_to_doctor);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        txt_email=(EditText)findViewById(R.id.txt_email);
        pick_doctor=(TextView)findViewById(R.id.pick_doctor);
        btnsendreport=(Button)findViewById(R.id.btnsendreport);
        btnviewpdf=(Button)findViewById(R.id.btnviewpdf);
        chk_agree=(CheckBox)findViewById(R.id.chk_agree);




     try {
         file = new File(getIntent().getStringExtra("path"));
         Mail_subject= getIntent().getStringExtra("Email_heading");
     }catch (Exception e)
     {

     }



        btnsendreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConstData.validate(txt_email.getText().toString()))
                {
                    if(chk_agree.isChecked()) {
                        emailPdf();
                    }else {
                        Toast.makeText(MRA_SendReprtToDoctor.this, "Please agree the terms", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(MRA_SendReprtToDoctor.this, "Please enter a valid email id", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnviewpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_pdf();
            }
        });


        pick_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Doctor_Dialog myDiag = Add_Doctor_Dialog.newInstance("add_doctor");
                myDiag.show(getFragmentManager(), "Diag");
            }
        });



    }


    @Override
    public void onSelectDoctor(String Doc_id, String Doc_name,String email_id) {

//        add_doctor.setText(Doc_name);
//        Doctor_id=Doc_id;
        pick_doctor.setText(Doc_name);
        txt_email.setText(email_id);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
       // inflater.inflate(R.menu.menu_new_add_medicine, menu);
        //this.objMemberMenu=menu;


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.reminder_invite) {
            //Show_MedInvite();
            return true;
        }

        if (id == R.id.reminder_all_medicine) {
            // Show_All_Medicine();
        }

        return super.onOptionsItemSelected(item);
    }


    private  void show_pdf()
    {
        try
        {


            Intent intentUrl = new Intent(Intent.ACTION_VIEW);
            intentUrl.setDataAndType(Uri.fromFile(file), "application/pdf");

            intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentUrl);
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
        }

    }


    private void emailPdf()
    {



        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT,Mail_subject);
        email.putExtra(Intent.EXTRA_TEXT,"");
        email.putExtra( Intent.EXTRA_EMAIL, new String[] { txt_email.getText().toString() });
        Uri uri = Uri.parse("file:///"+file.getAbsolutePath());
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("message/rfc822");
        startActivity(email);
    }
}
