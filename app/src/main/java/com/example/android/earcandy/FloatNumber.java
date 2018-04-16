package com.example.android.earcandy;

/**
 * Created by Mario on 8/3/2017.
 * Utility class to encapsulate float data-type variables behavior
 */

public class FloatNumber {
    private float number;
    public FloatNumber(float number) {
        this.number = number;
    }

    public void setFloat(float number){
        this.number = number;
    }

    public float getFloat(){
        return this.number;
    }

    public boolean isLargerThan(float number){
        Float thisNumber = new Float(this.number);
        Float comparedNumber = new Float(number);

        if(thisNumber.compareTo(comparedNumber) >= 0 ){
            return true;
        }
        return false;

    }

    public boolean isSmallerThan(float number){
        Float thisNumber = new Float(this.number);
        Float comparedNumber = new Float(number);

        if(thisNumber.compareTo(comparedNumber) <= 0 ){
            return true;
        }
        return false;

    }

    public boolean isEqualsTo(float number) {
        Float thisNumber = new Float(this.number);
        Float comparedNumber = new Float(number);

        if(thisNumber.compareTo(comparedNumber) == 0 ){
            return true;
        }
        return false;
    }

    public int compareTo(float number){
        Float thisNumber = new Float(this.number);
        Float comparedNumber = new Float(number);

        return thisNumber.compareTo(number);
    }
}
