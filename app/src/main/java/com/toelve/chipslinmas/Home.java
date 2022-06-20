package com.toelve.chipslinmas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private Button btLogout,btLapor,btLaporKecelakaan;
    private SharedPreferences loginPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
         loginPreferences = getSharedPreferences("chiplinmas", Context.MODE_PRIVATE);
        btLogout=(Button)findViewById(R.id.btLogout);
        btLapor=(Button)findViewById(R.id.btLapor);
        btLaporKecelakaan=(Button)findViewById(R.id.btLaporKecelakaan);
        startService(new Intent(Home.this, GCMTokenRefreshListenerService.class));
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);
                alertDialogBuilder.setMessage("Yakin Mau Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });
                alertDialogBuilder.setNegativeButton("Yakin",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                loginPreferences.edit().clear().apply();
                                startActivity(new Intent(Home.this,MainActivity.class));
                                finish();
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();

            }
        });

        btLapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,Laporan.class));
                finish();
            }
        });
        btLaporKecelakaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if ((locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)))) {
                    new AmbilBeritaNode(Home.this).execute("cariin", "aaa");
                }else{
                    showGPSDisabledAlertToUser();
                }

            }

        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Yakin Mau Keluar?")
                .setCancelable(false)
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
        alertDialogBuilder.setNegativeButton("Keluar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        System.exit(0);
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    protected void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS Kamu Gak aktif. mau mengaktifkan GPS?")
                .setCancelable(false)
                .setPositiveButton("Aktifkan GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                                finish();
                            }
                        });
        alertDialogBuilder.setNegativeButton("Keluar",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                        finish();


                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
