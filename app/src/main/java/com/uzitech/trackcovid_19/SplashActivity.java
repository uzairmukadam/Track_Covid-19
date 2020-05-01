package com.uzitech.trackcovid_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SplashActivity extends AppCompatActivity {
    
    DataHelper dataHelper;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        dataHelper=new DataHelper(this);
        intent=new Intent(this, DashboardActivity.class);
        
        checkLocationPermission();
        getGlobal();
    }

    private void getGlobal() {
        final String url="https://corona.lmao.ninja/v2/countries";

        StringRequest request=new StringRequest(0, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataHelper.saveData(response);
                loadDashboard();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashActivity.this, "Cannot connect to network", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        Volley.newRequestQueue(this).add(request);
    }

    private void loadDashboard() {
        startActivity(intent);
        finish();
    }

    private void checkLocationPermission() {
        if((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION))!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
}
