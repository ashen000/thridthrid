package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import site.gemus.openingstartanimation.OpeningStartAnimation;

public class MainActivity extends AppCompatActivity {


    private TimerTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("1111111","there is main");
        /*
        判断token是否可用
        */
        SharedPreferences sharedPreferences=getSharedPreferences("data",Context.MODE_PRIVATE);
        String token=sharedPreferences.getString("token",null);
        if(token==null)
        {
            Log.i("1111111","there token is null");
            final Intent intent=new Intent(this,LoginActivity.class);
            Timer timer=new Timer();
            TimerTask tast=new TimerTask() {
                @Override
                public void run() {
                    startActivity(intent);
                    finish();
                }
            };
            timer.schedule(tast,3000);
        }
        else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://39.106.195.109/itnews/api/self/info")
                            .method("GET", null)
                            .addHeader("Authorization", token)
                            .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        JSONObject jsonObject=new JSONObject(response.body().string());
                        int code=jsonObject.getInt("code");
                        Log.i("1111111","there is "+String.valueOf(code));
                        if(code==1000)
                        {
                            Log.i("1111111","there is main  code is 1000");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final Intent intent2=new Intent(MainActivity.this,HomeActivity.class);
                                    Timer timer=new Timer();
                                    TimerTask tast=new TimerTask() {
                                        @Override
                                        public void run() {
                                            startActivity(intent2);
                                            finish();
                                        }
                                    };
                                    timer.schedule(tast,1500);
                                }
                            });
                        }
                        else
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final Intent intent2=new Intent(MainActivity.this,LoginActivity.class);
                                    Timer timer=new Timer();
                                    TimerTask tast=new TimerTask() {
                                        @Override
                                        public void run() {
                                            startActivity(intent2);
                                            finish();
                                        }
                                    };
                                    timer.schedule(tast,1500);
                                }
                            });
                        }
                    } catch (IOException | JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Intent intent2=new Intent(MainActivity.this,LoginActivity.class);
                                Timer timer=new Timer();
                                TimerTask tast=new TimerTask() {
                                    @Override
                                    public void run() {
                                        startActivity(intent2);
                                        finish();
                                    }
                                };
                                timer.schedule(tast,1);
                            }
                        });
                        e.printStackTrace();
                        Log.i("136378","shibai");
                    }

                }
            }).start();
        }

    }
}