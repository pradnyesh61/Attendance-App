package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Login_Page extends AppCompatActivity {

    TextView create_account, reset_Password;
    Button login_button;
    EditText username, pass;
    DatabaseReference reff;

    private static String text;
    ProgressBar progressBar;

    CheckBox remember;
    private static final String key_username = "name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);

        progressBar = findViewById(R.id.progressBar);

        remember = findViewById(R.id.checkBox);
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");
        String ID = preferences.getString("ID", "");

        if (checkbox.equals("true")) {
            Intent intent = new Intent(Login_Page.this, Subject_View.class);
            intent.putExtra("ID_keep_login", ID);
            startActivity(intent);
        } else if (checkbox.equals("false")) {
            Toast.makeText(this, "Please sign in...!", Toast.LENGTH_LONG).show();
        }

        username = findViewById(R.id.editText);
        pass = findViewById(R.id.editText2);


        create_account = (TextView) findViewById(R.id.create_account);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_Page.this, Account_Creation.class);
                startActivity(i);
            }
        });



        login_button = (Button) findViewById(R.id.Login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(username.getText())) {
                    username.setError("required!");
                }
                if (TextUtils.isEmpty(pass.getText())) {
                    pass.setError("required!");
                }

                reff = FirebaseDatabase.getInstance().getReference().child("Account_Detail").child(username.getText().toString());

                reff.addValueEventListener(new ValueEventListener() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        try {

                            String phn = datasnapshot.child("phn").getValue().toString();
                            text = phn;

                            String password = datasnapshot.child("password").getValue().toString();

                            if (phn.equals(username.getText().toString()) && password.equals(pass.getText().toString())) {

                                Intent i = new Intent(Login_Page.this, Subject_View.class);
                                i.putExtra("ID", text);
                                startActivity(i);
                                progressBar.setVisibility(View.INVISIBLE);

                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(Login_Page.this, "Username or Password incorrect !", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        reset_Password = findViewById(R.id.forgot_password);
        reset_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(username.getText())) {
                    username.setError("required!");
                } else {
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Account_Detail");



                    dr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(username.getText().toString())) {
                                Intent it = new Intent(Login_Page.this, OTP.class);
                                it.putExtra("contact", username.getText().toString().trim());
                                startActivity(it);
                                username.setText("");

                            }
                            else {
                                Toast.makeText(Login_Page.this, "Account is not exists With this Contact Number ,Please  enter Valid Contact Number ! ", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()) {

                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.putString(key_username, username.getText().toString());
                    editor.apply();

                } else if (!buttonView.isChecked()) {


                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();


                }
            }
        });
    }


}