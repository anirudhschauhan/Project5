package com.example.project5;

import android.widget.TextView;
/**
 * class for cycling between topping options
 * @author Anirudh Chauhan, Matthew Calora
 */
public class ToppingRecycler  {
    private String mText;
    public ToppingRecycler(String text){
        mText = text;
    }
    public String getText(){
        return mText;
    }
}
