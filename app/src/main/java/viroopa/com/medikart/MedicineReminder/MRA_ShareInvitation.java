package viroopa.com.medikart.MedicineReminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import viroopa.com.medikart.R;


public class MRA_ShareInvitation extends AppCompatActivity {

    // private ImageButton btnbp;
    // private ImageView btnbuy, btnbp, btnmr, btnwm, btndm;
    private String sMemberId;
    private ImageView contact;
    private TextView randomtxt,msgtxt,main_name;
    private String random_number;
   private String msg;
    private String contact_name;
    private String contact_number;
    private String mail;
    private Button sms,email;
    private  String sMemberid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_a_invite);
        randomtxt =(TextView)findViewById(R.id.textView48);
        msgtxt=(TextView)findViewById(R.id.textView49);
        sms=(Button)findViewById(R.id.btnsave);
        email=(Button)findViewById(R.id.btnemail);
        main_name=(TextView)findViewById(R.id.textView47);
        sms.clearAnimation();
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.milkshake);
        sms.startAnimation(animRotate);
        getintent();
        randomtxt.setText(random_number);
        msg="I want you to be notified if i forget my meds.Get Medicine Reminder app.  http://google.com . Your invite code is: "+random_number;

        msgtxt.setText("I would like to send this code to  "+contact_name+". Your friend should enter the code when they download and open the app");
        main_name.setText(contact_name);



        email.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                Log.i("Send email", "");
                String[] TO = {""};
                String[] CC = {""};

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                //emailIntent.putExtra(Intent.ACTION_SENDTO, mail);
              //  emailIntent.putExtraIntent.ACTION_SENDTO
                emailIntent.putExtra( Intent.EXTRA_EMAIL, new String[] { mail });
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Medicine Reminder");
                emailIntent.putExtra(Intent.EXTRA_TEXT, msg);

                emailIntent.putExtra("address", contact_number);

                try {
                    //startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    startActivity(emailIntent);
                    finish();
                    Log.i("Finished sending email", "");
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MRA_ShareInvitation.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

            }


        });


            sms.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {


                    send_invitation();


                }


            });






    }

   public void getintent() {
       Intent intent_mainactivity = getIntent();
       contact_name = intent_mainactivity.getStringExtra("contact_name");
       contact_number = intent_mainactivity.getStringExtra("contact_nr");
       mail = intent_mainactivity.getStringExtra("email_id");

       random_number=intent_mainactivity.getStringExtra("random_nr");
       SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
       sMemberid = pref.getString("memberid", "");

   }





    public void send_invitation()
    {
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        //emailIntent.putExtra(Intent.ACTION_SENDTO, mail);
        //  emailIntent.putExtraIntent.ACTION_SENDTO
        emailIntent.putExtra( Intent.EXTRA_EMAIL, new String[] { mail });
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Medicine Reminder");
        emailIntent.putExtra(Intent.EXTRA_TEXT, msg);

        emailIntent.putExtra("address", contact_number);

        try {
            //startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            startActivity(emailIntent);
            finish();
            Log.i("Finished sending email", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MRA_ShareInvitation.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }


    }


}