package com.example.attendance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class Mark_Attendance extends AppCompatActivity {

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;

    int count = 1;


    Button next_Attendance;
    ArrayList<String> mark_text;
    String a;
     int student_count;
    static int sst_count;
    TextView subject, time;

    static String send_subject;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark__attendance);


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();


        time = findViewById(R.id.textView4);
        time.append("" + formatter.format(date));

        Intent ii = getIntent();
        subject = findViewById(R.id.textView5);
        send_subject = ii.getStringExtra("Sub_name_send");

        subject.append(ii.getStringExtra("Sub_name_send"));

        Intent iii = getIntent();
        student_count = Integer.parseInt(iii.getStringExtra("Count_send"));
        sst_count = student_count;

        next_Attendance = (Button) findViewById(R.id.next_attendance);
        mark_text = new ArrayList<>();

        next_Attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Mark_Attendance.this, Absent_Roll_Number.class);
                System.out.println(mark_text);
                i.putStringArrayListExtra("str", mark_text);
                startActivity(i);
                finish();
            }
        });


        al = new ArrayList<>();

        al.add("1");


        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                a = al.get(0);
                al.remove(0);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                mark_text.add(a);

            }

            @Override
            public void onRightCardExit(Object dataObject) {
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                if (student_count != count) {
                    count++;
                    al.add(String.valueOf(count));
                }


                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {


            }
        });

    }


}