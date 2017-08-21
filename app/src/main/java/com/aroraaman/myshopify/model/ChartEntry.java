package com.aroraaman.myshopify.model;


public class ChartEntry {
    public final String label;
    public final float value;
    public final int color;

    public ChartEntry(String label, float value, int color) {
        this.label = label;
        this.value = value;
        this.color = color;
    }
}
