package com.example.nehaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class TemplateGalleryActivity extends AppCompatActivity implements ClickListener {
    RecyclerView recyclerView;
    private PictureAdapter pictureAdapter;
    ArrayList<String> pictureList;
    private ArrayList<File> localPictureList;


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
        updateAdapter(pictureList);

    }


    public void updateAdapter(final ArrayList<String> pictureList){
        final ArrayList<File>  localPictureList = new ArrayList<>();
        int i = 1;
        for(String picture : pictureList){
            pictureAdapter = new PictureAdapter(localPictureList);
            pictureAdapter.setClickListener(this);

            StorageReference pathReference = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(picture);
            final File localFile = new File(getApplicationContext().getCacheDir().getAbsolutePath() +
                    "/" + "templateimage"  + i + ".jpg");
            final int finalI = i;
            pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    localPictureList.add(localFile);
                    if(localPictureList.size() >= pictureList.size()) {
                        setContentView(R.layout.recycler_layout);
                        recyclerView = findViewById(R.id.recycler_layout);

                        RecyclerView.LayoutManager manager =
                                new GridLayoutManager(getApplicationContext(), 2);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                                LinearLayoutManager.VERTICAL));
                        recyclerView.setAdapter(pictureAdapter);
                        pictureAdapter.notifyDataSetChanged();
                    }

                }
            });
            i++;
        }


    }
    @Override
    public void onClicked(String fname) {
        //Toast.makeText(this,"I am templategallery activity",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("filename",fname);
        setResult(RESULT_OK, intent);
        finish();
    }
}
