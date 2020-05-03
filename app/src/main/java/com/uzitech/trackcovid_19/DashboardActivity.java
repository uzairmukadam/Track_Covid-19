package com.uzitech.trackcovid_19;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    DataHelper dataHelper;
    Spinner countries;
    TextView currCases, currDeaths, currRecovery, currNewCases, currNewDeaths, currActive;
    ImageView flag;
    ImageButton resetLocation, moreInfo;

    String curr_country, selected_country, moreURL;
    String[] countryList, countryFlag;
    int[] countryCases, countryDeaths, countryRecovers, newCases, newDeaths, countryActive;
    int globalCases, globalDeaths, globalRecovers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dataHelper = new DataHelper(this);
        countries = findViewById(R.id.country_list);
        currCases = findViewById(R.id.curr_cases);
        currNewCases = findViewById(R.id.new_cases);
        currDeaths = findViewById(R.id.curr_deaths);
        currNewDeaths = findViewById(R.id.new_deaths);
        currRecovery = findViewById(R.id.curr_recovers);
        currActive = findViewById(R.id.curr_active);
        flag = findViewById(R.id.flag);
        resetLocation = findViewById(R.id.curr_location);
        moreInfo = findViewById(R.id.learn);

        curr_country = getCountry();
        getData(dataHelper.readData());

        countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setData(position);
                selected_country = countryList[position];
                setURL(countryList[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        resetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countries.setSelection(getCurrIndex());
            }
        });

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, LearnActivity.class));
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });
    }

    private void setData(int position) {
        currCases.setText((NumberFormat.getNumberInstance(Locale.US).format(countryCases[position])));
        currDeaths.setText((NumberFormat.getNumberInstance(Locale.US).format(countryDeaths[position])));
        currRecovery.setText((NumberFormat.getNumberInstance(Locale.US).format(countryRecovers[position])));
        currNewCases.setText("+" + (NumberFormat.getNumberInstance(Locale.US).format(newCases[position])));
        currNewDeaths.setText("+" + (NumberFormat.getNumberInstance(Locale.US).format(newDeaths[position])));
        currActive.setText("Active: " + (NumberFormat.getNumberInstance(Locale.US).format(countryActive[position])));
        Glide.with(this).load(countryFlag[position]).into(flag);
    }

    private void getData(JSONArray array) {
        setCountryNames(array);
    }

    private void setCountryNames(JSONArray array) {
        countryList = new String[array.length()];
        countryFlag = new String[array.length()];
        countryCases = new int[array.length()];
        countryDeaths = new int[array.length()];
        countryRecovers = new int[array.length()];
        newCases = new int[array.length()];
        newDeaths = new int[array.length()];
        countryActive = new int[array.length()];
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                countryList[i] = checkCountry(object.getString("country"));
                countryFlag[i] = object.getJSONObject("countryInfo").getString("flag");
                countryCases[i] = object.getInt("cases");
                countryDeaths[i] = object.getInt("deaths");
                countryRecovers[i] = object.getInt("recovered");
                newCases[i] = object.getInt("todayCases");
                newDeaths[i] = object.getInt("todayDeaths");
                countryActive[i] = object.getInt("active");
            }
        } catch (Exception ignored) {
        }
        calculateGlobal();
        setSpinner();
    }

    private String checkCountry(String country) {
        if (country.toLowerCase().equals("usa")) {
            country = "United States";
        } else if (country.toLowerCase().equals("uk")) {
            country = "United Kingdom";
        }
        return country;
    }

    private void calculateGlobal() {
        for (int i = 0; i < countryList.length; i++) {
            globalCases += countryCases[i];
            globalDeaths += countryDeaths[i];
            globalRecovers += countryRecovers[i];
        }
        TextView cases = findViewById(R.id.global_cases);
        TextView deaths = findViewById(R.id.global_deaths);
        TextView recovery = findViewById(R.id.global_recovers);

        cases.setText((NumberFormat.getNumberInstance(Locale.US).format(globalCases)));
        deaths.setText((NumberFormat.getNumberInstance(Locale.US).format(globalDeaths)));
        recovery.setText((NumberFormat.getNumberInstance(Locale.US).format(globalRecovers)));
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryList) {
            public View getView(int position, View view, ViewGroup parent) {
                View v = super.getView(position, view, parent);
                v.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                ((TextView) v).setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_bold));
                return v;
            }

            public View getDropDownView(int position, View view, ViewGroup parent) {
                View v = super.getView(position, view, parent);
                v.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                ((TextView) v).setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_regular));
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countries.setAdapter(adapter);

        countries.setSelection(getCurrIndex());
    }

    private int getCurrIndex() {
        int index = 0;
        for (int i = 0; i < countryList.length; i++) {
            if (curr_country.equals(countryList[i].toLowerCase())) {
                index = i;
                break;
            }
        }
        return index;
    }

    private String getCountry() {
        String country_name = null;
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(getApplicationContext());
        assert lm != null;
        for (String provider : lm.getAllProviders()) {
            @SuppressWarnings("ResourceType") Location location = lm.getLastKnownLocation(provider);
            if (location != null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && ((List) addresses).size() > 0) {
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

    void setURL(String name) {
        if (dataHelper.getURL(name).equals("")) {
            findViewById(R.id.show_more).setVisibility(View.GONE);
            moreURL = "";
        } else {
            findViewById(R.id.show_more).setVisibility(View.VISIBLE);
            moreURL = dataHelper.getURL(name);
        }
    }

    public void loadMore(View view) {
        Intent intent = new Intent(this, MoreActivity.class);
        intent.putExtra("url", moreURL);
        intent.putExtra("country", selected_country);
        startActivity(intent);
    }
}
