package com.kadzalik.kalkulator;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WaktuSholatActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;

    private double nilaiLatitude = 0.0, nilaiLongitude = 0.0, nilaiAltitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private TextView tv_lokasi,tvS,tvT,tvD,tvA,tvM,tvI;

    private boolean isGPS = false;


    Button bt_lokasi;

    List<Address> addresses = null;
    String desa = "";
    String kecamatan ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waktu_sholat);

        bt_lokasi=findViewById(R.id.button10);
        tv_lokasi=findViewById(R.id.textView3);
        tvS=findViewById(R.id.tvS);
        tvT=findViewById(R.id.tvT);
        tvD=findViewById(R.id.tvD);
        tvA=findViewById(R.id.tvA);
        tvM=findViewById(R.id.tvM);
        tvI=findViewById(R.id.tvI);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });


        locationCallback = new LocationCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Geocoder geocoder = new Geocoder(WaktuSholatActivity.this, Locale.getDefault());
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        nilaiLatitude = location.getLatitude();
                        nilaiLongitude = location.getLongitude();
                        nilaiAltitude=location.getAltitude();
                        try {
                            addresses = geocoder.getFromLocation(nilaiLatitude, nilaiLongitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (addresses != null && addresses.size() > 0) {

                            desa = addresses.get(0).getSubLocality();
                            kecamatan = addresses.get(0).getLocality();
                        }

                        tv_lokasi.setText(desa+"-"+kecamatan+","+nilaiLatitude+","+nilaiLongitude+","+nilaiAltitude);

                        if ( mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };


        bt_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLocation();





                if (!isGPS) {

                    return;
                }

                new GpsUtils(WaktuSholatActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                    @Override
                    public void gpsStatus(boolean isGPSEnable) {
                        // turn on GPS
                        isGPS = isGPSEnable;
                    }
                });




            }
        });


