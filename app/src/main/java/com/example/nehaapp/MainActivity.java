package com.example.nehaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "NehaappMainActivity";
    private CanvasNeha canvasNeha;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button colorpickerdialog;
    private ToggleButton zoomButton;
    private SeekBar seekbar;
    private int color;
    private String fileName;
    private StorageReference pathReference;
    private AlertDialog.Builder builder;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private GoogleSignInClient mGoogleSignInClient;




    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.d(TAG,"onCreate method called");
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle(mAuth.getCurrentUser().getEmail());

        canvasNeha = findViewById(R.id.canvasNeha);
        canvasNeha.setPAINT_ALPHA(100);
        canvasNeha.setBrushwidth(30);
        canvasNeha.setBrushColor(000000);

        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(this);

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this);

        button3 = findViewById(R.id.button3);
        button3.setOnClickListener(this);

        button4 = findViewById(R.id.button4);
        button4.setOnClickListener(this);

        button5 = findViewById(R.id.button5);
        button5.setOnClickListener(this);

        button6 = findViewById(R.id.button6);
        button6.setOnClickListener(this);

        colorpickerdialog = findViewById(R.id.colorpickerdialog);
        colorpickerdialog.setOnClickListener(this);

        zoomButton = findViewById(R.id.zoombutton);
        zoomButton.setOnClickListener(this);


        fileName = UUID.randomUUID().toString() + getString(R.string.image_extension_jpg);
        //loadCanvaswithBackground();


