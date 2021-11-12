package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class Show_Attendance extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;
    ProgressBar progressBar;
    DatabaseReference img;
    View hview;
    TextView h_Name, h_contact, h_email;
    ImageView h_image;

    String subject_fetch;
    Button date_fetch;
   static String date;

    ListView listView;
    ArrayAdapter<String> adapter;


    Button b1;

    DatabaseReference reff, dr;


    private DatePickerDialog.OnDateSetListener dateSetListener;

    ArrayList<String> departments = Subject_View.scripts_send_data;

    Spinner spin_department;

    static String temp;
    static String[] manu;

    static long student_count_roll_no;
    AlertDialog.Builder builder;

    private static final int MY_PERMISSIONS_CONSTANT = 123 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__attendance);
        builder = new AlertDialog.Builder(this);

        b1 = findViewById(R.id.button_check);

        listView = findViewById(R.id.listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                builder.setMessage("Do you really want to download the attendance ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                temp = adapter.getItem(position);

                                temp = temp.replaceAll("\\[", "").replaceAll("\\]", "");

                                temp = temp.replaceAll(" ", "");

                                manu = temp.split(",");


                                final List<String> list = new ArrayList<String>();
                                Collections.addAll(list, manu);
                                list.remove(list.get(0));
                                String time_file = list.get(0);

                                list.remove(list.get(0));
                                manu = list.toArray(new String[list.size()]);
                                temp = String.valueOf(manu);

                                time_file = time_file.replaceAll(":", "-");

                                saveExcelFile(Show_Attendance.this, subject_fetch + "-" + date + "-" + time_file + ".xls");


                                Toast.makeText(Show_Attendance.this, "Attendance downloaded Successfully ! ", Toast.LENGTH_SHORT).show();


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Download");
                alert.show();


            }
        });

        spin_department = findViewById(R.id.subject_check);
        spin_department.setOnItemSelectedListener(this);


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, departments);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_department.setAdapter(arrayAdapter);

        registerForContextMenu(listView);

        date_fetch = findViewById(R.id.date_check);


        date_fetch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Show_Attendance.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }

        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

              //  System.out.println("11111111 ===== final date ====   " + dayOfMonth+" / "+ month + " / "+ year);
                String month_change = String.valueOf(month);
                String day_change = String.valueOf(dayOfMonth);

                month_change = String.valueOf(month + 1);
              //  System.out.println("222222 ===== final date ====   " + dayOfMonth+" / "+ month + " / "+ year);

                if ((month+1) < 10) {
                    month_change = "0" + month;
                }

                if (dayOfMonth < 10) {
                    day_change = "0" + dayOfMonth;
                }

             //   System.out.println("----------date ==== "+ date);
             //   System.out.println("33333 ===== final date ====   " + dayOfMonth+" / "+ month + " / "+ year);
                date = String.format("%s-%s-%d", day_change, month_change, year);
             //   System.out.println("----------date ==== "+ date);


            }
        };


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (date == null) {
                    Toast.makeText(Show_Attendance.this, "Please Select the Date !", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.clear();
                    dataview_subject();
                    adapter.notifyDataSetChanged();
                }
            }


        });


        listView.setAdapter(adapter);


        progressBar = findViewById(R.id.progressBar);
        navigationView = findViewById(R.id.navigationView);

        setUpToolBar();
        navigationView = findViewById(R.id.navigationView);
        hview = navigationView.getHeaderView(0);
        h_contact = hview.findViewById(R.id.h_contact_number);
        h_email = hview.findViewById(R.id.h_email);
        h_Name = hview.findViewById(R.id.h_name);
        h_image = hview.findViewById(R.id.profile_image_circular);

        h_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Show_Attendance.this, header_profile_image_update.class);
                i.putExtra("counter", "2");
                startActivity(i);
            }
        });
        dataview_image_update();
        dataview_account();
        nevigationBar_menu();


    }


    private void dataview_subject() {

        subject_count_fetch();

        dr = FirebaseDatabase.getInstance().getReference().child("Attendance/" + Subject_View.text_send_data).child(subject_fetch).child(date);

        dr.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    progressBar.setVisibility(View.VISIBLE);


                    reff = FirebaseDatabase.getInstance().getReference().child("Attendance/" + Subject_View.text_send_data).child(subject_fetch).child(date);

                    progressBar.setVisibility(View.VISIBLE);
                    reff.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot datasnapshot, @Nullable String previousChildName) {

                            String SUB_NAME = datasnapshot.getValue().toString();
                            if (SUB_NAME.equals("[]")) {
                                SUB_NAME = "No One Absent";
                            }

                            //adapter.add(SUB_NAME);
                            adapter.add(date + "," + datasnapshot.getKey() + "," + SUB_NAME);

                            progressBar.setVisibility(View.INVISIBLE);
                        }


                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    Toast.makeText(Show_Attendance.this, "No Data found !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void setUpToolBar() {

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    public void nevigationBar_menu() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.About:
                        final AlertDialog finalDialog;
                        finalDialog = new AlertDialog.Builder(Show_Attendance.this) // Pass a reference to your main activity here
                                .setTitle("About")
                                .setMessage("1) This app can Only use by the Teachers ,Students cannot use this app \n" +
                                        "2)Swipe left to mark the attendee absent or right to mark present\n" +
                                        "3)In Attendance only absent roll numbers are shown.\n" +
                                        "4)To download the attendance click on the attendance and choose one.\n"+
                                        "5)Downloaded attendance will be stored at your file/storage/emulated/0/Download/")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                        Toast.makeText(Show_Attendance.this, "Clicked About", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.Update_Profile:
                        Toast.makeText(Show_Attendance.this, "Clicked Update_Profile", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Show_Attendance.this, Update_profile.class);
                        startActivity(i);
                        break;

                    case R.id.logout:
                        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", "false");
                        editor.clear();
                        editor.apply();
                        finishAffinity();
                        Intent ii = new Intent(Show_Attendance.this, Login_Page.class);
                        startActivity(ii);
                        break;

                    case R.id.home:
                        Intent iii = new Intent(Show_Attendance.this, Subject_View.class);
                        startActivity(iii);
                }
                return false;
            }
        });
    }

    private void dataview_image_update() {


        img = FirebaseDatabase.getInstance().getReference("images");
        img.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                try {
                    String phn = datasnapshot.child(Subject_View.text_send_data).getValue().toString();

                    Picasso.get().load(phn).into(h_image);

                } catch (Exception e) {


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        subject_fetch = parent.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void dataview_account() {

        reff = FirebaseDatabase.getInstance().getReference().child("Account_Detail/" + Subject_View.text_send_data);
        reff.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                try {
                    String phn = datasnapshot.child("phn").getValue().toString();
                    String name = datasnapshot.child("name").getValue().toString();
                    String email = datasnapshot.child("email").getValue().toString();

                    h_Name.setText(name);
                    h_contact.setText(phn);
                    h_email.setText(email);


                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void subject_count_fetch() {

        DatabaseReference reffe;

        reffe = FirebaseDatabase.getInstance().getReference().child("Subject_Details/" + Subject_View.text_send_data).child(subject_fetch);

        reffe.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                String STD_COUNT = datasnapshot.child("student_count").getValue().toString();
                student_count_roll_no = Long.parseLong(STD_COUNT);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private static boolean saveExcelFile(Context context, String fileName) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(context, " Allow the Storage Permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_CONSTANT);
            return false;
        }

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.w("FileUtils", "Storage not available or read only");
            return false;
        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;


        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Attendance");


        // Generate column headings
        Row row = sheet1.createRow(0);


        c = row.createCell(0);
        c.setCellValue("Roll Number");


        c = row.createCell(1);
        c.setCellValue("Status");

        String[] roll_count = new String[Math.toIntExact(student_count_roll_no)];
        int j = 0;
        for (int i = 1; i <= student_count_roll_no; i++) {

            roll_count[j] = String.valueOf(i);
            j++;
        }


        List<String> list1 = new ArrayList<String>();
        Collections.addAll(list1, manu);




        int rownum = 2;
        int rownum2 = 2;


        int jj = 1;
        for (int i = 0; i < roll_count.length; i++) {

            Row row1 = sheet1.createRow(rownum++);
            Row row2 = sheet1.createRow(rownum2++);


            Cell cellTitle = row1.createCell(0);
            Cell cellTitle2 = row2.createCell(1);

            cellTitle.setCellValue(roll_count[i]);


            boolean ans = list1.contains("" + jj);
            jj++;


            if (ans) {

                cellTitle2.setCellValue("Absent");

            } else {

                cellTitle2.setCellValue("Present");
            }

        }


        sheet1.setColumnWidth(0, (15 * 300));
        sheet1.setColumnWidth(1, (15 * 300));
        sheet1.setColumnWidth(2, (15 * 300));

        // Create a path where we will place our List of objects on external storage
      //  File file = new File(context.getExternalFilesDir(null), fileName);

        File file  = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);



        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }

        return success;
    }


    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
