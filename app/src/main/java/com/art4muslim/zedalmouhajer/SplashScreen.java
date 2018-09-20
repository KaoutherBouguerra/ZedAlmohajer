package com.art4muslim.zedalmouhajer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.art4muslim.zedalmouhajer.features.LoginActivity;
import com.art4muslim.zedalmouhajer.features.MainActivity;
import com.art4muslim.zedalmouhajer.features.entertainment.EntertainmentActivity;

public class SplashScreen extends AppCompatActivity {

   Button btnAljam3iyat, btnEntertainment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initFields();


        btnAljam3iyat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this, EntertainmentActivity.class);
                startActivity(intent);
               // finish();
            }
        });

    }

    private void initFields(){
        btnAljam3iyat = findViewById(R.id.btn_aljam3iyat);
        btnEntertainment = findViewById(R.id.btn_entertainment);

    }
}
