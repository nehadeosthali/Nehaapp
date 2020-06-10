package com.example.nehaapp;


import android.graphics.Path;

public class Drawing {
    private int color;
    private Path path;
    private float width;
    private int ALPHA;

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
}
