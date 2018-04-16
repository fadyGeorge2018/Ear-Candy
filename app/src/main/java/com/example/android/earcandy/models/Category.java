package com.example.android.earcandy.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mario on 8/1/2017.
 */

public class Category implements Serializable{
    private ArrayList<SubCategory> subCategories;
    private String color;
    private String name;
    private boolean isClickedFlag;

    public Category(){
        setClicked(false);
    }

    public void setSubCategories(ArrayList<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public ArrayList<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setClicked(boolean isClickedFlag) {
        this.isClickedFlag = isClickedFlag;
    }

    public boolean isClicked() {
        return isClickedFlag;
    }
}
