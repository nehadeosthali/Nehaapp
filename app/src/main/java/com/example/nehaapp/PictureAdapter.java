package com.example.nehaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MyViewHolder> {
     ArrayList<File> mpictureList;
    private Context context;

    public PictureAdapter(ArrayList<File> pictureList) {
        mpictureList = pictureList;
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
        Picasso.with(context).load(mpictureList.get(position))
                .fit().centerCrop()
                .into(holder.imageView);

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
