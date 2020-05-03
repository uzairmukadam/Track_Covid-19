package com.uzitech.trackcovid_19;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.Iterator;
import java.util.Objects;

public class MoreActivity extends AppCompatActivity {

    JSONArray data;
    DataHelper dataHelper;
    int size;
    String[] header;
    String[][] dataset;
    LinearLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        dataHelper = new DataHelper(this);
        table = findViewById(R.id.table);

        getData();
    }

    private void getData() {
        StringRequest request = new StringRequest(0, getIntent().getStringExtra("url"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                data = dataHelper.formatData(response, Objects.requireNonNull(getIntent().getStringExtra("country")));
                processData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MoreActivity.this, "Cannot get local data!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Volley.newRequestQueue(this).add(request);
    }

    private void processData() {
        try {
            size = data.getJSONObject(0).length();
            header = new String[size];
            dataset = new String[data.length()][size];
            Iterator<String> key = data.getJSONObject(0).keys();
            int a = 0;
            while (key.hasNext()) {
                header[a] = key.next();
                a++;
            }
            for (int i = 0; i < data.length(); i++) {
                for (int j = 0; j < size; j++) {
                    dataset[i][j] = data.getJSONObject(i).getString(header[j]);
                }
            }
            setData();
        } catch (Exception ignored) {}
    }

    private void setData() {
        LinearLayout headerLayout = findViewById(R.id.header);
        for (int i = 0; i < size; i++) {
            TextView title = new TextView(this);
            title.setText(header[i]);
            title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            title.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_bold));
            title.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            headerLayout.addView(title);
        }

        for (String[] strings : dataset) {
            LinearLayout row = new LinearLayout(this);
            row.setPadding(0, 2, 0, 2);
            for (int j = 0; j < size; j++) {
                TextView cell = new TextView(this);
                cell.setText(strings[j]);
                cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                if (j == 0) {
                    cell.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_bold));
                    cell.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                } else {
                    cell.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_regular));
                    cell.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                }
                cell.setBackground(getDrawable(R.drawable.cell_border));
                row.addView(cell);
            }
            table.addView(row);
        }
    }
}