//        Button clearbutton = findViewById(R.id.clearButton);
//        clearbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Clickedon clear button",Toast.LENGTH_SHORT).show();
//                canvasNeha.clear();
//            }
//        });

    }


    private void updateUI(FirebaseUser account) {

    }

    @Override
    public void onStart() {
        super.onStart();


    }



    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (item.getItemId()) {
          /*  case R.id.ic_undo:
                canvasNeha.undo();
                return true;
            case R.id.ic_redo:
                canvasNeha.redo();
                return true;*/
            case R.id.toolbarmenucontainer:
                final AlertDialog.Builder Builder = new AlertDialog.Builder(this);
                View Layout = inflater.inflate(R.layout.color,null);
                builder.setView(Layout);
           /* case R.id.ic_clear:
                canvasNeha.clear();
                return true;*/
            case R.id.ic_save:
                canvasNeha.uploadFile(fileName);
                return true;
            case R.id.ic_erase:
                canvasNeha.erase();
                return true;
            case R.id.ic_signOut:
                signOut();

            case R.id.brush_size:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View layout = inflater.inflate(R.layout.brush_size,null);
                builder.setView(layout);

                builder.show();

                ImageView brushSmall = layout.findViewById(R.id.brush_small);
                ImageView brushMedium = layout.findViewById(R.id.brush_medium);
                ImageView brushLarge = layout.findViewById(R.id.brush_large);

                brushSmall.setImageDrawable(getResources().getDrawable(R.drawable.brush));
                brushMedium.setImageDrawable(getResources().getDrawable(R.drawable.brush));
                brushLarge.setImageDrawable(getResources().getDrawable(R.drawable.brush));

                brushSmall.setOnClickListener((View.OnClickListener) this);
                brushMedium.setOnClickListener((View.OnClickListener) this);
                brushLarge.setOnClickListener((View.OnClickListener) this);
                return true;
            case R.id.ic_templates:
                Intent intent  = new Intent(this,TemplateGalleryActivity.class);
                startActivityForResult(intent,100);
                return true;
            case R.id.ic_save_gallery:
                Intent Intent  = new Intent(this, SavedPicturesGalleryActivity.class);
                startActivityForResult(Intent, 200);
                return true;

            case R.id.ic_transparency:
                final AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
                View transparencyLayout = inflater.inflate(R.layout.transparency_slider,null);
                mbuilder.setView(transparencyLayout);

                mbuilder.show();

                seekbar = transparencyLayout.findViewById(R.id.seekBar);
                seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Log.d(TAG,"onProgressChanged ");
                        if (progress == 0){
                            canvasNeha.setPAINT_ALPHA(10);
                        }
                        else if (progress > 0 & progress <=50){
                            canvasNeha.setPAINT_ALPHA(progress+100);
                        }
                        else if (progress > 50 & progress <=100){
                            canvasNeha.setPAINT_ALPHA(progress+155);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Log.d(TAG,"onStartTrackingTouch ");
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Log.d(TAG,"onStopTrackingTouch ");
                    }
                });

        }

        return true;

    }
    private void signOut(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();
                        Intent loginIntent = new Intent(getApplicationContext(), com.example.nehaapp.LoginActivity.class);
                        startActivity(loginIntent);
                        // ...
                    }
                });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100 || requestCode == 200 ) {
            Toast.makeText(this,
                    "Main actvity:" + data.getStringExtra("filename"),
                    Toast.LENGTH_SHORT).show();
            convertFiletoBitmap(data.getStringExtra("filename"));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void convertFiletoBitmap(String filename){

        String path = getApplicationContext().getCacheDir().getAbsolutePath() ;
        final File fileimage = new File(path + "/" + filename);
                FileInputStream is = null;
                try {
                    is = new FileInputStream(fileimage);

                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    LoadedImage loadedImage = new LoadedImage(bmp,null,null);
                    //canvasNeha.setBitmap(bmp);
                    canvasNeha.startCanvas(loadedImage);
                    is.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbarmenucontainer:

            case R.id.brush_small:
                canvasNeha.setBrushwidth(30);
                break;
            case R.id.brush_medium:
                canvasNeha.setBrushwidth(40);
                break;
            case R.id.brush_large:
                canvasNeha.setBrushwidth(50);
                break;
            case R.id.button1:
                color = ((ColorDrawable)button1.getBackground()).getColor();
                canvasNeha.setBrushColor(color);
                break;
            case R.id.button2:
                color = ((ColorDrawable)button2.getBackground()).getColor();
                canvasNeha.setBrushColor(color);
                break;
            case R.id.button3:
                color = ((ColorDrawable)button3.getBackground()).getColor();
                canvasNeha.setBrushColor(color);
                break;
            case R.id.button4:
                color = ((ColorDrawable)button4.getBackground()).getColor();
                canvasNeha.setBrushColor(color);
                break;
            case R.id.button5:
                color = ((ColorDrawable)button5.getBackground()).getColor();
                canvasNeha.setBrushColor(color);
                break;
            case R.id.button6:
                color = ((ColorDrawable)button6.getBackground()).getColor();
                canvasNeha.setBrushColor(color);
                break;
            case R.id.colorpickerdialog:
                new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton("Confirm",
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        canvasNeha.setBrushColor(envelope.getColor());
                                        //setLayoutColor(envelope);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                        .attachAlphaSlideBar(true) // default is true. If false, do not show the AlphaSlideBar.
                        .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                        .show();
                break;

            case R.id.zoombutton:
                if (zoomButton.isChecked()) {
                    zoomButton.setBackgroundColor(Color.GREEN);
                    canvasNeha.setZoomMode(true);
                } else {
                    zoomButton.setBackgroundColor(Color.RED);
                    canvasNeha.setZoomMode(false);
                }


        }



    }

//    private void loadCanvaswithBackground(){
//        pathReference = FirebaseStorage.getInstance()
//                .getReferenceFromUrl("gs://drawing-app-72e25.appspot.com/elephant.png");
//        final File localFile = new File(getApplicationContext().getCacheDir().getAbsolutePath() +
//                "/" + "elephant.png");
//        pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                FileInputStream is = null;
//                try {
//                    is = new FileInputStream(localFile);
//
//                    Bitmap bmp = BitmapFactory.decodeStream(is);
//                    canvasNeha.setBitmap(bmp);
//                    is.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });




}
