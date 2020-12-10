package com.proyecto.flash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.proyecto.flash.R;
import com.proyecto.flash.activities.client.RegisterActivity;
import com.proyecto.flash.activities.driver.RegisterDriverActivity;
import com.proyecto.flash.includes.MyToolbar;


public class SelectOptionAuthActivity extends AppCompatActivity {

    Button mButtonIAmAccount;
    Button mButtonRegisterNow;
    Toolbar mToolbar;

    SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option_auth);
        MyToolbar.show(this, "Seleccionar una Opción", true);

        mButtonIAmAccount = findViewById(R.id.btnIAmAccount);
        mButtonRegisterNow = findViewById(R.id.btnRegisterNow);

        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        mButtonIAmAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSelectLogin();
            }
        });

        mButtonRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSelectRegister();
            }
        });
    }

    private void goToSelectLogin() {
        Intent intent = new Intent(SelectOptionAuthActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToSelectRegister() {
        String typeUser = mPref.getString("user","");
        if(typeUser.equals("client")){
            Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterDriverActivity.class);
            startActivity(intent);
        }

    }

}