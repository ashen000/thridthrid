package com.example.myapplication3;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ChangeActivity extends AppCompatActivity {

    EditText makenickname,makeinfo,makegender;
    String smakenickname,smakeinfo,smakegender;
    Button doit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_page);

        makenickname=findViewById(R.id.makenickname);
        makeinfo=findViewById(R.id.makeinfo);
        makegender=findViewById(R.id.makegender);
        doit=findViewById(R.id.doit);
        doit.setText("完成");
        doit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                smakenickname=makenickname.getText().toString();
                smakeinfo=makeinfo.getText().toString();
                smakegender=makegender.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sharedPreferences=getSharedPreferences("data",Context.MODE_PRIVATE);
                        String token=sharedPreferences.getString("token",null);

                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, "{\n    \"info\": \""+smakeinfo+"\",\n    \"nickname\": \""+smakenickname+"\",\n    \"gender\": "+smakegender+"\n}");
                        Log.i("qwe",body.toString());
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/self/info-refresh")
                                .method("POST", body)
                                .addHeader("Authorization",token)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .addHeader("Content-Type", "application/json")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            JSONObject jsonObject=new JSONObject(response.body().string());
                            int code=jsonObject.getInt("code");
                            String msg=jsonObject.getString("msg");
                            if(code==1000)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ChangeActivity.this , "更改成功" , Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                            Log.i("qwe",msg);

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

            }
        });
    }

}
