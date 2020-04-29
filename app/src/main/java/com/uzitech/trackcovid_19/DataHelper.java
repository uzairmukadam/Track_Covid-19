package com.uzitech.trackcovid_19;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

class DataHelper {

    private Activity activity;

    DataHelper(Activity activity) {
        this.activity = activity;
    }

    boolean getGlobal(){
        //final String url="https://pomber.github.io/covid19/timeseries.json";
        final String url="https://corona.lmao.ninja/v2/countries";
        final boolean[] status = new boolean[1];

        StringRequest request=new StringRequest(0, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                saveData(response);
                status[0] =true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Cannot connect! Please check network", Toast.LENGTH_SHORT).show();
                status[0] =false;
            }
        });

        Volley.newRequestQueue(activity).add(request);
        return status[0];
    }

    private void saveData(String response) {
        try {
            FileOutputStream fileOutputStream;
            fileOutputStream = activity.openFileOutput(String.valueOf(R.string.app_name), Context.MODE_PRIVATE);
            fileOutputStream.write(response.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            Toast.makeText(activity, "Cannot save data!", Toast.LENGTH_SHORT).show();
        }
    }

    JSONObject readData() {
        try {
            FileInputStream fileInputStream;
            fileInputStream = activity.openFileInput(String.valueOf(R.string.app_name));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return new JSONObject(line);
        } catch (Exception e) {
            return null;
        }
    }

}
