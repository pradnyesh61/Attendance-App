package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class OTP extends AppCompatActivity {

    EditText o1, o2, o3, o4, o5, o6;
    Button submit;

    String sphone;
    int x1;
    int x2;
    int x3;
    int x4;
    int x5;
    int x6;

    EditText edit_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        o1 = findViewById(R.id.o1);
        o2 = findViewById(R.id.o2);
        o3 = findViewById(R.id.o3);
        o4 = findViewById(R.id.o4);
        o5 = findViewById(R.id.o5);
        o6 = findViewById(R.id.o6);

        submit = findViewById(R.id.submit_otp);

        Intent i = getIntent();
        sphone = i.getStringExtra("contact");

        edit_number = findViewById(R.id.edit_number);

        edit_number.setText(sphone);
        edit_number.setEnabled(false);

        if (ContextCompat.checkSelfPermission(OTP.this,
                Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED) {

            sendMessage();
        } else {
            ActivityCompat.requestPermissions(OTP.this, new String[]{Manifest.permission.SEND_SMS}, 100);

        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(o1.getText())) {
                    o1.setError("!");
                } else if (TextUtils.isEmpty(o2.getText())) {
                    o2.setError("!");
                } else if (TextUtils.isEmpty(o3.getText())) {
                    o3.setError("!");
                } else if (TextUtils.isEmpty(o4.getText())) {
                    o4.setError("!");
                } else if (TextUtils.isEmpty(o5.getText())) {
                    o5.setError("!");
                } else if (TextUtils.isEmpty(o6.getText())) {
                    o6.setError("!");
                } else {

                    if (Integer.parseInt(o1.getText().toString()) == (x1) && Integer.parseInt(o2.getText().toString()) == (x2) && Integer.parseInt(o3.getText().toString()) == (x3) && Integer.parseInt(o4.getText().toString()) == (x4) && Integer.parseInt(o5.getText().toString()) == (x5) && Integer.parseInt(o6.getText().toString()) == (x6)) {
                        Intent i = new Intent(OTP.this, Forgot_Password.class);
                        i.putExtra("contactaaaaa",sphone);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finishAffinity();
                        startActivity(i);

                    } else {
                        Toast.makeText(OTP.this, "OTP is incorrect", Toast.LENGTH_SHORT).show();
                    }

                }

            }

        });


    }

    private void sendMessage() {


        // String mess = message.getText().toString().trim();

        Random random = new Random();
        // Generates random integers 0 to 49
        x1 = random.nextInt(10);
        x2 = random.nextInt(10);
        x3 = random.nextInt(10);
        x4 = random.nextInt(10);
        x5 = random.nextInt(10);
        x6 = random.nextInt(10);


        String mess = "Dear Customer,your One Time Password (OTP) is " + x1 + x2 + x3 + x4 + x5 + x6 + " to recover the forgotten Password...";

        if (!sphone.equals("") && !mess.equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sphone, null, mess, null, null);
            Toast.makeText(this, "sms SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "enter value first", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            sendMessage();
        } else {
            Toast.makeText(this, "Permisson denied", Toast.LENGTH_SHORT).show();
        }
    }
}
