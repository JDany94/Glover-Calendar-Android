package com.dany.glovercalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Help extends AppCompatActivity {

    WebView wv_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        wv_1 = (WebView)findViewById(R.id.wv1);
        wv_1.setWebViewClient(new WebViewClient());
        wv_1.loadUrl("https://glovers.glovoapp.com/es/consejos/aprovechar-las-horas-de-alta-demanda");
    }
}
