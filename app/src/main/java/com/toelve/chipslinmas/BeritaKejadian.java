package com.toelve.chipslinmas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BeritaKejadian extends AppCompatActivity {
    private ArrayList<String> id=new ArrayList<>();
    private ArrayList<String> usernames=new ArrayList<>();
    private ArrayList<String> tanggal=new ArrayList<>();
    private ArrayList<String> jam=new ArrayList<>();
    private ArrayList<String> laporan=new ArrayList<>();
    private ArrayList<String> foto=new ArrayList<>();
    private ArrayList<String> latitude=new ArrayList<>();
    private ArrayList<String> longitude=new ArrayList<>();
    private ArrayList<String> alamat=new ArrayList<>();
    private ArrayList<String> perihal=new ArrayList<>();
    private ListView lvList;
    private String hasil;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_kejadian);
        final SharedPreferences loginPreferences = getSharedPreferences("chiplinmas", Context.MODE_PRIVATE);
        String berita=loginPreferences.getString("berita","");
        id=PojoMion.AmbilArray(berita,"id");
        usernames=PojoMion.AmbilArray(berita,"username");
        tanggal=PojoMion.AmbilArray(berita,"tanggal");
        laporan=PojoMion.AmbilArray(berita,"laporan");
        foto=PojoMion.AmbilArray(berita,"foto");
        latitude=PojoMion.AmbilArray(berita,"latitude");
        longitude=PojoMion.AmbilArray(berita,"longitude");
        alamat=PojoMion.AmbilArray(berita,"alamat");
        perihal=PojoMion.AmbilArray(berita,"perihal");
        jam=PojoMion.AmbilArray(berita,"jam");
        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        //Toast.makeText(this,gambar.get(0),Toast.LENGTH_LONG).show();
        lvList=(ListView)findViewById(R.id.lvList);
        CustomList boykeAdapter = new CustomList(this,usernames,tanggal,foto,laporan);
        boykeAdapter.notifyDataSetChanged();
        lvList.setAdapter(boykeAdapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loginPreferences.edit().putString("id",id.get(i))
                        .putString("usernames",usernames.get(i))
                        .putString("tanggal",tanggal.get(i))
                        .putString("laporan",laporan.get(i))
                        .putString("foto",foto.get(i))
                        .putString("latitude",latitude.get(i))
                        .putString("longitude",longitude.get(i))
                        .putString("alamat",alamat.get(i))
                        .putString("perihal",perihal.get(i))
                        .putString("jam",jam.get(i)).apply();
                startActivity(new Intent(BeritaKejadian.this,DetilBerita.class));
                finish();

            }
        });
        lvList.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(mLastFirstVisibleItem<firstVisibleItem)
                {
                    Log.i("SCROLLING DOWN","TRUE");
                    floatingActionButton.setVisibility(View.GONE);
                }
                if(mLastFirstVisibleItem>firstVisibleItem)
                {   floatingActionButton.setVisibility(View.VISIBLE);
                    Log.i("SCROLLING UP","TRUE");
                }
                mLastFirstVisibleItem=firstVisibleItem;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(BeritaKejadian.this,Laporan.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BeritaKejadian.this,Home.class));
        finish();
    }
}
