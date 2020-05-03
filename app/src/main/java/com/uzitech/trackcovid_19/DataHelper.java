package com.uzitech.trackcovid_19;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

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

    String getURL(String country){
        if(country.toLowerCase().equals("india")){
            return "https://api.covid19india.org/data.json";
        } else if(country.toLowerCase().equals("united states")) {
            return "https://corona.lmao.ninja/v2/states";
        }else {
            return "";
        }
    }

    JSONArray formatData(String response, String country){
        if(country.toLowerCase().equals("india")){
            return indiaObject(response);
        }else if(country.toLowerCase().equals("united states")){
            return usaObject(response);
        }else{
            return null;
        }
    }

    private JSONArray usaObject(String response) {
        try {
            JSONArray array = new JSONArray();
            JSONArray temp = new JSONArray(response);
            for(int i=0; i<temp.length(); i++){
                JSONObject tempObj=new JSONObject();
                JSONObject object=temp.getJSONObject(i);
                tempObj.put("state", object.getString("state"));
                tempObj.put("cases", object.getInt("cases"));
                tempObj.put("active", object.getInt("active"));
                tempObj.put("deaths", object.getInt("deaths"));
                array.put(tempObj);
            }
            return array;
        }catch (Exception e){
            return null;
        }
    }

    private JSONArray indiaObject(String response) {
        try {
            JSONObject obj=new JSONObject(response);
            JSONArray array = new JSONArray();
            JSONArray temp = obj.getJSONArray("statewise");
            for(int i=0; i<temp.length(); i++){
                JSONObject tempObj=new JSONObject();
                JSONObject object=temp.getJSONObject(i);
                if(!object.getString("state").equals("Total")) {
                    tempObj.put("state", object.getString("state"));
                    tempObj.put("confirm", object.getInt("confirmed"));
                    tempObj.put("active", object.getInt("active"));
                    tempObj.put("deaths", object.getInt("deaths"));
                    tempObj.put("recoverd", object.getInt("recovered"));
                    array.put(tempObj);
                }
            }
            return array;
        }catch (Exception e){
            return null;
        }
    }

}
