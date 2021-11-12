package com.example.attendance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProgrammingAdapter extends RecyclerView.Adapter<ProgrammingAdapter.ProgrammingViewHolder> {

    private String[] data;
    private String[] data2;
    private String[] data3;
    private String[] data4;



    private Context context;

    public ProgrammingAdapter(Context context, ArrayList<String> data, ArrayList<String> data2, ArrayList<String> data3, ArrayList<String> data4) {
        this.context = context;
        this.data = data.toArray(new String[0]);
        this.data2 = data2.toArray(new String[0]);
        this.data3 = data3.toArray(new String[0]);
        this.data4 = data4.toArray(new String[0]);

    }


    @NonNull
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProgrammingViewHolder holder, int position) {

        final String title = data[position];
        holder.texttitle.setText(title);

        final String title2 = data2[position];
        holder.texttitle2.setText(title2);

        final String title3 = data3[position];
        holder.texttitle3.setText(title3);

        final String title4 = data4[position];
        holder.texttitle4.setText(title4);


        holder.texttitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    System.out.println(title);
                    Intent i = new Intent(context, Mark_Attendance.class);
                    i.putExtra("Sub_name_send", title);
                    i.putExtra("Count_send", title4);
                    context.startActivity(i);
                }
        });

        holder.texttitle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println(title);
                Intent i = new Intent(context, Mark_Attendance.class);
                i.putExtra("Sub_name_send", title);
                i.putExtra("Count_send", title4);
                context.startActivity(i);
            }
        });

        holder.texttitle4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println(title);
                Intent i = new Intent(context, Mark_Attendance.class);
                i.putExtra("Sub_name_send", title);
                i.putExtra("Count_send", title4);
                context.startActivity(i);
            }
        });


        holder.remove_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you really want to delete this Subject permanently ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseDatabase.getInstance().getReference().child("Subject_Details").child(Subject_View.text_send_data).child(title).removeValue();
                                 FirebaseDatabase.getInstance().getReference().child("Attendance").child(Subject_View.text_send_data).child(title).removeValue();
                                Intent i = new Intent(context,Subject_View.class);
                                context.startActivity(i);

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
              //  alert.setTitle("AlertDialogExample");

                alert.show();

            }
        });


    }


    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ProgrammingViewHolder extends RecyclerView.ViewHolder {

        TextView texttitle;
        TextView texttitle2;
        TextView texttitle3;
        TextView texttitle4;
        ImageButton remove_Button;

        public ProgrammingViewHolder(@NonNull View itemView) {
            super(itemView);

            texttitle = (TextView) itemView.findViewById(R.id.textTitle);
            texttitle2 = (TextView) itemView.findViewById(R.id.textTitle2);
            texttitle3 = (TextView) itemView.findViewById(R.id.textTitle3);
            texttitle4 = (TextView) itemView.findViewById(R.id.textTitle4);


            remove_Button = itemView.findViewById(R.id.Image_Button_remove);


        }
    }
}