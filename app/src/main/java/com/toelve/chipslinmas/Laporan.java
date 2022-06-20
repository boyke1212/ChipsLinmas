package com.toelve.chipslinmas;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Laporan extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Snackbar snackbar = null;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences loginPreferences;
    private Button btSave;
    private int CAM_REQUEST1 = 1;
    private Bitmap realimage;
    private AlertDialog alert = null;
    private String lat,longi,foto,jam,tanggal,username,laporan,alamat,perihal;
    private TextView tvTgl,tvJam,tvNamaPelapor;
    private AlertDialog.Builder alertDialogBuilder = null;
    private ImageView ivAmbilGambar;
    private EditText etLaporan,etAlamat,etPerihal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);
         loginPreferences =getSharedPreferences("chiplinmas", MODE_PRIVATE);

        tvTgl = (TextView) findViewById(R.id.tvTgl);
        tvJam = (TextView) findViewById(R.id.tvJam);
        etLaporan = (EditText) findViewById(R.id.etLaporan);
        etPerihal = (EditText) findViewById(R.id.etPerihal);
        etAlamat = (EditText) findViewById(R.id.etAlamat);
        btSave = (Button) findViewById(R.id.btSave);
        ivAmbilGambar = (ImageView) findViewById(R.id.ivAmbilGambar);
        tvNamaPelapor = (TextView) findViewById(R.id.tvNamaPelapor);
        tvTgl.setVisibility(View.GONE);
        tvJam.setVisibility(View.GONE);

        tvNamaPelapor.setVisibility(View.GONE);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        Typeface font = Typeface.createFromAsset(getAssets(), "font/reug.ttf");
        tvTgl.setTypeface(font);
        tvJam.setTypeface(font);
        tvNamaPelapor.setTypeface(font);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        tanggal=dateOnly.format(cal.getTime());
        jam= DateFormat.getTimeInstance(DateFormat.SHORT).format(cal.getTime());
        tvTgl.setText(tanggal);
        tvJam.setText(jam);
        username=loginPreferences.getString("username","");
        tvNamaPelapor.setText("Pengirim : "+username);
        ivAmbilGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Take Photo",
                        "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(Laporan.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo")) {
                            tvNamaPelapor.setVisibility(View.VISIBLE);
                            tvTgl.setVisibility(View.VISIBLE);
                            tvJam.setVisibility(View.VISIBLE);
                            ivAmbilGambar.setAlpha((float) 0.5);
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            File file = new File( getExternalFilesDir(Environment.DIRECTORY_PICTURES),  "foto1.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Laporan.this, Laporan.this.getApplicationContext().getPackageName() + ".chiplinmas.fileprovider", file));

                            startActivityForResult(intent, CAM_REQUEST1);

                        } else if (items[item].equals("Cancel")) {
                            ivAmbilGambar.setAlpha((float) 0.5);
                            dialog.dismiss();
                            ivAmbilGambar.setAlpha((float) 1.0);

                        } else {
                            ivAmbilGambar.setAlpha((float) 1.0);

                        }
                    }


                });
                builder.show();
            }
        });



        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                laporan=etLaporan.getText().toString();
                alamat=etAlamat.getText().toString();
                perihal="kosong";
                if(perihal.equalsIgnoreCase("")||alamat.equalsIgnoreCase("")|| etLaporan.getText().toString().equals("")||foto==null||foto.equalsIgnoreCase("")){
                    Toast.makeText(Laporan.this,"Semua Harus Diisi Dengan Benar",Toast.LENGTH_SHORT).show();
                }else {
                    if(isNetworkAvailable()) {

                        new LaporTask(Laporan.this).execute("Lapor", lat, longi, foto,username,tanggal, jam,laporan,alamat,perihal);
                    }else
                    {
                        snackbar = Snackbar.make(findViewById(android.R.id.content), "No Internet", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Refresh", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                                startActivity(new Intent(Laporan.this, Laporan.class));
                                finish();
                            }
                        });

                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    }
                }

            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(mLastLocation !=null) {
            Log.e("Latitude", String.valueOf(mLastLocation.getLatitude()));
            Log.e("Latitude", String.valueOf(mLastLocation.getLongitude()));
            lat = String.valueOf(mLastLocation.getLatitude());
            longi = String.valueOf(mLastLocation.getLongitude());
            Geocoder geocoder = new Geocoder(Laporan.this, Locale.getDefault());
            String alamat;
            try {
                List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);

                if (addresses != null) {
                    Address returnedAddress = addresses.get(0);
                    alamat = returnedAddress.getAddressLine(0);
                } else {
                    alamat = "almt lagi error!";
                }
            } catch (IOException e) {
                e.printStackTrace();
                alamat = "Address is not yet Registered!";

            }
            etAlamat.setText(alamat);


        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public boolean  isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) && (activeNetwork.getState()== NetworkInfo.State.CONNECTED)){
                return true;
            } else if ((activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) && (activeNetwork.getState()== NetworkInfo.State.CONNECTED)){
                return true;
            }
        } else {

            return false;
        }
        return false;
    }

    protected void showGPSDisabledAlertToUser() {
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS Kamu Gak aktif. mau mengaktifkan GPS?")
                .setCancelable(false)
                .setPositiveButton("Aktifkan GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Keluar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        alert.dismiss();
                        finish();
                    }
                });
        alert = alertDialogBuilder.create();
        alert.show();

    }

    @Override
    public void onBackPressed() {
        alertDialogBuilder = new AlertDialog.Builder(Laporan.this);
        alertDialogBuilder.setMessage("Batalkan Pelaporan?")
                .setCancelable(false)
                .setPositiveButton("Batalkan",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startActivity(new Intent(Laporan.this,Home.class));
                                finish();
                            }
                        });
        alertDialogBuilder.setNegativeButton("Jangan",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alert = alertDialogBuilder.create();
        alert.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQUEST1 && resultCode == Activity.RESULT_OK) {
            File file = new File( getExternalFilesDir(Environment.DIRECTORY_PICTURES),  "foto1.jpg");
            Bitmap bitmap1 = decodeSampledBitmapFromFile(file.getAbsolutePath(), 640, 480);
            realimage = rotate(bitmap1, 90);
            ivAmbilGambar.setImageBitmap(realimage);
            ivAmbilGambar.setAlpha((float) 1.0);
            RelativeLayout bitmapHolder = (RelativeLayout) findViewById(R.id.bitmapHolder);
            tvTgl.setVisibility(View.VISIBLE);
            tvJam.setVisibility(View.VISIBLE);
            tvNamaPelapor.setVisibility(View.VISIBLE);
            bitmapHolder.setDrawingCacheEnabled(true);
            bitmapHolder.buildDrawingCache();
            Bitmap bm = bitmapHolder.getDrawingCache();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } else if (requestCode == CAM_REQUEST1 && resultCode == Activity.RESULT_CANCELED) {
            ivAmbilGambar.setAlpha((float) 1.0);
            ivAmbilGambar.setImageResource(R.drawable.tombolkamera);
            tvJam.setVisibility(View.GONE);
            tvNamaPelapor.setVisibility(View.GONE);
            tvTgl.setVisibility(View.GONE);
            foto = "";
        } else {
            ivAmbilGambar.setAlpha((float) 1.0);
            ivAmbilGambar.setImageResource(R.drawable.tombolkamera);
            tvJam.setVisibility(View.GONE);
            tvNamaPelapor.setVisibility(View.GONE);
            tvTgl.setVisibility(View.GONE);
            foto = "";
        }

    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

}
