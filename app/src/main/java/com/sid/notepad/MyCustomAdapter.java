package com.sid.notepad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Sid on 01-09-2016.
 */
public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MyViewHolder>{

    Context context;
    Context c;
    ArrayList<Information> data;
    LayoutInflater inflator;

    public MyCustomAdapter(Context context, ArrayList<Information> data) {

        this.context = context;
        this.data = data;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflator.inflate(R.layout.list_item_row , parent , false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        final String temp_title = data.get(position).title;
        final String temp_content = data.get(position).content;
        final String temp_synced = data.get(position).isSynced;
        holder.tv1.setText(temp_title);
        holder.tv2.setText(temp_content);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context , temp_title + "  selected  " + MainActivity.globalUsername, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context , AddNote.class);
                intent.putExtra("by", "adapter");
                intent.putExtra("title" , temp_title);
                intent.putExtra("content" , temp_content);
                intent.putExtra("pos" , position);
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });

            if (temp_synced.equals("false")){
                holder.imageView.setVisibility(View.GONE);
            }

    }




    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv1, tv2;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv1 = (TextView) itemView.findViewById(R.id.title_row);
            tv2 = (TextView) itemView.findViewById(R.id.content_row);
            imageView = (ImageView) itemView.findViewById(R.id.sync_ic);
        }
    }


}


