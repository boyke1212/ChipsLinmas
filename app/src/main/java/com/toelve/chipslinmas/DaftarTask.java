package com.toelve.chipslinmas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class DaftarTask extends AsyncTask<String,Void,String> {
   private  Context ctx;
    private String username;
   private  AlertDialog alertDialog;
    DaftarTask(Context ctx) {
        this.ctx = ctx;
    }
    private ProgressDialog dialog=null;

    @Override
    protected void onPreExecute() {
try {
    alertDialog=new AlertDialog.Builder(ctx).create();
    alertDialog.setTitle("Login Information");
    dialog = new ProgressDialog(ctx);
    dialog.setMessage(" Loading...");
    dialog.show();
    dialog.setCancelable(false);
}catch (Exception e)
{
    e.printStackTrace();
}

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = "https://toelve.com/linmasbjm/mobile/pendaftaran";
        String method = params[0];
        if (method.equalsIgnoreCase("daftar")) {
          String  nama = params[1];
            String tempatlahir=params[2];
            String tanggallahir=params[3];
            String ktp=params[4];
            String alamat=params[5];
            String nomerhp=params[6];
            String email=params[7];
            String foto=params[8];
            String kelamin=params[9];
            String token=params[10];
            String username=params[11];
            String password=params[12];



            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("nama", "UTF-8") + "=" + URLEncoder.encode(nama, "UTF-8") + "&" +
                        URLEncoder.encode("tempatlahir", "UTF-8") + "=" + URLEncoder.encode(tempatlahir, "UTF-8") + "&" +
                        URLEncoder.encode("tanggallahir", "UTF-8") + "=" + URLEncoder.encode(tanggallahir, "UTF-8") + "&" +
                        URLEncoder.encode("ktp", "UTF-8") + "=" + URLEncoder.encode(ktp, "UTF-8") + "&" +
                        URLEncoder.encode("alamat", "UTF-8") + "=" + URLEncoder.encode(alamat, "UTF-8") + "&" +
                        URLEncoder.encode("nomerhp", "UTF-8") + "=" + URLEncoder.encode(nomerhp, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("foto", "UTF-8") + "=" + URLEncoder.encode(foto, "UTF-8") + "&" +
                        URLEncoder.encode("kelamin", "UTF-8") + "=" + URLEncoder.encode(kelamin, "UTF-8") + "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                        URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
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

        if(dialog!=null){
            dialog.dismiss();
        }

        try {


            if (result.trim().equalsIgnoreCase("sukses")) {

                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                ctx.startActivity(new Intent(ctx,MainActivity.class));
                ((Activity)ctx).finish();


            } else {

                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();


            }

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();


        }
    }
}
