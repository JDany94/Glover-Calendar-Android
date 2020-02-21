package com.dany.glovercalendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dany.glovercalendar.entidades.AltaDemanda;
import com.dany.glovercalendar.utilidades.Utility;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ProfileScreen extends AppCompatActivity {

    TextView tv_name, tv_phone, tv_mail;
    ImageView iv_profile;
    ProgressBar pb;
    ProgressDialog mProgressD;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference fStorage;

    private String userID;
    private DocumentReference userInfo;

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
        pb.setVisibility(View.VISIBLE);

        userInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tv_name.setText(documentSnapshot.get(Utility.FNAME).toString()); //STRINGS!!!!!!!!!!!!!!!!!!!!!1
                tv_mail.setText(documentSnapshot.get(Utility.EMAIL).toString());
                tv_phone.setText(documentSnapshot.get(Utility.PHONE).toString());
                pb.setVisibility(View.INVISIBLE);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            mProgressD.setMessage("Subiendo..");
            mProgressD.setCancelable(false);
            mProgressD.show();

            Uri uri = data.getData();
            StorageReference filePath = fStorage.child(Utility.PROFILEPICTURE).child(userID).child(Utility.PICTURE);
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mProgressD.dismiss();

                    Uri asd = taskSnapshot.get

                    Glide.with(ProfileScreen.this)
                            .load(asd)
                            .fitCenter()
                            .centerCrop()
                            .into(iv_profile);


                    Toast.makeText(ProfileScreen.this, "Foto de perfil guardada..", Toast.LENGTH_SHORT).show();


                }
            });
        }
    }
}
