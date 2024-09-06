package com.example.githubbrowser;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class ExploreActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        Toolbar titleBar = findViewById(R.id.xml_title_bar);
        titleBar.setTitle("Explore");
        titleBar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(titleBar);

        Button button1 = findViewById(R.id.sign_in);
        Button button2 = findViewById(R.id.button5);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.main).setVisibility(View.GONE);
                findViewById(R.id.xml_title_bar).setVisibility(View.GONE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in,R.anim.fade_out,R.anim.fade_in,R.anim.slide_out);
                transaction.replace(R.id.fragment_container, new Trending());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.main).setVisibility(View.GONE);
                findViewById(R.id.xml_title_bar).setVisibility(View.GONE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in,R.anim.fade_out,R.anim.fade_in,R.anim.slide_out);
                transaction.replace(R.id.fragment_container, new AwesomeLists());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }
    @Override
    public void onBackPressed(){
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            findViewById(R.id.xml_title_bar).setVisibility(View.VISIBLE);
            findViewById(R.id.main).setVisibility(View.VISIBLE);
        }
        else{
            super.onBackPressed();
        }
    }


}