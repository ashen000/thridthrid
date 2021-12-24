package com.example.myapplication3;

import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    String user;
    String token;
    int code;
    String password;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        EditText edittext1,edittext2;
        Button button0;
        TextView textView1,textView2;
        String date;
        edittext1=findViewById(R.id.account);
        edittext2=findViewById(R.id.password);
        edittext2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        button0=findViewById(R.id.button00);
        textView1=findViewById(R.id.T_enroll);
        textView2=findViewById(R.id.T_findback);

        token=null;
        usertoken usertoken=new usertoken();
        usertoken.token=token;
        SharedPreferences sharedPreferences=getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("token",usertoken.getToken());
        editor.commit();
        Intent intent=getIntent();
        token=null;
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            Gson gson=new Gson();
                            login login=new login();
                            user =edittext1.getText().toString();
                            password =edittext2.getText().toString();
                            if(password.length()>4)
                            {
                                login.username=user;
                                login.password=password.trim();
                                MediaType mediaType = MediaType.parse("application/json");
                                RequestBody body = RequestBody.create(mediaType,gson.toJson(login));
                                Request request = new Request.Builder()
                                        .url("http://39.106.195.109/itnews/api/reglog/all-log")
                                        .post(body)
                                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                        .addHeader("Content-Type", "application/json")
                                        .build();
                                Response response=client.newCall(request).execute();
                                JSONObject jsonObject=new JSONObject(response.body().string());
                                String msg=jsonObject.getString("msg");
                                code =jsonObject.getInt("code");
                                if(code ==1000)
                                {
                                    String data = jsonObject.getString("data");
                                    JSONObject jsonObject1=new JSONObject(data);
                                    token=jsonObject1.getString("token");
                                    usertoken usertoken=new usertoken();
                                    usertoken.token=token;
                                    SharedPreferences sharedPreferences=getSharedPreferences("data", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putString("token",usertoken.getToken());
                                    editor.commit();
                                    Log.i("asdasdasdasd","there is token *"+token);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                                Intent intent1=new Intent(getApplicationContext(),HomeActivity.class);
                                                startActivity(intent1);
                                                finish();

                                        }
                                    });
                                }
                                else
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            token=null;
                                            usertoken usertoken=new usertoken();
                                            usertoken.token=token;
                                            SharedPreferences sharedPreferences=getSharedPreferences("data", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor=sharedPreferences.edit();
                                            editor.putString("token",usertoken.getToken());
                                            editor.commit();
                                            Toast toast=Toast.makeText(getApplicationContext(), "密码或账号错误，请重试！", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    });
                                }
                            }
                            else
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        token=null;
                                        usertoken usertoken=new usertoken();
                                        usertoken.token=token;
                                        SharedPreferences sharedPreferences=getSharedPreferences("data", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putString("token",usertoken.getToken());
                                        editor.commit();
                                        Toast toast=Toast.makeText(getApplicationContext(), "密码或账号错误，请重试！", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                            }
                        } catch (IOException | JSONException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast=Toast.makeText(getApplicationContext(), "网络连接错误！", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();

                //跳转
            }
        });






        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(LoginActivity.this,EnrollActivity.class);
                startActivity(intent2);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(LoginActivity.this,FindbackActivity.class);
                startActivity(intent2);
            }
        });
    }
}
