package com.kadzalik.kalkulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button bt_kalkulator,bt_jd,bt_ws,bt_arah_kiblat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bt_kalkulator=findViewById(R.id.button2);
        bt_jd=findViewById(R.id.button3);
        bt_ws=findViewById(R.id.button4);
        bt_arah_kiblat=findViewById(R.id.button5);


        bt_kalkulator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MenuActivity.this,KalkulatorActivity.class);
                startActivity(intent);

            }
        });

        bt_jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MenuActivity.this,JuliandayActivity.class);
                startActivity(intent);

            }
        });





        bt_ws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuActivity.this,WaktuSholatActivity.class);
                startActivity(intent);

            }
        });


        bt_arah_kiblat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuActivity.this,ArahKiblatActivity.class);
                startActivity(intent);

            }
        });




    }
}
