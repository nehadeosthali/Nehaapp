package com.example.nehaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class SavedPicturesGalleryActivity extends TemplateGalleryActivity {
    RecyclerView recyclerView;
    private PictureAdapter PictureAdapter;
    private ArrayList<String> savedPictures;
    private ArrayList<String> pictureList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_layout);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users");
        final String email = user.getEmail().replace(".", ",");
        myRef.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseUser databaseUser = (DatabaseUser) dataSnapshot.getValue(DatabaseUser.class);

                savedPictures = new ArrayList();
                if (databaseUser.getSavedPictures() != null) {
                    savedPictures = databaseUser.getSavedPictures();
                    pictureList = new ArrayList<>();
                    for(String picture:savedPictures){
                        pictureList.add("gs://drawing-app-72e25.appspot.com/"+email+"/"+picture);

                    }
                    updateAdapter(pictureList);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



}
