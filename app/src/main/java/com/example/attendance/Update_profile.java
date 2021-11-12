package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Update_profile extends AppCompatActivity {

    EditText e_name, e_email, e_password;
    TextView e_create_contact_no;
    Button b1;
    DatabaseReference reff, dr;
    getset gs;
    Subject_View sv;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        e_name = findViewById(R.id.e_name);
        e_email = findViewById(R.id.e_email);
        e_create_contact_no = findViewById(R.id.e_contact);
        e_password = findViewById(R.id.e_pass);

        b1 = findViewById(R.id.button2);
        gs = new getset();

        sv = new Subject_View();

        e_create_contact_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Update_profile.this, "user - id cannot be changed", Toast.LENGTH_SHORT).show();
            }
        });


        reff = FirebaseDatabase.getInstance().getReference().child("Account_Detail/" + sv.text_send_data);
        reff.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                try {
                    String phn = datasnapshot.child("phn").getValue().toString();
                    String name = datasnapshot.child("name").getValue().toString();
                    String email = datasnapshot.child("email").getValue().toString();
                    String pass = datasnapshot.child("password").getValue().toString();

                    e_name.setText(name);
                    e_create_contact_no.setText(phn);
                    e_email.setText(email);
                    e_password.setText(pass);


                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dr = FirebaseDatabase.getInstance().getReference().child("Account_Detail");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(e_name.getText())) {
                    e_name.setError("required!");

                }
                if (TextUtils.isEmpty(e_password.getText())) {
                    e_password.setError("required!");

                }

                if (e_email.getText().toString().trim().matches(emailPattern)) {

                    try {
                        dr.child(sv.text_send_data + "/name").setValue(e_name.getText().toString());
                        dr.child(sv.text_send_data + "/email").setValue(e_email.getText().toString());
                        dr.child(sv.text_send_data + "/password").setValue(e_password.getText().toString());


                        Intent i = new Intent(Update_profile.this, Subject_View.class);
                        Toast.makeText(Update_profile.this, "Updated profile", Toast.LENGTH_LONG).show();
                        startActivity(i);
                    } catch (Exception e) {

                    }
                } else {
                        Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                        e_email.setError("required!");
                    }
            }
        });

    }
}
