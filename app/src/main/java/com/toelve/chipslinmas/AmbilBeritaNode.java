package com.toelve.chipslinmas;

import android.app.Activity;
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

class AmbilBeritaNode extends AsyncTask<String,Void,String> {
   private  Context ctx;

    AmbilBeritaNode(Context ctx) {
        this.ctx = ctx;
    }
    private ProgressDialog aw;

    @Override
    protected void onPreExecute() {
        aw=new ProgressDialog((Activity)ctx);
        aw.setTitle("Loading");
        aw.setMessage("Please Wait...");
        aw.show();
        aw.setCancelable(false);



        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = "https://toelve.com/linmasbjm/mobile/ambilberita";
        String method = params[0];
        if (method.equalsIgnoreCase("cariin")) {
            String asd = params[1];




            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("SLID", "UTF-8") + "=" + URLEncoder.encode(asd, "UTF-8");
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
        aw.cancel();
        aw.dismiss();
       if(!result.contains("berita"))
        {
            Toast.makeText(ctx,"DATA TIDAK DITEMUKAN",Toast.LENGTH_LONG).show();
            ctx.startActivity(new Intent(ctx,MainActivity.class));
            ((Activity) ctx).finish();
        }else {
           try {

               SharedPreferences loginPreferences = ctx.getSharedPreferences("chiplinmas", Context.MODE_PRIVATE);
               SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
               loginPrefsEditor.putString("berita", result);
               loginPrefsEditor.apply();
               ctx.startActivity(new Intent(ctx, BeritaKejadian.class));
               ((Activity) ctx).finish();

           } catch (Exception e) {
               Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
           }
       }
    }
}
