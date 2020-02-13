package com.dany.glovercalendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.widget.Toast;

import com.dany.glovercalendar.utilidades.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SplashScreen extends AppCompatActivity {

    private DatabaseReference update;
    private int dataBaseVersion, actualVersion;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        update = FirebaseDatabase.getInstance().getReference(Utility.UPDATE);

        versionApp();
    }

    private void Splash () {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        },1500);
    }

    private void versionApp () {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(),0);
            actualVersion = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        update.child(Utility.VERSION).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataBaseVersion = Integer.parseInt(dataSnapshot.getValue().toString());
                if (actualVersion != dataBaseVersion) {
                    actualizarApp();
                } else {
                    Splash();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void actualizarApp() {

        update.child(Utility.LINK).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                link = dataSnapshot.getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AppTheme));
        alertdialogBuilder.setTitle("Actualización disponible!");
        alertdialogBuilder.setMessage("Actualiza a la version " + dataBaseVersion);
        alertdialogBuilder.setCancelable(false);
        alertdialogBuilder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                //dialog.cancel();
            }
        });
        alertdialogBuilder.show();
    }

}
