package com.dany.glovercalendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dany.glovercalendar.entidades.AltaDemanda;
import com.dany.glovercalendar.utilidades.Utility;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileScreen extends AppCompatActivity {

    TextView tv_name, tv_phone, tv_mail;
    ImageView iv_profile;
    ProgressBar pb;
    ProgressDialog mProgressD;
    Uri picture;

    //Firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference fStorage;

    private String userID;
    private DocumentReference userInfo;
    private StorageReference userPicture;

    private static final int GALLERY_INTENT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        tv_name = (TextView)findViewById(R.id.tv_nameProfile);
        tv_mail = (TextView)findViewById(R.id.tv_emailProfile);
        tv_phone = (TextView)findViewById(R.id.tv_tlfProfile);
        iv_profile = (ImageView)findViewById(R.id.iv_ProfilePicture);
        pb = (ProgressBar) findViewById(R.id.progressBar_Profile);
        mProgressD = new ProgressDialog(this);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance().getReference();

        userID = fAuth.getCurrentUser().getUid();
        userInfo = fStore.collection(Utility.USERS).document(userID);
        userPicture = fStorage.child(Utility.PROFILE_PICTURE).child(userID).child(Utility.PICTURE);

        pb.setVisibility(View.VISIBLE);

        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        //Carga los datos del usuario
        userInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tv_name.setText(documentSnapshot.get(Utility.FNAME).toString());
                tv_mail.setText(documentSnapshot.get(Utility.EMAIL).toString());
                tv_phone.setText(documentSnapshot.get(Utility.PHONE).toString());
            }
        });
        //Carga la imagen de perfil del usuario
        try {
            final File file = File.createTempFile("image", "*");
            userPicture.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    iv_profile.setImageBitmap(bitmap);
                    pb.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileScreen.this, "Aún no tienes foto de perfil!", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.INVISIBLE);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Boton cerrar sesion
    public void signOut (View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginScreen.class));
        finish();
    }

    //Boton Cargar imagen
    public void botonGaleria (View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    //Galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            mProgressD.setMessage("Subiendo..");
            mProgressD.setCancelable(false);
            mProgressD.show();

            picture = data.getData();

            userPicture.putFile(picture).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressD.dismiss();
                    Glide.with(ProfileScreen.this)
                            .load(picture)
                            .fitCenter()
                            .centerCrop()
                            .into(iv_profile);

                    Toast.makeText(ProfileScreen.this, "Foto de perfil guardada..", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //Boton Cambiar Numero
    public void botonCambiarNum (View view) {
        final EditText resetNumber = new EditText(view.getContext());
        final AlertDialog.Builder numberResetDialog = new AlertDialog.Builder(view.getContext());
        numberResetDialog.setTitle("Reestablecer numero de telefono?");
        numberResetDialog.setMessage("Ingresa tu numero:");
        numberResetDialog.setView(resetNumber);
        numberResetDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String number = resetNumber.getText().toString();
                userInfo.update(Utility.PHONE, number);
                tv_phone.setText(number);
            }
        });
        numberResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        numberResetDialog.create().show();
    }
}
