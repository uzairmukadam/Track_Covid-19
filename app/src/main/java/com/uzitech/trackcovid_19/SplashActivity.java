package com.uzitech.trackcovid_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

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
        if(dataHelper.getGlobal()){
            loadDashboard();
        }else{
            Toast.makeText(this, "Please connect to network!", Toast.LENGTH_SHORT).show();
            finish();
        }
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
