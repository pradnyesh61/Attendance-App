package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ERP_Login extends AppCompatActivity {

    Button erp_connect;
    EditText erp_Api;
    EditText erp_teacher;

    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_r_p__login);


        erp_connect = findViewById(R.id.Connect_Erp);
        erp_Api = findViewById(R.id.erp_Api);
        erp_teacher = findViewById(R.id.erp_teacher);

        erp_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                API_insert();

                Intent i = new Intent(ERP_Login.this,Absent_Roll_Number.class);
                startActivity(i);
            }
        });

    }


    public void API_insert() {


        reff = FirebaseDatabase.getInstance().getReference().child("API");


        reff.child(Subject_View.text_send_data).setValue(erp_teacher.getText().toString());

        Toast.makeText(this, "attendance uploaded...", Toast.LENGTH_SHORT).show();

    }
}
