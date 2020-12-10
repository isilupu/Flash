package com.proyecto.flash.activities.client;

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
import com.proyecto.flash.activities.driver.MapDriverActivity;
import com.proyecto.flash.activities.driver.RegisterDriverActivity;
import com.proyecto.flash.includes.MyToolbar;
import com.proyecto.flash.models.Client;
import com.proyecto.flash.providers.AuthProvider;
import com.proyecto.flash.providers.ClientProvider;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    Toolbar mToolbar;
    SharedPreferences mPref;

    AuthProvider mAuthProvider;
    ClientProvider mClientProvider;

    TextInputEditText mTextNames;
    TextInputEditText mTextEmail;
    TextInputEditText mTextPswd;
    Button mButtonRegister;


    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        MyToolbar.show(this, "Registro de Usuario", true);

        mAuthProvider = new AuthProvider();
        mClientProvider = new ClientProvider();

        mTextNames = findViewById(R.id.txtInputNamesRegister);
        mTextEmail = findViewById(R.id.txtInputEmailRegister);
        mTextPswd = findViewById(R.id.txtInputPasswordRegister);

        mButtonRegister = findViewById(R.id.btnRegister);

        mDialog = new SpotsDialog.Builder().setContext(RegisterActivity.this).setMessage("Espere un momento").build();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRegister();
            }
        });


        /*
        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        String selectedUser = mPref.getString("user", "");
        Toast.makeText(this, "El valor que seleccionó fué: " + selectedUser, Toast.LENGTH_SHORT).show();
        */

    }

    private void clickRegister() {
        String name = mTextNames.getText().toString();
        String email = mTextEmail.getText().toString();
        String password = mTextPswd.getText().toString();

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            if (password.length() >= 6) {
                mDialog.show();
                register(name, email, password);
            } else {
                Toast.makeText(this, "La contraseña debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void register(String name, String email, String password) {
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if (task.isSuccessful()) {
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Client client = new Client(id, name, email);
                    create(client);
                    //Toast.makeText(RegisterActivity.this, "El registro se realizó correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void create(Client client) {
        mClientProvider.create(client).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(RegisterActivity.this, "El registro se realizó correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MapClientActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    /*
    private void goToSaveUser(String id, String nombre, String email) {
        String selectedUser  =  mPref.getString("user", "");
        User user = new User();
        user.setEmail(email);
        user.setName(nombre);

        if(selectedUser.equals("driver")){
            mDatabase.child("Users").child("Drivers").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Falló el registro", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if (selectedUser.equals("client")){
            mDatabase.child("Users").child("Clients").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Falló el registro", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    */
}