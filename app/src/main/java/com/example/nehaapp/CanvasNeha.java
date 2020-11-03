package com.example.nehaapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.provider.ContactsContract;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    private  Paint paint;
    private float mX, mY;
    private Bitmap bitmap;
    private static final float TOUCH_TOLERANCE = 4;
    private ArrayList<Drawing> drawingArrayList = new ArrayList<Drawing>();
    private ArrayList<Drawing> deletedDrawingsArrayList = new ArrayList<Drawing>();
    private Canvas mCanvas;
    private Drawing drawing;
    private Path path;
    boolean clearFlag = true;
    boolean zoomMode = false;

    private float brushwidth;
    private int brushColor;


    private int mWidth;
    private int mHeight;
    private Bitmap template;
    private int PAINT_ALPHA;
    private ArrayList<String> savedPictures;


    public CanvasNeha(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        drawing = new Drawing();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);


    }

    public void setPAINT_ALPHA(int PAINT_ALPHA) {
        if (PAINT_ALPHA >=0 & PAINT_ALPHA <= 255){
            this.PAINT_ALPHA = PAINT_ALPHA;
        }

    }

    public void setBrushwidth(float brushwidth) {
        this.brushwidth = brushwidth;
    }

    public void setBrushColor(int brushColor) {
        this.brushColor = brushColor;
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //canvas.drawCircle(500,500,300,paint);
            //canvas.drawColor(Color.RED);
            canvas.drawBitmap(bitmap, 0, 0, null);

            paint.setStrokeWidth(brushwidth);
            paint.setColor(brushColor);
            paint.setAlpha(PAINT_ALPHA);
            canvas.drawPath(path,paint);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged()");
        mWidth = w;
        mHeight = h;
        if (bitmap == null){
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(bitmap);
            mCanvas.drawColor(Color.WHITE);
        }

    }
    //Saving offline canvas
    private void saveOfflineCanvas(){
        Paint offlinePaint = new Paint();
        offlinePaint.setAntiAlias(true);
        offlinePaint.setDither(true);
        offlinePaint.setStyle(Paint.Style.STROKE);
        offlinePaint.setStrokeJoin(Paint.Join.ROUND);
        offlinePaint.setStrokeCap(Paint.Cap.ROUND);
       bitmap.eraseColor(Color.WHITE);
       if(template!=null) {
           mCanvas.drawBitmap(template, 0, 0, null);
       }
        for (Drawing drawing : drawingArrayList) {
            offlinePaint.setStrokeWidth(drawing.getWidth());
            offlinePaint.setColor(drawing.getColor());
            offlinePaint.setAlpha(drawing.getALPHA());
            mCanvas.drawPath(drawing.getPath(), offlinePaint);
        }
     invalidate();
    }

    //Clear the canvas

    public void clear(){

      //  bitmap.eraseColor(Color.WHITE);
        for (Drawing p: drawingArrayList){
           deletedDrawingsArrayList.add(p);
        }
        drawingArrayList.clear();
        saveOfflineCanvas();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!zoomMode) {
            //Toast.makeText(getContext(),"X = " + event.getX(),Toast.LENGTH_SHORT).show();
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
                    Drawing drawing = new Drawing();
                    drawing.setPath(path);
                    drawing.setWidth(brushwidth);
                    drawing.setColor(brushColor);
                    drawing.setALPHA(PAINT_ALPHA);

                    // Added for undo and redo
                    drawingArrayList.add(drawing);
                    // commit the path to our offscreen
                    saveOfflineCanvas();
                    path = new Path();
                    // kill this so we don't double draw
                    path.reset();
                    invalidate();
                    break;
            }
        }

            else{


            }
            return true;


    }

    public void undo() {
        clearFlag = false;
        if(drawingArrayList.size() > 0) {
            deletedDrawingsArrayList.add(drawingArrayList.get(drawingArrayList.size() - 1));
            drawingArrayList.remove(drawingArrayList.get(drawingArrayList.size() - 1));

            saveOfflineCanvas();
        }
    }

    public void redo() {
        if (clearFlag){
            for (Drawing drawing:deletedDrawingsArrayList){
                drawingArrayList.add(drawing);

            }
            deletedDrawingsArrayList.clear();
            saveOfflineCanvas();
        }
        else if (deletedDrawingsArrayList.size()>0) {
            drawingArrayList.add(deletedDrawingsArrayList.remove(deletedDrawingsArrayList.size() - 1));
//            drawingArrayList.add(deletedDrawingsArrayList.get(deletedDrawingsArrayList.size() - 1));
//            deletedDrawingsArrayList.remove(deletedDrawingsArrayList.get(deletedDrawingsArrayList.size() - 1));
            saveOfflineCanvas();

        }

    }

    public void erase(){
        brushColor = Color.WHITE;
        brushwidth = 20;
        setPAINT_ALPHA(255);
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
    public void uploadFile(final String filename){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            StorageReference imageRef = storageRef.child(replaceEmailwithComma(firebaseUser.getEmail())).child(filename);

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

                    }
                    savedPictures.add(filename);
                    databaseUser.setSavedPictures(savedPictures);
                    myRef.child(email).setValue(databaseUser);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




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
            // Write a message to the database

        } else {
            saveFile(filename);
            // No user is signed in
        }



    }

    private void addImagetoDatabase (){

    }
    private String replaceEmailwithComma(String email){
        return email.replace(".",",");
    }



    private String getRandomID(){
        return UUID.randomUUID().toString();
    }

    public void setBitmap(Bitmap bmp) {
        template = Bitmap.createScaledBitmap(bmp,mWidth,mHeight,false);
        saveOfflineCanvas();
    }

    public void setZoomMode(boolean b) {
        zoomMode = b;
    }
}
