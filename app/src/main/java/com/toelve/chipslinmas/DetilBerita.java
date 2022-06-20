package com.toelve.chipslinmas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetilBerita extends AppCompatActivity {
    SharedPreferences loginPreferences;
    private String ids,usernames,tanggal,laporan,foto,latitude,longitude,alamat,perihal,jam;
    private ImageView ivBerita;
    private GoogleMap mMap;
    private double lat,lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_berita);
        loginPreferences = getSharedPreferences("chiplinmas", Context.MODE_PRIVATE);
        SupportMapFragment mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        ids=loginPreferences.getString("id","");
        usernames=loginPreferences.getString("usernames","");
        tanggal=loginPreferences.getString("tanggal","");
        laporan=loginPreferences.getString("laporan","");
        foto=loginPreferences.getString("foto","");
        latitude=loginPreferences.getString("latitude","");
        longitude=loginPreferences.getString("longitude","");
        lat = Double.parseDouble(latitude);
        lng = Double.parseDouble(longitude);

        alamat=loginPreferences.getString("alamat","");
        perihal=loginPreferences.getString("perihal","");
        jam=loginPreferences.getString("jam","");
        ivBerita=(ImageView)findViewById(R.id.ivBerita);
        byte[] decodedString = Base64.decode(foto, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ivBerita.setImageBitmap(decodedByte);
        TextView tvUsername=(TextView)findViewById(R.id.tvUsername);
        TextView tvJudul=(TextView)findViewById(R.id.tvJudul);
        TextView tvTanggal=(TextView)findViewById(R.id.tvTanggal);
        TextView tvJam=(TextView)findViewById(R.id.tvJam);
        TextView tvPerihal=(TextView)findViewById(R.id.tvPerihal);
        TextView tvAlamat=(TextView)findViewById(R.id.tvAlamat);
        TextView tvLaporan=(TextView)findViewById(R.id.tvLaporan);
       tvUsername.setText(String.format("Pelapor : %s", usernames));
        tvTanggal.setText(String.format("Tanggal Laporan : %s", tanggal));
        tvJam.setText(String.format("Jam : %s", jam));
        tvPerihal.setText(String.format("Perihal : %s", perihal));
        tvAlamat.setText(String.format("Alamat : %s", alamat));
        tvLaporan.setText(String.format("Isi Laporan : %s", laporan));
        Typeface font = Typeface.createFromAsset(getAssets(), "font/reug.ttf");
        tvUsername.setTypeface(font);
        tvTanggal.setTypeface(font);
        tvJam.setTypeface(font);
        tvPerihal.setTypeface(font);
        tvAlamat.setTypeface(font);
        tvLaporan.setTypeface(font);
        tvJudul.setTypeface(font);
        mFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                        .title(perihal));


                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 15));

            }
        });

       Button btClose=(Button)findViewById(R.id.btClose);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AmbilBeritaNode(DetilBerita.this).execute("cariin","aaa");
            }
        });
        ivBerita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetilBerita.this,ShowFoto.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AmbilBeritaNode(DetilBerita.this).execute("cariin","aaa");
    }
}
