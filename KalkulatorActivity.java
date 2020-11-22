package com.kadzalik.kalkulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class KalkulatorActivity extends AppCompatActivity {

    Button bt_tambah,bt_kali,bt_bagi,bt_kurang,bt_mod;
    EditText et_1,et_2;
    TextView tv_hasil;

    double nilai1,nilai2,hasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalkulator);

        bt_tambah=findViewById(R.id.button);
        bt_kali=findViewById(R.id.button6);
        bt_bagi=findViewById(R.id.button7);
        bt_kurang=findViewById(R.id.button8);
        bt_mod=findViewById(R.id.button9);

        et_1=findViewById(R.id.editText);
        et_2=findViewById(R.id.editText2);

        tv_hasil=findViewById(R.id.tv);

        bt_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    nilai1=Double.parseDouble(et_1.getText().toString());
                    nilai2=Double.parseDouble(et_2.getText().toString());

                    hasil= nilai1+nilai2;
                    tv_hasil.setText(String.valueOf((int) hasil));

                }catch (Exception e){

                    tv_hasil.setText("Input tak boleh kosong!");
                }

            }
        });


        bt_kali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    nilai1=Double.parseDouble(et_1.getText().toString());
                    nilai2=Double.parseDouble(et_2.getText().toString());

                    hasil= nilai1*nilai2;
                    tv_hasil.setText(String.valueOf((int) hasil));

                }catch (Exception e){

                    tv_hasil.setText("Input tak boleh kosong!");
                }

            }
        });


        bt_bagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    nilai1=Double.parseDouble(et_1.getText().toString());
                    nilai2=Double.parseDouble(et_2.getText().toString());

                    hasil= nilai1/nilai2;
                    tv_hasil.setText(String.valueOf( hasil));

                }catch (Exception e){

                    tv_hasil.setText("Input tak boleh kosong!");
                }

            }
        });


        bt_kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    nilai1=Double.parseDouble(et_1.getText().toString());
                    nilai2=Double.parseDouble(et_2.getText().toString());

                    hasil= nilai1-nilai2;
                    tv_hasil.setText(String.valueOf((int) hasil));

                }catch (Exception e){

                    tv_hasil.setText("Input tak boleh kosong!");
                }

            }
        });


        bt_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    nilai1=Double.parseDouble(et_1.getText().toString());
                    nilai2=Double.parseDouble(et_2.getText().toString());

                    hasil= nilai1%nilai2;
                    tv_hasil.setText(String.valueOf(hasil));

                }catch (Exception e){

                    tv_hasil.setText("Input tak boleh kosong!");
                }

            }
        });



    }
}
