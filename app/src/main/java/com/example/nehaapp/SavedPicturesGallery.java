package com.example.nehaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class SavedPicturesGallery extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList <String> pictures;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_gallery);


    }
}
