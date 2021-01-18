package com.example.nehaapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MyViewHolder> {
     ArrayList<File> mpictureList;
    private Context context;
    ClickListener clickListener;

    public PictureAdapter(ArrayList<File> pictureList)
    {
        mpictureList = pictureList;
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_gallery, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final File f = mpictureList.get(position);
        Picasso.with(context).load(f)
                .fit().centerInside()
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context,"Clicked" + f.getName(),Toast.LENGTH_SHORT).show();
                clickListener.onClicked(f.getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mpictureList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
         ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView2);
        }
    }
}
