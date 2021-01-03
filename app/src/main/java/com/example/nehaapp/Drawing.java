package com.example.nehaapp;


import android.graphics.Path;
/*
public class Drawing {

    private Path path;
    private float width;
    private int color;
    private int alpha;
    private Float scaleFactor;
    private boolean scaled;
    private boolean eraseMode;

 */
public class Drawing {
    private int color;
    private Path path;
    private float width;
    private int ALPHA;
    private Float scaleFactor;
    private boolean scaled;
    private boolean eraseMode;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int getALPHA() {
        return ALPHA;
    }

    public void setALPHA(int ALPHA) {
        this.ALPHA = ALPHA;
    }

    public Float getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(Float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public boolean isScaled() {
        return scaled;
    }

    public void setScaled(boolean scaled) {
        this.scaled = scaled;
    }

    public boolean isEraseMode() {
        return eraseMode;
    }

    public void setEraseMode(boolean eraseMode) {
        this.eraseMode = eraseMode;
    }
}
