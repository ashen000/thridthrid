package com.example.myapplication3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FindbackActivity extends AppCompatActivity {

    Button find_get_checknumber,find_finish;
    EditText find_backemail,find_make_password,find_checknumber;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findback_page);

        find_get_checknumber=findViewById(R.id.findget_checknumber);
        find_finish=findViewById(R.id.find_finish);
        find_backemail=findViewById(R.id.findbackemail);
        find_make_password=findViewById(R.id.findmakepassword);
        find_checknumber=findViewById(R.id.findchecknumber);

        find_get_checknumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            Gson gson=new Gson();
                            Email eemail=new Email();
                            eemail.email=find_backemail.getText().toString();
                            eemail.usage=2;
                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType,gson.toJson(eemail));
                            Request request = new Request.Builder()
                                    .url("http://39.106.195.109/itnews/api/reglog/code-reg")
                                    .post(body)
                                    .build();
                            Response response=client.newCall(request).execute();
                            JSONObject jsonObject=new JSONObject(response.body().string());
                            String msg=jsonObject.getString("msg");
                            int code=jsonObject.getInt("code");
                            if(code==996)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast toast=Toast.makeText(getApplicationContext(), "邮箱格式错误！", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                            }
                            else if (code==1000)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast toast=Toast.makeText(getApplicationContext(), "发送成功！", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        find_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            Gson gson=new Gson();
                            usedata usedata=new usedata();
                            String email,password,verify;
                            email=find_backemail.getText().toString();
                            password=find_make_password.getText().toString();
                            verify=find_checknumber.getText().toString();
                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType, "{\n    \"email\": \""+email+"\",\n    \"verify\": \""+verify+"\",\n    \"password\": \""+password+"\"\n}");
                            Request request = new Request.Builder()
                                    .url("http://39.106.195.109/itnews/api/reglog/pwd-recall")
                                    .post(body)
                                    .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                    .addHeader("Content-Type", "application/json")
                                    .build();
                            Response response=client.newCall(request).execute();
                            JSONObject jsonObject=new JSONObject(response.body().string());
                            String msg=jsonObject.getString("msg");
                            int code=jsonObject.getInt("code");
                            if(code==996)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast toast=Toast.makeText(getApplicationContext(), "错误，请重试！", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                            }
                            else if(code==1000)
                            {
                                finish();

                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                finish();
            }
        });



    }
}
