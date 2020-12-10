package com.proyecto.flash.activities.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.proyecto.flash.R;
import com.proyecto.flash.activities.client.RegisterActivity;
import com.proyecto.flash.includes.MyToolbar;
import com.proyecto.flash.models.Client;
import com.proyecto.flash.models.Driver;
import com.proyecto.flash.providers.AuthProvider;
import com.proyecto.flash.providers.ClientProvider;
import com.proyecto.flash.providers.DriverProvider;

import dmax.dialog.SpotsDialog;

public class RegisterDriverActivity extends AppCompatActivity {

    Toolbar mToolbar;

    AuthProvider mAuthProvider;
    DriverProvider mDriverProvider;

    TextInputEditText mTextNames;
    TextInputEditText mTextEmail;
    TextInputEditText mTextPswd;
    TextInputEditText mTextVehicleBrand;
    TextInputEditText mTextVehiclePlate;
    Button mButtonRegister;


    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);
        MyToolbar.show(this, "Registro de conductor", true);

        mAuthProvider = new AuthProvider();
        mDriverProvider = new DriverProvider();

        mTextNames = findViewById(R.id.txtInputNamesRegister);
        mTextEmail = findViewById(R.id.txtInputEmailRegister);
        mTextPswd = findViewById(R.id.txtInputPasswordRegister);
        mTextVehicleBrand = findViewById(R.id.txtInputVehicleBrand);
        mTextVehiclePlate = findViewById(R.id.txtInputVehiclePlate);

        mButtonRegister = findViewById(R.id.btnRegister);


        mDialog = new SpotsDialog.Builder().setContext(RegisterDriverActivity.this).setMessage("Espere un momento").build();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRegister();
            }
        });
    }

    private void clickRegister() {
        String name = mTextNames.getText().toString();
        String email = mTextEmail.getText().toString();
        String password = mTextPswd.getText().toString();
        String vehicleBrand = mTextVehicleBrand.getText().toString();
        String vehiclePlate = mTextVehiclePlate.getText().toString();

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !vehicleBrand.isEmpty() && !vehiclePlate.isEmpty()) {
            if (password.length() >= 6) {
                mDialog.show();
                register(name, email, password, vehicleBrand, vehiclePlate);
            } else {
                Toast.makeText(this, "La contraseña debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void register(String name, String email, String password, String vehicleBrand, String vehiclePlate) {
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if (task.isSuccessful()) {
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Driver driver = new Driver(id, name, email, vehicleBrand, vehiclePlate);
                    create(driver);
                    //Toast.makeText(RegisterActivity.this, "El registro se realizó correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterDriverActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void create(Driver driver) {
        mDriverProvider.create(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(RegisterDriverActivity.this, "El registro se realizó correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterDriverActivity.this, MapDriverActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterDriverActivity.this, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}