update();


    }


    public  void  update(){
        final Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                String tgl= new SimpleDateFormat("dd",Locale.getDefault()).format(new Date());
                String bln= new SimpleDateFormat("MM",Locale.getDefault()).format(new Date());
                String thn= new SimpleDateFormat("yyyy",Locale.getDefault()).format(new Date());
                String tz= new SimpleDateFormat("Z",Locale.getDefault()).format(new Date());


                double tanggal=Double.parseDouble(tgl);
                double bulan=Double.parseDouble(bln);
                double tahun=Double.parseDouble(thn);





                double timezone= Double.parseDouble(tz)/100;
                double tinggilokasi= nilaiAltitude ;
                double sudutsubuh= -20;
                double sudutisya=-18;
                double ihtiyat=0;

                //ubah ke nilai desimal
                double lintang=nilaiLatitude;
                double  lintang_r=Math.toRadians(lintang);
                double bujur= nilaiLongitude ;

                if (bulan<=2){bulan+=12; tahun-=1;}//bila bulan jan/feb maka dianggap bulan ke 13/14 tahun sebelumnya
                //bila periode julian
                int A;
                int B=0;

                //bila periode gregorian
                if ((tahun+bulan/100+tanggal/10000)>=1582.1015){
                    A=(int)(tahun/100);
                    B=2+(A/4)-A;
                }

                double jam=12;
                double julianday12=1720994.5+(int)(365.25*tahun)+(int)(30.60001*(bulan+1))+tanggal+B+jam/24;
                double jdl=julianday12-timezone/24;
                double sudut_tanggal=2*Math.PI*(jdl-2451545)/365.25;
                double deklinasi=0.37877+23.264* Math.sin(Math.toRadians(57.297*sudut_tanggal-79.547))+0.3812*Math.sin(Math.toRadians(2*57.297*sudut_tanggal-82.682))+0.17132*Math.sin(Math.toRadians(3*57.297*sudut_tanggal-59.722));
                double deklinasi_r=Math.toRadians(deklinasi);

                double U = ((jdl-2451545)/36525);
                double LO=Math.toRadians((280.46607+36000.7698*U));
                double EoT=(-1*(1789+237*U)*Math.sin(LO)-(7146-62*U)*Math.cos(LO)+(9934-14*U)*Math.sin(2*LO)-(29+5*U)*Math.cos(2*LO)+(74+10*U)*Math.sin(3*LO)+(320-4*U)*Math.cos(3*LO)-212*Math.sin(4*LO))/1000;



                int nomorhari=(int)((julianday12+1.5)%7+1);
                int nomorpasaran=(int)((julianday12+1.5)%5);


//                System.out.println("Tanggal            = " +(int)tanggal+"-"+(int)bulan+"-"+(int)tahun);
//                System.out.println("Hari Pasaran       = "+ Nama.hari(nomorhari)+" "+Nama.pasaran(nomorpasaran));
//                System.out.println("Julianday 12 UT    = "+julianday12);
//                System.out.println("JDL       12 LT    = "+jdl);
//                System.out.println("Sudut tanggal      = "+sudut_tanggal);
//                System.out.println("Deklinasi          = "+deklinasi);
//                System.out.println("U                  = "+U);
//                System.out.println("LO                 = "+LO);
//                System.out.println("EoT                = "+EoT);
//                System.out.println("Lintang            = "+lintang_r);

                double waktu_dzuhur=12+timezone-bujur/15-EoT/60;
                String dzuhur=Konversi.DesKeDms(waktu_dzuhur)[1]+":"+Konversi.DesKeDms(waktu_dzuhur)[2]+":"+Konversi.DesKeDms(waktu_dzuhur)[3];
                tvD.setText(dzuhur);

                //ashar
                double sudut_a1=Math.sin(Math.atan(1/(1+Math.tan(Math.abs(lintang_r-deklinasi_r)))));
                double sudut_a2=Math.sin(deklinasi_r)*Math.sin(lintang_r);
                double sudut_a3=Math.cos(deklinasi_r)*Math.cos(lintang_r);
                double sudutWaktu_a=(sudut_a1-sudut_a2)/sudut_a3;
                double ha_ashar=Math.toDegrees(Math.acos(sudutWaktu_a));

                double waktu_ashar=waktu_dzuhur+ha_ashar/15;
                String ashar=Konversi.DesKeDms(waktu_ashar)[1]+":"+Konversi.DesKeDms(waktu_ashar)[2]+":"+Konversi.DesKeDms(waktu_ashar)[3];
                tvA.setText(ashar);

                //maghrib
                double sudut_m1=(Math.sin(Math.toRadians(-0.8333-0.0347*Math.sqrt(tinggilokasi)))-Math.sin(deklinasi_r)*Math.sin(lintang_r))/(Math.cos(deklinasi_r)*Math.cos(lintang_r));
                double sudut_m2=Math.toDegrees(Math.acos(sudut_m1));
                double waktu_maghrib=waktu_dzuhur+sudut_m2/15;
                String maghrib=Konversi.DesKeDms(waktu_maghrib)[1]+":"+Konversi.DesKeDms(waktu_maghrib)[2]+":"+Konversi.DesKeDms(waktu_maghrib)[3];
                tvM.setText(maghrib);

                //isya
                double ha_isya=(Math.sin(Math.toRadians(sudutisya))-Math.sin(lintang_r)*Math.sin(deklinasi_r))/(Math.cos(lintang_r)*Math.cos(deklinasi_r));
                double waktu_isya=waktu_dzuhur + Math.toDegrees(Math.acos(ha_isya))/15;
                String isya=Konversi.DesKeDms(waktu_isya)[1]+":"+Konversi.DesKeDms(waktu_isya)[2]+":"+Konversi.DesKeDms(waktu_isya)[3];
                tvI.setText(isya);

                //shubuh
                double ha_shubuh=(Math.sin(Math.toRadians(sudutsubuh))-Math.sin(lintang_r)*Math.sin(deklinasi_r))/(Math.cos(lintang_r)*Math.cos(deklinasi_r));
                double waktu_shubuh=waktu_dzuhur - Math.toDegrees(Math.acos(ha_shubuh))/15;
                String shubuh=Konversi.DesKeDms(waktu_shubuh)[1]+":"+Konversi.DesKeDms(waktu_shubuh)[2]+":"+Konversi.DesKeDms(waktu_shubuh)[3];
                tvS.setText(shubuh);

                //terbit
                double sudut_t1=(Math.sin(Math.toRadians(-0.8333-0.0347*Math.sqrt(tinggilokasi)))-Math.sin(deklinasi_r)*Math.sin(lintang_r))/(Math.cos(deklinasi_r)*Math.cos(lintang_r));
                double sudut_t2=Math.toDegrees(Math.acos(sudut_t1));
                double waktu_terbit=waktu_dzuhur-sudut_t2/15;

                String terbit=Konversi.DesKeDms(waktu_terbit)[1]+":"+Konversi.DesKeDms(waktu_terbit)[2]+":"+Konversi.DesKeDms(waktu_terbit)[3];
                tvT.setText(terbit);



                mHandler.postDelayed(this,1000);

            }
        };
        mHandler.post(runnable);




    }

    @SuppressLint("SetTextI18n")
    private void getLocation() {
        Geocoder geocoder = new Geocoder(WaktuSholatActivity.this, Locale.getDefault());

        if (ActivityCompat.checkSelfPermission(WaktuSholatActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(WaktuSholatActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WaktuSholatActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);

        } else {

            mFusedLocationClient.getLastLocation().addOnSuccessListener(WaktuSholatActivity.this, location -> {
                if (location != null) {
                    Toast.makeText(this, "Lokasi berhasil diperbarui", Toast.LENGTH_LONG).show();
                    nilaiLatitude = location.getLatitude();
                    nilaiLongitude = location.getLongitude();
                    nilaiAltitude=location.getAltitude();
                    try {
                        addresses = geocoder.getFromLocation(nilaiLatitude, nilaiLongitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addresses != null && addresses.size() > 0) {

                        desa = addresses.get(0).getSubLocality();
                        kecamatan = addresses.get(0).getLocality();
                    }

                    tv_lokasi.setText(desa+"-"+kecamatan+","+nilaiLatitude+","+nilaiLongitude+","+nilaiAltitude);

                } else {
                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                }
            });



        }



    }

    @SuppressLint({"MissingPermission", "SetTextI18n"})
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Geocoder geocoder = new Geocoder(WaktuSholatActivity.this, Locale.getDefault());
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.getLastLocation().addOnSuccessListener(WaktuSholatActivity.this, location -> {
                        if (location != null) {
                            nilaiLatitude = location.getLatitude();
                            nilaiLongitude = location.getLongitude();
                            nilaiAltitude=location.getAltitude();
                            try {
                                addresses = geocoder.getFromLocation(nilaiLatitude, nilaiLongitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (addresses != null && addresses.size() > 0) {

                                desa = addresses.get(0).getSubLocality();
                                kecamatan = addresses.get(0).getLocality();
                            }

                            tv_lokasi.setText(desa+"-"+kecamatan+","+nilaiLatitude+","+nilaiLongitude+","+nilaiAltitude);

                        } else {
                            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        }
                    });

                } else {
                    Toast.makeText(this, "Permintaan akses ditolak", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }



}
