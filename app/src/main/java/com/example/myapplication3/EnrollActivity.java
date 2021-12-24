package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EnrollActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enroll);

        final EditText edittext1,edittext2,editText3,editText4;
        Button button1,button2;
        final String[] email = new String[1];
        final String usage;
        final String[] all = new String[1];

        edittext1=findViewById(R.id.makeaccount);
        edittext2=findViewById(R.id.makepassword);
        edittext2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText3=findViewById(R.id.email);
        editText4=findViewById(R.id.checknumber);
        button1=findViewById(R.id.get_checknumber);
        button2=findViewById(R.id.finish);

        Intent intent=getIntent();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            Gson gson=new Gson();
                            Email eemail=new Email();
                            email[0]=editText3.getText().toString();
                            eemail.email=email[0];
                            eemail.usage=1;
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
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            Gson gson=new Gson();
                            usedata usedata=new usedata();
                            usedata.username=edittext1.getText().toString();
                            usedata.password=edittext2.getText().toString();
                            usedata.email=email[0];
                            usedata.verify=editText4.getText().toString();
                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType,gson.toJson(usedata));
                            Request request = new Request.Builder()
                                    .url("http://39.106.195.109/itnews/api/reglog/all-reg")
                                    .post(body)
                                    .build();
                            Response response=client.newCall(request).execute();
                            JSONObject jsonObject=new JSONObject(response.body().string());
                            String msg=jsonObject.getString("msg");
                            int code=jsonObject.getInt("code");
                            if(code==996)
                            {
//                                Toast toast=Toast.makeText(getApplicationContext(), "错误，请重试！", Toast.LENGTH_SHORT);
//                                toast.show();
                            }
                            else
                            {
                                String data=jsonObject.getString("data");
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
