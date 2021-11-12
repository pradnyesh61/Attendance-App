package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Create_Subject extends AppCompatActivity {

    Button create_subject;
    Spinner spin_year, spin_department_sub, spin_sem;

    String[] department_sub_array = {"SELECT DEPARTMENT", "IT", "CSE", "CIVIL", "MECHANICAL", "E&TC", "CHEMICAL"};
    String[] year_array = {"SELECT YEAR", "First Year", "Second Year", "Third Year", "Forth Year"};
    String[] sem_array = {"SELECT SEM", "I", "II", "III", "IV", "V", "VI", "VII", "VIII"};


    EditText e_subject, e_student_cont;

    DatabaseReference reff;
    getset gs;
    String temp_spin_year, temp_spin_department_sub, temp_spin_sem;

    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__subject);
        Intent ii = getIntent();
        ID = ii.getStringExtra("ID");

        e_subject = findViewById(R.id.subject_name);
        e_student_cont = findViewById(R.id.Student_Count);

        create_subject = findViewById(R.id.create_subject_button);

        gs = new getset();

        reff = FirebaseDatabase.getInstance().getReference().child("Subject_Details").child("" + ID);

        create_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(e_subject.getText())) {
                    e_subject.setError("required!");
                }else {
                    if (TextUtils.isEmpty(e_student_cont.getText())) {
                        e_student_cont.setError("required!");
                    } else {
                        if (!temp_spin_department_sub.equals("SELECT DEPARTMENT")) {
                            if (!temp_spin_year.equals("SELECT YEAR")) {
                                if (!temp_spin_sem.equals("SELECT SEM")) {

                                    if (Integer.parseInt(e_student_cont.getText().toString()) <= 300) {
                                        try {
                                            Long student_count = Long.parseLong(e_student_cont.getText().toString().trim());
                                            if (student_count != 0) {

                                                gs.setSubject_year(temp_spin_year.trim());
                                                gs.setSubject_sem(temp_spin_sem.trim());
                                                gs.setSubject_department(temp_spin_department_sub.trim());
                                                gs.setSubject_name(e_subject.getText().toString().trim());
                                                gs.setStudent_count(student_count);


                                                reff.child(e_subject.getText().toString()).setValue(gs);
                                                Toast.makeText(Create_Subject.this, "Subject created successfully...!!!", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(Create_Subject.this, Subject_View.class);
                                                i.putExtra("SUB_ID", ID);
                                                startActivity(i);

                                            } else {
                                                Toast.makeText(Create_Subject.this, "Student Count can't be Zero !", Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (Exception e) {
                                        }

                                    } else {
                                        Toast.makeText(Create_Subject.this, "Student Count can't be more than 300 !", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    ((TextView) spin_sem.getSelectedView()).setError("Error message");
                                }

                            } else {
                                ((TextView) spin_year.getSelectedView()).setError("Error message");
                            }
                        } else {
                            ((TextView) spin_department_sub.getSelectedView()).setError("Error message");
                        }
                    }
                }

            }
        });

        spin_department_sub = (Spinner) findViewById(R.id.department_spinner_create_subject);
        spin_year = (Spinner) findViewById(R.id.year_spinner);
        spin_sem = (Spinner) findViewById(R.id.sem_spinner);

        spin_department_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  if (!parent.getSelectedItem().toString().equals("SELECT DEPARTMENT"))
                {
                    temp_spin_department_sub = parent.getSelectedItem().toString();
                }
                //else
                {
                    //   ((TextView)spin_department_sub.getSelectedView()).setError("Error message");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // if (!parent.getSelectedItem().toString().equals("SELECT YEAR"))
                {
                    temp_spin_year = parent.getSelectedItem().toString();

                }
                //else
                {
                    //  ((TextView)spin_year.getSelectedView()).setError("Error message");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //if (!parent.getSelectedItem().toString().equals("SELECT SEM"))
                {
                    temp_spin_sem = parent.getSelectedItem().toString();

                }
                //   else
                {
                    //((TextView)spin_sem.getSelectedView()).setError("Error message");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter arrayAdapter_department_sub = new ArrayAdapter(this, android.R.layout.simple_spinner_item, department_sub_array);
        arrayAdapter_department_sub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_department_sub.setAdapter(arrayAdapter_department_sub);

        ArrayAdapter arrayAdapter_year = new ArrayAdapter(this, android.R.layout.simple_spinner_item, year_array);
        arrayAdapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_year.setAdapter(arrayAdapter_year);

        ArrayAdapter arrayAdapter_sem = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sem_array);
        arrayAdapter_sem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_sem.setAdapter(arrayAdapter_sem);

    }

}
