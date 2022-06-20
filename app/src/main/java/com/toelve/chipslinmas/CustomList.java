package com.toelve.chipslinmas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ToElvE on 25/01/2017.
 */


    public class CustomList extends ArrayAdapter<String> {
        private ArrayList<String> username;
    private ArrayList<String> tanggal;
    private ArrayList<String> foto;
    private ArrayList<String> perihal;


        private final Activity context;;
        CustomList(Activity context, ArrayList<String> username, ArrayList<String> tanggal, ArrayList<String> foto,
                   ArrayList<String> perihal) {
            super(context, R.layout.rowmenu,perihal);
            this.context = context;
            this.username = username;
            this.foto = foto;
            this.perihal = perihal;
            this.tanggal = tanggal;



        }

        @NonNull
        public View getView(final int position, View view, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            @SuppressLint({"ViewHolder", "InflateParams"}) View rowView = inflater.inflate(R.layout.rowmenu, null, true);
            ImageView ivMenuImage=(ImageView)rowView.findViewById(R.id.ivMenuImage);
            TextView tvJudulKet=(TextView)rowView.findViewById(R.id.tvJudulKet);
            TextView tvFrom=(TextView)rowView.findViewById(R.id.tvFrom);
            TextView tvDates=(TextView)rowView.findViewById(R.id.tvDates);
            byte[] decodedString = Base64.decode(foto.get(position), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivMenuImage.setImageBitmap(decodedByte);
            tvJudulKet.setText(perihal.get(position));
            tvFrom.setText(username.get(position));
            tvDates.setText(tanggal.get(position));
            return rowView;
        }

}
