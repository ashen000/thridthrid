package com.example.myapplication3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    BlankFragment1 blankFragment1=new BlankFragment1();
    BlankFragment2 blankFragment2=new BlankFragment2();
    BlankFragment3 blankFragment3=new BlankFragment3();


//    TextView username,nickname,info,gender,like_num,star_num,follow_num,fans_num,avatar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        Log.i("1111111","there is home");


        SharedPreferences sharedPreferences=getSharedPreferences("data", Context.MODE_PRIVATE);
        String token=sharedPreferences.getString("token","");
        Log.i("1111111",token);



        Button button1=findViewById(R.id.button1);
        Button button2=findViewById(R.id.button2);
        Button button3=findViewById(R.id.button3);
        Bundle bundle1=new Bundle();
        bundle1.putString("token",token);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setSelected(true);
                button2.setSelected(false);
                button3.setSelected(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,blankFragment1).commit();
                blankFragment1.setArguments(bundle1);
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setSelected(false);
                button2.setSelected(true);
                button3.setSelected(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,blankFragment2).commit();
            }
        });
        Bundle bundle3=new Bundle();
        bundle3.putString("token",token);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setSelected(false);
                button2.setSelected(false);
                button3.setSelected(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,blankFragment3).commit();
                blankFragment3.setArguments(bundle3);
            }
        });
        button1.setSelected(true);
        button2.setSelected(false);
        button3.setSelected(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,blankFragment1).commit();
    }





}
