package com.kadzalik.kalkulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JuliandayActivity extends AppCompatActivity {

    Button bt_mulai,bt_stop;
    TextView tv_tanggal,tv_jd;
    boolean mStop=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_julianday);
        bt_mulai=findViewById(R.id.button12);
        bt_stop=findViewById(R.id.button13);
        tv_tanggal=findViewById(R.id.textView);
        tv_jd=findViewById(R.id.textView2);



        bt_mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                mStop=false;

            }
        });

        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStop=true;

            }
        });




    }

    public  void  update(){
        final  Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                double tanggal=Double.parseDouble(new  SimpleDateFormat("dd",Locale.getDefault()).format(new Date()));
                double bulan=Double.parseDouble(new  SimpleDateFormat("MM",Locale.getDefault()).format(new Date()));
                double tahun=Double.parseDouble(new  SimpleDateFormat("yyyy",Locale.getDefault()).format(new Date()));

                double jam=Double.parseDouble(new  SimpleDateFormat("HH",Locale.getDefault()).format(new Date()));
                double menit=Double.parseDouble(new  SimpleDateFormat("mm",Locale.getDefault()).format(new Date()));
                double detik=Double.parseDouble(new  SimpleDateFormat("ss",Locale.getDefault()).format(new Date()));

                if (bulan<=2){bulan+=12; tahun-=1;}//bila bulan jan/feb maka dianggap bulan ke 13/14 tahun sebelumnya
                //bila periode julian
                int A;
                int B=0;

                //bila periode gregorian
                if ((tahun+bulan/100+tanggal/10000)>=1582.1015){
                    A=(int)(tahun/100);
                    B=2+(A/4)-A;
                }

                double julianday=1720994.5+(int)(365.25*tahun)+(int)(30.60001*(bulan+1))+tanggal+B+(jam+(menit/60)+detik/3600)/24;

                String waktu=(int)tanggal+" - "+(int)bulan+" - "+ (int)tahun+ ",  "+ (int)jam+" : "+ (int)menit+" : "+(int)detik;
                tv_tanggal.setText(waktu);
                tv_jd.setText(String.valueOf(julianday));

            if (!mStop) mHandler.postDelayed(this,1000);

            }
        };
        mHandler.post(runnable);




    }
}
