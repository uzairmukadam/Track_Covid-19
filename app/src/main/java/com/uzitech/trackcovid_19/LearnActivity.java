package com.uzitech.trackcovid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;

public class LearnActivity extends AppCompatActivity {

    FrameLayout symptomsPage, carePage;
    boolean clickSym=false, clickCare=false;
    Button symp, care;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        symptomsPage=findViewById(R.id.symptoms_page);
        carePage=findViewById(R.id.care_page);
        symp=findViewById(R.id.sym_butt);
        care=findViewById(R.id.care_butt);
    }

    public void loadSymptom (View view){
        if(!clickSym){
            clickSym=true;
            resetButton();
            symp.setBackground(getDrawable(R.drawable.rounded_button_selected));
            symptomsPage.setVisibility(View.VISIBLE);
        }else{
            clickSym=false;
            resetButton();
        }
    }

    public void loadCare (View view){
        if(!clickCare){
            clickCare=true;
            resetButton();
            care.setBackground(getDrawable(R.drawable.rounded_button_selected));
            carePage.setVisibility(View.VISIBLE);
        }else{
            clickCare=false;
            resetButton();
        }
    }

    void resetButton(){
        symp.setBackground(getDrawable(R.drawable.rounded_button));
        care.setBackground(getDrawable(R.drawable.rounded_button_selected));
        symptomsPage.setVisibility(View.GONE);
        carePage.setVisibility(View.GONE);
    }
}
