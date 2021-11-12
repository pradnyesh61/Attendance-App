package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellSignalStrength;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Forgot_Password extends AppCompatActivity {

    EditText repass, repass1;
    TextView un_pass;
    Button b;
    DatabaseReference reff;
    String name_p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);

        un_pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        repass1 = findViewById(R.id.repass1);

        Intent i = getIntent();
        name_p = i.getStringExtra("contactaaaaa");

        un_pass.setText(name_p);

        b = findViewById(R.id.button_forgot);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(repass.getText())) {
                    repass.setError("required!");
                } else if (TextUtils.isEmpty(repass1.getText())) {
                    repass1.setError("required!");
                }else if (repass.getText().toString().equals(repass1.getText().toString())) {
                        try {

                            reff = FirebaseDatabase.getInstance().getReference().child("Account_Detail").child(name_p);
                            reff.child("password").setValue(repass.getText().toString());
                            Toast.makeText(Forgot_Password.this, "Password Reset successfully...!!!", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Forgot_Password.this, Login_Page.class);
                            finishAffinity();
                            startActivity(i);


                        } catch (Exception e) {
                            Toast.makeText(Forgot_Password.this, "something went wrong", Toast.LENGTH_LONG).show();
                        }

                    } else {

                        Toast.makeText(Forgot_Password.this, "password not matached", Toast.LENGTH_LONG).show();
                    }


            }
        });
    }

}
