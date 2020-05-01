package com.uzitech.trackcovid_19;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
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

    void saveData(String response) {
        try {
            FileOutputStream fileOutputStream;
            fileOutputStream = activity.openFileOutput(String.valueOf(R.string.app_name), Context.MODE_PRIVATE);
            fileOutputStream.write(response.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            Toast.makeText(activity, "Cannot save data!", Toast.LENGTH_SHORT).show();
        }
    }

    JSONArray readData() {
        try {
            FileInputStream fileInputStream;
            fileInputStream = activity.openFileInput(String.valueOf(R.string.app_name));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return new JSONArray(stringBuilder.toString());
        } catch (Exception e) {
            return null;
        }
    }

}
