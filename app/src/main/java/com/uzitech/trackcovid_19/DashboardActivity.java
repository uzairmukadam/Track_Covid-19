package com.uzitech.trackcovid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    DataHelper dataHelper;
    
    String curr_country;
    String[] countryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        
        dataHelper=new DataHelper(this);

        curr_country=getCountry();
        getData(dataHelper.readData());
    }

    private void getData(JSONObject object) {
        setCountryNames(object);
    }

    private void setCountryNames(JSONObject object) {
        Iterator<String> key=object.keys();
        countryList=new String[object.length()];
        int i=0;
        while(key.hasNext()){
            String name=key.next();
            countryList[i]=name;
            i++;
        }
    }

    private String getCountry() {
        String country_name = null;
        LocationManager lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(getApplicationContext());
        assert lm != null;
        for(String provider: lm.getAllProviders()) {
            @SuppressWarnings("ResourceType") Location location = lm.getLastKnownLocation(provider);
            if(location!=null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if(addresses != null && ((List) addresses).size() > 0) {
                        country_name = addresses.get(0).getCountryName();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        assert country_name != null;
        return country_name.toLowerCase();
    }
}
