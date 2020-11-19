package com.kadzalik.auqot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LokasiActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;

    private double nilaiLatitude = 0.0, nilaiLongitude = 0.0, nilaiAltitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private TextView tv_lokasi;

    private boolean isGPS = false;
    Button bt_simpan,cari_lok_oto;


    List<Address> addresses = null;
    String desa = "";
    String kecamatan ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi);
        tv_lokasi =findViewById(R.id.textView);
        bt_simpan =findViewById(R.id.button18);
        cari_lok_oto =findViewById(R.id.button11);
        ProgressBar progressBar = findViewById(R.id.progressBar2);


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

        bt_simpan.setEnabled(false);

        ArrayList<String> tv_nama_daerah = new ArrayList<String>();
        String[] nama_daerah = getResources().getStringArray(R.array.nama_daerah);
        String[] data_daerah = getResources().getStringArray(R.array.data_daerah);
        tv_nama_daerah.addAll(Arrays.asList(nama_daerah));

        AutoCompleteTextView kolom_cari = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, tv_nama_daerah);

        kolom_cari.setThreshold(1);
        kolom_cari.setAdapter(adapter);
        //Pencarian.addTextChangedListener(this);

        kolom_cari.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                int index = tv_nama_daerah.indexOf(kolom_cari.getText().toString());
                String value = data_daerah[index];
                // Do Whatever you want to do ;)
                tv_lokasi.setText(value);

                //menutup virtual keyboar pada layar
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                } catch (Exception ignored) {}

                kolom_cari.clearFocus();


            }
        });



        locationCallback = new LocationCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Geocoder geocoder = new Geocoder(LokasiActivity.this, Locale.getDefault());
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

        cari_lok_oto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLocation();
                kolom_cari.setText("");

                progressBar.setVisibility(View.VISIBLE);




                if (!isGPS) {

                    return;
                }

                new GpsUtils(LokasiActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                    @Override
                    public void gpsStatus(boolean isGPSEnable) {
                        // turn on GPS
                        isGPS = isGPSEnable;
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });




            }
        });


        tv_lokasi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bt_simpan.setAlpha(1f);
                bt_simpan.setEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable s) {
                progressBar.setVisibility(View.INVISIBLE);


            }
        });




        bt_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LokasiActivity.this, MainActivity.class);
                intent.putExtra("datalokasi",tv_lokasi.getText().toString());
                startActivity(intent);





            }
        });

    }





    @SuppressLint("SetTextI18n")
    private void getLocation() {
        Geocoder geocoder = new Geocoder(LokasiActivity.this, Locale.getDefault());

        if (ActivityCompat.checkSelfPermission(LokasiActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(LokasiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LokasiActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);

        } else {

            mFusedLocationClient.getLastLocation().addOnSuccessListener(LokasiActivity.this, location -> {
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
        Geocoder geocoder = new Geocoder(LokasiActivity.this, Locale.getDefault());
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.getLastLocation().addOnSuccessListener(LokasiActivity.this, location -> {
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
