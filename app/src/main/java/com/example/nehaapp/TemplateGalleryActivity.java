package com.example.nehaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class TemplateGalleryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private PictureAdapter pictureAdapter;
    ArrayList<String> pictureList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pictureList = new ArrayList();
        pictureList.add("gs://drawing-app-72e25.appspot.com/elephant.png");
        pictureList.add("gs://drawing-app-72e25.appspot.com/Sunflower.jpg");
        pictureList.add("gs://drawing-app-72e25.appspot.com/bear.jpg");
        pictureList.add("gs://drawing-app-72e25.appspot.com/cat.jpg");
        pictureList.add("gs://drawing-app-72e25.appspot.com/fish.png");
        pictureList.add("gs://drawing-app-72e25.appspot.com/leaf.jpg");
        pictureList.add("gs://drawing-app-72e25.appspot.com/pony.jpg");
        pictureList.add("gs://drawing-app-72e25.appspot.com/turtle.png");
        pictureList.add("gs://drawing-app-72e25.appspot.com/whale.jpg");




        setContentView(R.layout.recycler_layout);
        recyclerView = findViewById(R.id.recyler_layout);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        pictureAdapter = new PictureAdapter(pictureList);

    }
}
