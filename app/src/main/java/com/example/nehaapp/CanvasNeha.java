package com.example.nehaapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class CanvasNeha extends View {
    private static final String TAG = "CanvasNeha";
    private Path path;
    private  Paint paint;
    private float mX, mY;
    private Bitmap bitmap;
    private static final float TOUCH_TOLERANCE = 4;
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> deletedpaths = new ArrayList<Path>();
    private Canvas mCanvas;

    float brushwidth;
     int brushColor;

    HashMap<Path, Integer> colorHashMap = new HashMap<>();
    HashMap<Path, Float> widthHashMap = new HashMap<>();
    private int mWidth;
    private int mHeight;
    private Bitmap template;


    public CanvasNeha(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);


    }


    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawCircle(500,500,300,paint);
        //canvas.drawColor(Color.RED);
        canvas.drawBitmap(bitmap, 0, 0, null);

        paint.setStrokeWidth(brushwidth);
        paint.setColor(brushColor);
        paint.setAlpha(150);
        canvas.drawPath(path,paint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged()");
        mWidth = w;
        mHeight = h;
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmap);
        mCanvas.drawColor(Color.WHITE);

    }
    private void saveOfflineCanvas(){
       bitmap.eraseColor(Color.WHITE);
       mCanvas.drawBitmap(template,0,0,null);
        for (Path p : paths) {
            paint.setStrokeWidth(widthHashMap.get(p));
            paint.setColor(colorHashMap.get(p));
            paint.setAlpha(150);
            mCanvas.drawPath(p, paint);
        }
     invalidate();
    }

    public void clear(){
      //  bitmap.eraseColor(Color.WHITE);
        for (Path p: paths){
           deletedpaths.add(p);
        }
        paths.clear();
        saveOfflineCanvas();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Toast.makeText(getContext(),"X = " + event.getX(),Toast.LENGTH_SHORT).show();
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(x, y);
                mX = x;
                mY = y;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                    mX = x;
                    mY = y;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(mX, mY);
                // Added for undo and redo
                paths.add(path);
                widthHashMap.put(path, brushwidth);
                colorHashMap.put(path, brushColor);
                // commit the path to our offscreen
                saveOfflineCanvas();
                path = new Path();
                // kill this so we don't double draw
                path.reset();
                invalidate();
                break;
        }

        return true;
    }

    public void undo() {
        if(paths.size() > 0) {
            deletedpaths.add(paths.get(paths.size() - 1));
            paths.remove(paths.get(paths.size() - 1));

            saveOfflineCanvas();
        }
    }

    public void redo() {
        if(deletedpaths.size()>0) {
            paths.add(deletedpaths.get(deletedpaths.size() - 1));
            deletedpaths.remove(deletedpaths.get(deletedpaths.size() - 1));

            saveOfflineCanvas();
        }

    }

    public void erase(){
        brushColor = Color.WHITE;
        brushwidth = 20;
        saveOfflineCanvas();
    }

    @SuppressLint("WrongThread")
    public void saveFile(String filename){
        String root = getContext().getFilesDir().getAbsolutePath();
        File myDir = new File(root);
        myDir.mkdirs();

        File file = new File (myDir, filename);
        if (file.exists ())
        {
            file.delete ();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("WrongThread")
    public void uploadFile(String filename){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/"+ filename +".png");

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });


    }

    private String getRandomID(){
        return UUID.randomUUID().toString();
    }

    public void setBitmap(Bitmap bmp) {
        template = Bitmap.createScaledBitmap(bmp,mWidth,mHeight,false);
        saveOfflineCanvas();
    }
}
