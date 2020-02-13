package com.dany.glovercalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dany.glovercalendar.utilidades.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class LoginScreen extends AppCompatActivity {

    TextView olvidoContrasena;
    EditText email, password;
    FirebaseAuth fAuth;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        email = (EditText) findViewById(R.id.et_EmailAcceder);
        password = (EditText) findViewById(R.id.et_PasswAcceder);
        olvidoContrasena = (TextView) findViewById(R.id.tv_olvidarContrasena);
        pb = (ProgressBar) findViewById(R.id.progressBar_login);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginScreen.this, MainActivity.class));
            finish();
        }

        olvidoContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reestablecer Contraseña?");
                passwordResetDialog.setMessage("Ingresa tu correo:");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginScreen.this, "Link enviado..", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }

    //Boton acceder
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

    //Boton registro
    public void Registro (View view) {
        startActivity( new Intent(LoginScreen.this, RegisterScreen.class));
    }

}