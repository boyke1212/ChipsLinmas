package com.toelve.chipslinmas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Register extends AppCompatActivity {
    private TextView tvHead,tvMohon,namaJudul,kelaminJudul,tempatlahirJudul,tanggallahirJudul,
            ktpJudul,tvNomerHp,tvEmail,alamatJudul,kameraJudul,tvCaptcha,tvUsername,tvPassword;
    private EditText etNama,etKelamin,etTempatLahir,etTanggalLahir,etKtp,etNomerHp,
            etEmail,etAlamat,etJawaban,etUsername,etPassword;
    private String nama,kelamin,tempatlahir,tanggallahir,ktp,nomerhp,email,alamat,jawaban,foto,
            username,password;
    private ImageButton ivAmbilGambar;
    private SharedPreferences loginPreferences;
    private ImageView im;
    private Button btCaptcha,btDaftar;
    private int CAM_REQUEST1 = 1;
    private Bitmap bitmap,bitmap2,realimage;
    private int SELECT_FILE = 666;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1976;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvHead=(TextView)findViewById(R.id.tvHead);
        tvUsername=(TextView)findViewById(R.id.tvUsername);
        tvPassword=(TextView)findViewById(R.id.tvPassword);
        tvCaptcha=(TextView)findViewById(R.id.tvCaptcha);
        tvMohon=(TextView)findViewById(R.id.tvMohon);
        namaJudul=(TextView)findViewById(R.id.namaJudul);
        kelaminJudul=(TextView)findViewById(R.id.kelaminJudul);
        tempatlahirJudul=(TextView)findViewById(R.id.tempatlahirJudul);
        tanggallahirJudul=(TextView)findViewById(R.id.tanggallahirJudul);
        ktpJudul=(TextView)findViewById(R.id.ktpJudul);
        tvNomerHp=(TextView)findViewById(R.id.tvNomerHp);
        tvEmail=(TextView)findViewById(R.id.tvEmail);
        alamatJudul=(TextView)findViewById(R.id.alamatJudul);
        kameraJudul=(TextView)findViewById(R.id.kameraJudul);
        Typeface fars = Typeface.createFromAsset(getAssets(), "font/reug.ttf");
        tvHead.setTypeface(fars);
        tvMohon.setTypeface(fars);
        namaJudul.setTypeface(fars);
        kelaminJudul.setTypeface(fars);
        tempatlahirJudul.setTypeface(fars);
        tanggallahirJudul.setTypeface(fars);
        ktpJudul.setTypeface(fars);
        tvNomerHp.setTypeface(fars);
        tvEmail.setTypeface(fars);
        tvCaptcha.setTypeface(fars);
        alamatJudul.setTypeface(fars);
        kameraJudul.setTypeface(fars);
        etNama=(EditText)findViewById(R.id.etNama);
        etUsername=(EditText)findViewById(R.id.etUsername);
        etPassword=(EditText)findViewById(R.id.etPassword);
        etJawaban=(EditText)findViewById(R.id.etJawaban);
        etKelamin=(EditText)findViewById(R.id.etKelamin);
        etTempatLahir=(EditText)findViewById(R.id.etTempatLahir);
        etTanggalLahir=(EditText)findViewById(R.id.etTanggalLahir);
        etKtp=(EditText)findViewById(R.id.etKtp);
        etNomerHp=(EditText)findViewById(R.id.etNomerHp);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etAlamat=(EditText)findViewById(R.id.etAlamat);
        ivAmbilGambar=(ImageButton)findViewById(R.id.ivAmbilGambar);
        btCaptcha=(Button)findViewById(R.id.btCaptcha);
        btDaftar=(Button)findViewById(R.id.btDaftar);
        im = (ImageView)findViewById(R.id.imageView1);
        btCaptcha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Captcha c = new MathCaptcha(300, 100, MathCaptcha.MathOptions.PLUS_MINUS_MULTIPLY);
                im.setImageBitmap(c.image);
                im.setLayoutParams(new LinearLayout.LayoutParams(c.width *2, c.height *2));
                jawaban=c.answer;

            }
        });
        btCaptcha.performClick();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {

                    token = intent.getStringExtra("token");
                    loginPreferences = getSharedPreferences("chiplinmas", MODE_PRIVATE);
                    loginPreferences.edit()
                            .putString("token", token)
                            .apply();
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();

        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS != resultCode) {
            if (googleAPI.isUserResolvableError(resultCode)) {
                googleAPI.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();

            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

        } else {
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }
        ivAmbilGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Take Photo","Choose from Gallery",
                        "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            ivAmbilGambar.setAlpha((float) 0.5);
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            File file = new File( getExternalFilesDir(Environment.DIRECTORY_PICTURES),  "foto1.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Register.this, Register.this.getApplicationContext().getPackageName() + ".chiplinmas.fileprovider", file));

                            startActivityForResult(intent, CAM_REQUEST1);

                        } else if (items[item].equals("Choose from Gallery")) {
                            ivAmbilGambar.setAlpha((float) 0.5);
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);//
                            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

                        }else if (items[item].equals("Cancel")) {
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
        btDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama=etNama.getText().toString();
                tempatlahir=etTempatLahir.getText().toString();
                tanggallahir=etTanggalLahir.getText().toString();
                ktp=etKtp.getText().toString();
                alamat=etAlamat.getText().toString();
                nomerhp=etNomerHp.getText().toString();
                email=etEmail.getText().toString();
                kelamin=etKelamin.getText().toString();
                username=etUsername.getText().toString();
                password=etPassword.getText().toString();
                if(foto==null) {
                    Toast.makeText(Register.this, "Masukan Foto Dahulu", Toast.LENGTH_LONG).show();
                }
                else  if(tempatlahir.equalsIgnoreCase("")){
                    Toast.makeText(Register.this,"Silahkan Masukan Tempat Lahir Anda",Toast.LENGTH_LONG).show();
                }
                else  if(kelamin.equalsIgnoreCase("")){
                    Toast.makeText(Register.this,"Silahkan Masukan Jenis Kelamin Anda",Toast.LENGTH_LONG).show();
                }
                else  if(nama.equalsIgnoreCase("")){
                    Toast.makeText(Register.this,"Silahkan Masukan Nama Anda",Toast.LENGTH_LONG).show();
                }
                else  if(tanggallahir.equalsIgnoreCase("")){
                    Toast.makeText(Register.this,"Silahkan Masukan Tempat Lahir Anda",Toast.LENGTH_LONG).show();
                } else  if(ktp.equalsIgnoreCase("")){
                    Toast.makeText(Register.this,"Silahkan Masukan KTP anda",Toast.LENGTH_LONG).show();
                }else  if(alamat.equalsIgnoreCase("")){
                    Toast.makeText(Register.this,"Silahkan Masukan alamat anda",Toast.LENGTH_LONG).show();
                }else  if(nomerhp.equalsIgnoreCase("")){
                    Toast.makeText(Register.this,"Silahkan Masukan nomerhp anda",Toast.LENGTH_LONG).show();
                }else  if(email.equalsIgnoreCase("")) {
                    Toast.makeText(Register.this, "Silahkan Masukan email anda", Toast.LENGTH_LONG).show();
                }else  if(!jawaban.equalsIgnoreCase(etJawaban.getText().toString())){
                    Toast.makeText(Register.this,"Captcha Anda Salah",Toast.LENGTH_LONG).show();
                }else  if(username.equalsIgnoreCase("")){
                    Toast.makeText(Register.this,"Silahkan Masukan Username anda",Toast.LENGTH_LONG).show();
                }else  if(password.equalsIgnoreCase("")){
                    Toast.makeText(Register.this,"Silahkan Masukan Password anda",Toast.LENGTH_LONG).show();
                }


                else {
                    if(isNetworkAvailable()) {
                        new DaftarTask(Register.this).execute("daftar", nama, tempatlahir, tanggallahir, ktp, alamat, nomerhp, email,
                                foto, kelamin, token, username, password);
                    }else{
                        Toast.makeText(Register.this,"Internet Tidak Tersedia",Toast.LENGTH_LONG).show();
                    }
                     /* new DaftarCobaTask(Pendaftaran.this).execute("daftar",nama,foto);*/
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);
        alertDialogBuilder.setMessage("Yakin  mau membatalkan Registrasi?")
                .setCancelable(false)
                .setPositiveButton("Jangan",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();

                            }
                        });
        alertDialogBuilder.setNegativeButton("Batalkan",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                        startActivity(new Intent(Register.this,MainActivity.class));
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQUEST1 && resultCode == Activity.RESULT_OK) {
            File file = new File( getExternalFilesDir(Environment.DIRECTORY_PICTURES),  "foto1.jpg");
            bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 800, 480);
            realimage = rotate(bitmap, 90);
            ivAmbilGambar.setImageBitmap(realimage);
            ivAmbilGambar.setAlpha((float) 1.0);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            realimage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } else if (requestCode == CAM_REQUEST1 && resultCode == Activity.RESULT_CANCELED) {
            ivAmbilGambar.setAlpha((float) 1.0);

        } else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
            bitmap2 = null;
            if (data != null) {
                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
            ivAmbilGambar.setImageBitmap(bitmap2);
            ivAmbilGambar.setAlpha((float) 1.0);
        } else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_CANCELED) {
            ivAmbilGambar.setAlpha((float) 1.0);

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
    @Override
    protected void onResume() {
        super.onResume();
        if(isNetworkAvailable()) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        }
    }


    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        if (isNetworkAvailable()) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

        }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) && (activeNetwork.getState()== NetworkInfo.State.CONNECTED)){
                return true;
            } else if ((activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) && (activeNetwork.getState()== NetworkInfo.State.CONNECTED)){
                return true;
            }
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);
            alertDialogBuilder.setMessage("Ada masalah dengan internet kamu?")
                    .setCancelable(false)
                    .setPositiveButton("Refresh",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    dialog.cancel();
                                    ProgressDialog progressDialog = ProgressDialog.show(Register.this, "Please Wait",
                                            "Loading", true);
                                    Intent refresh=new Intent(Register.this,Register.class);
                                    startActivity(refresh);
                                    finish();
                                    progressDialog.dismiss();
                                }
                            });
            alertDialogBuilder.setNegativeButton("Keluar",
                    new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            dialog.cancel();
                            finish();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }return false;
    }


}
