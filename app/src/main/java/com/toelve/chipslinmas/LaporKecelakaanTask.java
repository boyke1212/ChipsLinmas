package com.toelve.chipslinmas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LaporKecelakaanTask extends AsyncTask<String,Void,String> {
   private  Context ctx;
    private String username,password;
   private  AlertDialog alertDialog;
    LaporKecelakaanTask(Context ctx) {
        this.ctx = ctx;
    }
    private ProgressDialog dialog=null;

    @Override
    protected void onPreExecute() {

        alertDialog=new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Information");
        dialog = new ProgressDialog(ctx);
        dialog.setMessage(" Loading...");
        dialog.show();
        dialog.setCancelable(false);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = "https://toelve.com/linmasbjm/mobile/kecelakaan";
        String method = params[0];
        if (method.equalsIgnoreCase("lapor")) {
           String lat = params[1];
          String  longi=params[2];
            String  foto=params[3];
            String  username=params[4];
            String  tanggal=params[5];
            String  jam=params[6];
            String  laporan=params[7];
            String  alamat=params[8];



            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8") + "&" +
                        URLEncoder.encode("longi", "UTF-8") + "=" + URLEncoder.encode(longi, "UTF-8") + "&" +
                        URLEncoder.encode("foto", "UTF-8") + "=" + URLEncoder.encode(foto, "UTF-8") + "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("tanggal", "UTF-8") + "=" + URLEncoder.encode(tanggal, "UTF-8") + "&" +
                        URLEncoder.encode("jam", "UTF-8") + "=" + URLEncoder.encode(jam, "UTF-8") + "&" +
                        URLEncoder.encode("alamat", "UTF-8") + "=" + URLEncoder.encode(alamat, "UTF-8") + "&" +
                URLEncoder.encode("laporan", "UTF-8") + "=" + URLEncoder.encode(laporan, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {

        try {


            if (result.trim().equalsIgnoreCase("sukses")) {
                dialog.dismiss();
                Toast.makeText(ctx, "Laporan Terkirim!", Toast.LENGTH_LONG).show();
              ctx.startActivity(new Intent(ctx,Home.class));
                ((Activity)ctx).finish();

            } else {
                dialog.dismiss();
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();


            }

        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
            Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            SharedPreferences loginPreferences =ctx. getSharedPreferences("ekta", Context.MODE_PRIVATE);
            SharedPreferences.Editor  loginPrefsEditor = loginPreferences.edit();
            loginPrefsEditor.putBoolean("saveLogin",false);
            loginPrefsEditor.apply();

        }
    }
}
