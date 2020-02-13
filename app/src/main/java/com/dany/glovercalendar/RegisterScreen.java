package com.dany.glovercalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dany.glovercalendar.utilidades.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterScreen extends AppCompatActivity {

    EditText fullName, email, password, phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar pb;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        fullName = (EditText) findViewById(R.id.et_NombreRegistrar);
        email = (EditText) findViewById(R.id.et_EmailRegistrar);
        password = (EditText) findViewById(R.id.et_PasswRegistrar);
        phone = (EditText) findViewById(R.id.et_TelefonoRegistrar);
        pb = (ProgressBar) findViewById(R.id.progressBar_Register);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(RegisterScreen.this, MainActivity.class));
            finish();
        }
    }

    public void Registrar (View view) {

        final String mEmail = email.getText().toString().trim();
        final String mPassword = password.getText().toString().trim();
        final String mFullName = fullName.getText().toString();
        final String mPhone = phone.getText().toString();
        final Date creationDate = new Date();

        boolean camposVacios = false;

        if (TextUtils.isEmpty(mEmail)){
            email.setError("Requerido");
            camposVacios = true;
        }

        if (TextUtils.isEmpty(mPassword)) {
            password.setError("Requerido");
            camposVacios = true;
        }

        if (mPassword.length() < 6){
            password.setError("Debe contener minimo 6 caracteres");
            camposVacios = true;
        }

        if (TextUtils.isEmpty(mFullName)){
            fullName.setError("Requerido");
            camposVacios = true;
        }

        if (TextUtils.isEmpty(mPhone)){
            phone.setError("Requerido");
            camposVacios = true;
        }

        if (camposVacios == false){
            pb.setVisibility(View.VISIBLE);

            fAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterScreen.this, "Usuario Creado! Bienvenido", Toast.LENGTH_LONG).show();
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection(Utility.USERS).document(userID);
                        Map<String,Object> user = new HashMap<>();
                        user.put("fName",mFullName);
                        user.put("email",mEmail);
                        user.put("phone",mPhone);
                        user.put("creationDate",creationDate);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                        startActivity(new Intent(RegisterScreen.this, MainActivity.class));
                        finish();
                    }else {
                        Toast.makeText(RegisterScreen.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        pb.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }
}