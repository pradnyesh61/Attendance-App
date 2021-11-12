package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Account_Creation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] departments = {"SELECT DEPARTMENT", "IT", "CSE", "CIVIL", "MECHANICAL", "E&TC", "CHEMICAL"};
    Button create_button;
    Spinner spin_department;

    EditText e_name, e_email, e_create_contact_no, e_password, e_repassword;

    DatabaseReference reff, dr;
    getset gs;

    String temp;
    Long ph;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__creation);
        e_name = findViewById(R.id.editText3);
        e_email = findViewById(R.id.editText4);
        e_create_contact_no = findViewById(R.id.editText6);
        e_password = findViewById(R.id.editText7);
        e_repassword = findViewById(R.id.editText8);

        create_button = findViewById(R.id.Create_account_button);

        spin_department = (Spinner) findViewById(R.id.spinner);
        spin_department.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, departments);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_department.setAdapter(arrayAdapter);


        gs = new getset();

        reff = FirebaseDatabase.getInstance().getReference().child("Account_Detail");

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(e_create_contact_no.getText())) {
                    e_create_contact_no.setError("required!");
                } else {
                    dr = FirebaseDatabase.getInstance().getReference().child("Account_Detail");
                    dr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(e_create_contact_no.getText().toString())) {

                                final AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(Account_Creation.this);
                                builder.setMessage("You indicated you are a new User ,but an account already exists with the mobile phone number +" + e_create_contact_no.getText().toString())
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        });

                                TextView title = new TextView(Account_Creation.this);
// You Can Customise your Title here
                                title.setText("Error");
                                title.setBackgroundColor(Color.DKGRAY);
                                title.setPadding(10, 10, 10, 10);
                                title.setGravity(Gravity.CENTER);
                                title.setTextSize(20);
                                title.setTextColor(Color.WHITE);
                                builder.setCustomTitle(title);

                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                // alert.setTitle("Error");

                                alert.show();

                                Toast.makeText(Account_Creation.this, "Account exist already", Toast.LENGTH_SHORT).show();


                            } else {
                                if (!TextUtils.isEmpty(e_name.getText())) {
                                    if (e_email.getText().toString().trim().matches(emailPattern)) {
                                        if (!temp.equals("SELECT DEPARTMENT")) {
                                            if (!TextUtils.isEmpty(e_password.getText())) {
                                                if (e_password.getText().toString().equals(e_repassword.getText().toString())) {
                                                    try {

                                                        ph = Long.parseLong(e_create_contact_no.getText().toString().trim());
                                                        gs.setDepartment(temp.trim());
                                                        gs.setPassword(e_password.getText().toString().trim());
                                                        gs.setEmail(e_email.getText().toString().trim());
                                                        gs.setName(e_name.getText().toString().trim());
                                                        gs.setPhn(ph);


                                                        reff.child(String.valueOf(ph)).setValue(gs);
                                                        Toast.makeText(Account_Creation.this, "data inserted successfully...!!!", Toast.LENGTH_SHORT).show();

                                                        e_name.setText("");
                                                        e_email.setText("");
                                                        e_create_contact_no.setText("");
                                                        e_password.setText("");
                                                        e_repassword.setText("");


                                                        Intent i = new Intent(Account_Creation.this, Login_Page.class);
                                                        startActivity(i);

                                                    } catch (Exception e) {
                                                        System.out.println("hello ::: " + e);

                                                    }
                                                } else {
                                                    Toast.makeText(Account_Creation.this, "Password not matched !", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                e_password.setError("required!");
                                            }
                                        } else {
                                            ((TextView) spin_department.getSelectedView()).setError("Error message");

                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                                        e_email.setError("required!");
                                    }
                                } else {
                                    e_name.setError("required!");
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // if (!parent.getSelectedItem().toString().equals("SELECT DEPARTMENT"))
        {
            temp = parent.getSelectedItem().toString();

        }
        // else
        {

            //  spin_department.setError("required!");
            //((TextView)spin_department.getSelectedView()).setError("Error message");

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
