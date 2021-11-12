package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Script;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.ChildrenNode;
import com.squareup.picasso.Picasso;

import java.security.PublicKey;
import java.util.ArrayList;

public class Subject_View extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;

    View hview;
    TextView h_Name, h_contact, h_email;
    ImageView h_image;

    Button add_subject_button;
    public String ID;



    RecyclerView programingList;
    ArrayList<String> scripts;
    static ArrayList<String> scripts_send_data;
    ArrayList<String> scripts2;
    ArrayList<String> scripts3;
    ArrayList<String> scripts4;


    public static String text_send_data;

    DatabaseReference reff,dr;
    DatabaseReference img;

    private static final String key_username = "name";

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject__view);


        progressBar = findViewById(R.id.progressBar);


        Intent ii = getIntent();
        ID = ii.getStringExtra("ID");

        if (ID == null) {
            Intent i = getIntent();
            ID = i.getStringExtra("SUB_ID");
        }

        if (ID == null) {
            Intent i = getIntent();
            ID = i.getStringExtra("Upload_ID");
        }
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String name = preferences.getString(key_username, null);
        if (name != null) {
            ID = name;
        }
        text_send_data = ID;

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
                Intent i = new Intent(Subject_View.this, header_profile_image_update.class);
                i.putExtra("counter", "1");
                startActivity(i);
            }
        });
        dataview_image_update();


        nevigationBar_menu();

        add_subject_button = findViewById(R.id.add_subject_button);

        add_subject_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Subject_View.this, Create_Subject.class);

                i.putExtra("ID", ID);
                startActivity(i);
            }
        });


        programingList = findViewById(R.id.programmingList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        programingList.setLayoutManager(gridLayoutManager);

        scripts = new ArrayList<String>();
        scripts2 = new ArrayList<String>();
        scripts3 = new ArrayList<String>();
        scripts4 = new ArrayList<String>();
        scripts_send_data = new ArrayList<String>();
        dataview_account();
        dataview_subject();

    }


    private void dataview_account() {

        reff = FirebaseDatabase.getInstance().getReference().child("Account_Detail/" + text_send_data);
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



    private void dataview_subject() {


        dr =  FirebaseDatabase.getInstance().getReference().child("Subject_Details/"+ID);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    progressBar.setVisibility(View.VISIBLE);

                    reff = FirebaseDatabase.getInstance().getReference().child("Subject_Details/"+ID);


                    reff.addChildEventListener(new ChildEventListener() {

                        @Override
                        public void onChildAdded(@NonNull DataSnapshot datasnapshot, @Nullable String previousChildName) {


                            String SUB_NAME = datasnapshot.child("subject_name").getValue().toString();
                            String STD_COUNT = datasnapshot.child("student_count").getValue().toString();
                            String SUB_DEPART = datasnapshot.child("subject_department").getValue().toString();
                            String SUB_SEM = datasnapshot.child("subject_sem").getValue().toString();
                            String SUB_YEAR = datasnapshot.child("subject_year").getValue().toString();


                            scripts.add(SUB_NAME);
                            scripts2.add(SUB_YEAR);
                            scripts3.add(SUB_DEPART);
                            scripts4.add(STD_COUNT);
                            scripts_send_data.add(SUB_NAME);
                            programingList.setAdapter(new ProgrammingAdapter(Subject_View.this, scripts, scripts2, scripts3, scripts4));


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
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void setUpToolBar() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);  // in style we apply no action bar so now action bar will be appear as toolbar and because of this line only the app name is visible on toolbar
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
                        finalDialog = new AlertDialog.Builder(Subject_View.this) // Pass a reference to your main activity here
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
                        break;

                    case R.id.Update_Profile:
                        Intent i = new Intent(Subject_View.this, Update_profile.class);
                        startActivity(i);
                        break;

                    case R.id.logout:
                        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", "false");
                        editor.clear();
                        editor.apply();
                        finishAffinity();
                        Intent ii = new Intent(Subject_View.this, Login_Page.class);
                        startActivity(ii);
                        break;

                    case R.id.show_attendance:
                        Intent iii = new Intent(Subject_View.this, Show_Attendance.class);
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
                    String phn = datasnapshot.child(text_send_data).getValue().toString();

                    Picasso.get().load(phn).into(h_image);

                } catch (Exception e) {


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
