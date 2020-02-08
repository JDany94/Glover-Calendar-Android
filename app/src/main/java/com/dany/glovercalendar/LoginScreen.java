package com.dany.glovercalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginScreen extends AppCompatActivity {

    private EditText email, password;
    FirebaseAuth fAuth;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        email = (EditText) findViewById(R.id.et_EmailAcceder);
        password = (EditText) findViewById(R.id.et_PasswAcceder);
        pb = (ProgressBar) findViewById(R.id.progressBar_login);
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginScreen.this, MainActivity.class));
            finish();
        }
    }

    public void Acceder (View view) {

        String mEmail = email.getText().toString().trim();
        String mPassword = password.getText().toString().trim();
        boolean camposVacios = false;

        if (TextUtils.isEmpty(mEmail)){
            email.setError("Requerido");
            camposVacios = true;
        }

        if (TextUtils.isEmpty(mPassword)) {
            password.setError("Requerido");
            camposVacios = true;
        }

        if (camposVacios == false){
            pb.setVisibility(View.VISIBLE);

            fAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginScreen.this, "Bienvenido", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginScreen.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginScreen.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        pb.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    public void Registro (View view) {
        startActivity( new Intent(LoginScreen.this, RegisterScreen.class));
    }
}