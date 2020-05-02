package com.example.nehaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Nehaapp";
    private CanvasNeha canvasNeha;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private int color;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate method called");
        setContentView(R.layout.activity_main);
        canvasNeha = findViewById(R.id.canvasNeha);

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

        fileName = UUID.randomUUID().toString() + getString(R.string.image_extension_jpg);


//        Button clearbutton = findViewById(R.id.clearButton);
//        clearbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Clickedon clear button",Toast.LENGTH_SHORT).show();
//                canvasNeha.clear();
//            }
//        });
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
        if(item.getItemId() == R.id.ic_undo)
        {
            canvasNeha.undo();
            return true;

        }
        if(item.getItemId()==R.id.ic_redo){
            canvasNeha.redo();
        }
        if (item.getItemId()==R.id.ic_clear){
            canvasNeha.clear();
        }
        if (item.getItemId()==R.id.ic_save){
            canvasNeha.saveFile(fileName);
        }
        if(item.getItemId()==R.id.brush_size){
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

        }

        return true;

    }

    private void saveFile() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.brush_small:
                canvasNeha.brushwidth = 30;
                break;
            case R.id.brush_medium:
                canvasNeha.brushwidth = 40;
                break;
            case R.id.brush_large:
                canvasNeha.brushwidth = 50;
                break;
            case R.id.button1:
                color = ((ColorDrawable)button1.getBackground()).getColor();
                canvasNeha.brushColor = color;
                break;
            case R.id.button2:
                color = ((ColorDrawable)button2.getBackground()).getColor();
                canvasNeha.brushColor = color;
                break;
            case R.id.button3:
                color = ((ColorDrawable)button3.getBackground()).getColor();
                canvasNeha.brushColor = color;
                break;
            case R.id.button4:
                color = ((ColorDrawable)button4.getBackground()).getColor();
                canvasNeha.brushColor = color;
                break;
            case R.id.button5:
                color = ((ColorDrawable)button5.getBackground()).getColor();
                canvasNeha.brushColor = color;
                break;
            case R.id.button6:
                color = ((ColorDrawable)button6.getBackground()).getColor();
                canvasNeha.brushColor = color;
                break;
        }

    }
}
