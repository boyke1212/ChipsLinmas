package com.toelve.chipslinmas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowFoto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_foto);
        ImageView ivShow=(ImageView)findViewById(R.id.ivShow);
        Button btClose=(Button)findViewById(R.id.btClose);
      SharedPreferences loginPreferences = getSharedPreferences("chiplinmas", Context.MODE_PRIVATE);
       String foto=loginPreferences.getString("foto","");
        byte[] decodedString = Base64.decode(foto, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ivShow.setImageBitmap(decodedByte);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowFoto.this,DetilBerita.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ShowFoto.this,DetilBerita.class));
        finish();
    }
}
