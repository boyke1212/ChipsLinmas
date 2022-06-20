package com.toelve.chipslinmas;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by boyke on 7/19/2016.
 */
public class PojoMion {
    public static JSONObject jsonObject;
    public static JSONArray jsonArray;



    public static ArrayList<String> AmbilArray(String b, String datanya) {
        ArrayList<String> Arrays =  new ArrayList<>();
        try {

            Log.w("datanya", datanya);
            Arrays.clear();
            jsonObject = new JSONObject(b);
            jsonArray = jsonObject.getJSONArray("berita");
            int count = 0;
            while (count < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                String isi = jo.getString(datanya);
                Arrays.add(isi);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return Arrays;

    }



}
