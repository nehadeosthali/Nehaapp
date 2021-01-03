package com.example.nehaapp;
/*
public class LoadedImage {
    private Bitmap mBackground;
    private ArrayList<Drawing> mDrawingArrayList;
    private ArrayList<Drawing> mUndoneDrawingArrayList;
    private boolean isCached;
    private String downloadName;
    private String id;
 */

import android.graphics.Bitmap;

import java.util.ArrayList;

public class LoadedImage {
    private Bitmap background;
    private ArrayList<Drawing> drawingArrayList;
    private ArrayList <Drawing> undoneDrawingArrayList;

    public LoadedImage(Bitmap background, ArrayList<Drawing> drawingArrayList, ArrayList<Drawing> undoneDrawingArrayList) {
        this.background = background;
        this.drawingArrayList = drawingArrayList;
        this.undoneDrawingArrayList = undoneDrawingArrayList;
    }

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    public ArrayList<Drawing> getDrawingArrayList() {
        return drawingArrayList;
    }

    public void setDrawingArrayList(ArrayList<Drawing> drawingArrayList) {
        this.drawingArrayList = drawingArrayList;
    }

    public ArrayList<Drawing> getUndoneDrawingArrayList() {
        return undoneDrawingArrayList;
    }

    public void setUndoneDrawingArrayList(ArrayList<Drawing> undoneDrawingArrayList) {
        this.undoneDrawingArrayList = undoneDrawingArrayList;
    }
}
