package com.example.attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Absent_Roll_Number extends AppCompatActivity {

    TextView absent_roll_view;
    Button save_and_upload;
    ArrayList<String> aall;

    AlertDialog.Builder builder;
    String name;
    String[] str;

    DatabaseReference reff;

    String ERP_API;

    SimpleDateFormat dated;
    SimpleDateFormat time;

    String date_upload, time_upload;

    List<String> al;
    List<String> A;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent__roll__number);

        dated = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        date_upload = dated.format((date));


        time = new SimpleDateFormat("HH:mm:ss");
        Date time1 = new Date();

        time_upload = time.format(time1);

        absent_roll_view = findViewById(R.id.absent_roll_view);
        save_and_upload = findViewById(R.id.save_and_upload);

        aall = getIntent().getStringArrayListExtra("str");

        for (int j = 0; j < aall.size(); j++) {
            absent_roll_view.append(aall.get(j) + ",");
        }

        builder = new AlertDialog.Builder(this);

        name = getIntent().getStringExtra("Teacher");

        save_and_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                str = absent_roll_view.getText().toString().split(",");
                al = new ArrayList<String>();
                al = Arrays.asList(str);

                String ss = String.valueOf(Mark_Attendance.sst_count);


                A = new ArrayList<String>();


                for (int i = 1; i <= Mark_Attendance.sst_count; i++) {
                    A.add(String.valueOf(i));
                }
                System.out.println("A   " + A);


                A.retainAll(al);




                {
                    builder.setMessage("Do you want to upload the attendance ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                    System.out.println("Abefore   " + A);
                                    insert_attendance();
                                    System.out.println("Aafter   " + A);
                                    Intent i = new Intent(Absent_Roll_Number.this, Upload.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    Intent i = new Intent(Absent_Roll_Number.this, Subject_View.class);
                                    startActivity(i);

                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("AlertDialogExample");
                    alert.show();

                }

            }
        });
    }

    public void insert_attendance() {
        reff = FirebaseDatabase.getInstance().getReference().child("Attendance");

        if (A.isEmpty())
        {
            reff.child(Subject_View.text_send_data).child(Mark_Attendance.send_subject).child(date_upload).child(time_upload).setValue("[]");
        }
        else {

            reff.child(Subject_View.text_send_data).child(Mark_Attendance.send_subject).child(date_upload).child(time_upload).setValue(A);

        }
        Toast.makeText(this, "attendance uploaded...", Toast.LENGTH_SHORT).show();
    }
}


