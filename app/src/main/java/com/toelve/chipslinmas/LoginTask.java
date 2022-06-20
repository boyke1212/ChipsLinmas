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

class LoginTask extends AsyncTask<String,Void,String> {
   private Context ctx;
    private String username,password;
    LoginTask(Context ctx) {
        this.ctx = ctx;
    }
    private ProgressDialog dialog;

    @Override
    protected void onPreExecute() {

        try {
            dialog = new ProgressDialog((Activity)ctx);
            dialog.setMessage("Loading Data");
            dialog.show();
            dialog.setCancelable(false);
        }catch (Exception e){
            e.printStackTrace();
        }

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = "https://toelve.com/linmasbjm/mobile/login";
        String method = params[0];
        if (method.equalsIgnoreCase("login")) {
             username = params[1];
             password = params[2];




            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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

            //Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();

            if (result.equalsIgnoreCase("sukses")) {
                SharedPreferences loginPreferences =ctx. getSharedPreferences("chiplinmas", Context.MODE_PRIVATE);
                SharedPreferences.Editor  loginPrefsEditor = loginPreferences.edit();
                loginPrefsEditor.putBoolean("saveLogin",true);
                loginPrefsEditor.putString("username",username);
                loginPrefsEditor.putString("password",password);
                loginPrefsEditor.apply();
                dialog.dismiss();
                ctx.startActivity(new Intent(ctx,Home.class));
                ((Activity)ctx).finish();
            }else if(result.equalsIgnoreCase("")) {

                SharedPreferences loginPreferences =ctx. getSharedPreferences("chiplinmas", Context.MODE_PRIVATE);
                SharedPreferences.Editor  loginPrefsEditor = loginPreferences.edit();
                loginPrefsEditor.putBoolean("saveLogin",false);
                loginPrefsEditor.apply();
                Toast.makeText(ctx,"Server Gangguan", Toast.LENGTH_LONG).show();


            }
            else {

                SharedPreferences loginPreferences =ctx. getSharedPreferences("mtrxpulse", Context.MODE_PRIVATE);
                SharedPreferences.Editor  loginPrefsEditor = loginPreferences.edit();
                loginPrefsEditor.putBoolean("saveLogin",false);
                loginPrefsEditor.apply();
                Toast.makeText(ctx,result, Toast.LENGTH_LONG).show();


            }

        } catch (Exception e) {
            SharedPreferences loginPreferences =ctx. getSharedPreferences("mtrxpulse", Context.MODE_PRIVATE);
            SharedPreferences.Editor  loginPrefsEditor = loginPreferences.edit();
            loginPrefsEditor.putBoolean("saveLogin",false);
            loginPrefsEditor.apply();
            Toast.makeText(ctx,"Server Gangguan", Toast.LENGTH_LONG).show();
        }
    }
}
