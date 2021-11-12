package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Upload extends AppCompatActivity {

    Button ok_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        ok_Button = findViewById(R.id.Ok_button);

        ok_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String ss = new Subject_View().text_send_data;
                    System.out.println("UPLOAD    " + ss);

                    Intent i = new Intent(Upload.this, Subject_View.class);
                    i.putExtra("Upload_ID", ss);
                    startActivity(i);
                    finish();
                } catch (Exception e) {

                }
            }
        });

    }
}
