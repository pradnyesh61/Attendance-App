package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class header_profile_image_update extends AppCompatActivity {

    private ImageView mImageContainer;
    private Button muploadButton, browse;
    private ProgressBar muploadProgressBar;

    Uri FilePathUri;
    public static Uri text_url;
    int Image_Request_Code = 7;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataview_image_update();

        setContentView(R.layout.activity_header_profile_image_update);

        mImageContainer = findViewById(R.id.imageView);

        muploadButton = findViewById(R.id.button3);
        browse = findViewById(R.id.browse);
        muploadProgressBar = findViewById(R.id.pg);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
            }
        });

        muploadButton.setOnClickListener(mUploadClickHandler);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                mImageContainer.setImageBitmap(bitmap);
            } catch (IOException e) {
            }
        }
    }

    private View.OnClickListener mUploadClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                Bitmap capture = Bitmap.createBitmap(
                        mImageContainer.getWidth(),
                        mImageContainer.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas captureCanvas = new Canvas(capture);
                mImageContainer.draw(captureCanvas);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                capture.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] data = outputStream.toByteArray();

                String path = "firememes/" + Subject_View.text_send_data + ".png";
                final StorageReference firememeRef = storage.getReference(path);


                UploadTask uploadTask = firememeRef.putBytes(data);

                muploadProgressBar.setVisibility(View.VISIBLE);
                muploadButton.setEnabled(false);

                uploadTask.addOnCompleteListener(header_profile_image_update.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                        muploadProgressBar.setVisibility(View.GONE);
                        muploadButton.setEnabled(true);


                        int cc = Integer.parseInt(getIntent().getStringExtra("counter"));


                        if (cc == 1) {
                            Intent i = new Intent(header_profile_image_update.this, Subject_View.class);
                            startActivity(i);
                        }
                        if (cc == 2) {
                            Intent i = new Intent(header_profile_image_update.this, Show_Attendance.class);
                            startActivity(i);
                        }
                    }
                });


                Task<Uri> getDownloadUriTask = uploadTask.continueWithTask(
                        new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                return firememeRef.getDownloadUrl();
                            }
                        }
                );

                getDownloadUriTask.addOnCompleteListener( new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            text_url = downloadUri;
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("images");

                            myRef.child(Subject_View.text_send_data).setValue(String.valueOf(downloadUri));

                            Toast.makeText(header_profile_image_update.this, "Profile Image Updated", Toast.LENGTH_SHORT).show();
                        }

                        muploadProgressBar.setVisibility(View.GONE);
                        muploadButton.setEnabled(true);
                    }
                });


            } catch (Exception e) {

            }

        }


    };


    private void dataview_image_update() {
        try {

            img = FirebaseDatabase.getInstance().getReference("images");
            img.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    try {

                        String phn = datasnapshot.child(Subject_View.text_send_data).getValue().toString();

                        Picasso.get().load(phn).into(mImageContainer);

                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
        }
    }

}

