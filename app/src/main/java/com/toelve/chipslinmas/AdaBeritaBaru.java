package com.toelve.chipslinmas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdaBeritaBaru extends AppCompatActivity {
    SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ada_berita_baru);
        loginPreferences = getSharedPreferences("chiplinmas", Context.MODE_PRIVATE);
        Button btCLose=(Button)findViewById(R.id.btClose);
        btCLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=loginPreferences.getString("username","");
                if(username.equals("")){
                    startActivity(new Intent(AdaBeritaBaru.this,MainActivity.class));
                    finish();
                }else{
                    new AmbilBeritaNode(AdaBeritaBaru.this).execute("cariin", "aaa");
                }

            }
        });
    }
}
