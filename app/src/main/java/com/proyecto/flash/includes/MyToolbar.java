package com.proyecto.flash.includes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.proyecto.flash.R;

public class MyToolbar {

    public static void show(AppCompatActivity activity, String title, boolean UpButton){
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(UpButton);

    }
}
