package com.example.myapplication3;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InnerActivity extends AppCompatActivity {
    String id,token,create_time;
    String his_nickname,his_avatar,his_username;
    ImageView his_avatars;
    private List<String> urls = new ArrayList<>();
    TextView titlein,authorin,contentin,timein,hisuser;
    Button bbbk;
    TextView likenum,starnum;
    ImageView imageView1,imageView2,imageView3,like_image,star_image;
    int isl,iss,islike,isstar;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_page);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        Log.i("123",String.valueOf(id));


        titlein=findViewById(R.id.titlein);
        authorin=findViewById(R.id.authorin);
        contentin=findViewById(R.id.contentin);
        timein=findViewById(R.id.timein);
        bbbk=findViewById(R.id.backin);
        likenum=findViewById(R.id.like_num);
        starnum=findViewById(R.id.star_num);
        imageView1=findViewById(R.id.show1);
        imageView2=findViewById(R.id.show2);
        imageView3=findViewById(R.id.show3);
        like_image=findViewById(R.id.like_image);
        star_image=findViewById(R.id.star_image);
        his_avatars=findViewById(R.id.his_avatar);
        hisuser=findViewById(R.id.hisuser);

        bbbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sharedPreferences=getSharedPreferences("data", Context.MODE_PRIVATE);
        token=sharedPreferences.getString("token",null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/info/"+id+"/info-full")
                        .method("GET", null)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    Log.i("123",jsonObject.toString());
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    String title=jsonObject1.getString("title");
                    String author_id=String.valueOf(jsonObject1.getInt("author_id"));
                    String content=jsonObject1.getString("content");
                    String like_num=String.valueOf(jsonObject1.getInt("like_num"));
                    String star_num=String.valueOf(jsonObject1.getInt("star_num"));
                    create_time=jsonObject1.getString("create_time");
                    create_time=create_time.substring(0,10);
                    JSONArray jsonArray=jsonObject1.getJSONArray("pics");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        urls.add(jsonArray.getString(i));
                    }
                    islike=jsonObject1.getInt("isLike");
                    isstar=jsonObject1.getInt("isStar");
                    Log.i("is?",String.valueOf(islike));
                    Log.i("is?",String.valueOf(isstar));


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient client2 = new OkHttpClient().newBuilder()
                                    .build();
                            Request request2 = new Request.Builder()
                                    .url("http://39.106.195.109/itnews/api/users/"+author_id+"/info-alpha")
                                    .method("GET", null)
                                    .addHeader("Authorization", token)
                                    .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                    .build();
                            try {
                                Response response2 = client2.newCall(request2).execute();
                                JSONObject jsonObject3=new JSONObject(response2.body().string());
                                Log.i("avatar",jsonObject3.toString());
                                JSONObject jsonObject2=new JSONObject(jsonObject3.getString("data"));
                                his_nickname=jsonObject2.getString("nickname");
                                Log.i("avatar",his_nickname);
                                his_avatar=jsonObject2.getString("avatar");
                                Log.i("avatar",his_avatar);
                                his_username=jsonObject2.getString("username");
                                Log.i("avatar",his_username);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        titlein.setText(title);
                                        contentin.setText(content);
                                        authorin.setText(his_nickname);
                                        hisuser.setText(his_username);
                                        timein.setText(create_time);
                                        Glide.with(InnerActivity.this).load(his_avatar).into(his_avatars);
                                    }
                                });



                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final int[] like = {Integer.valueOf(like_num)};
                            final int[] star = {Integer.valueOf(star_num)};
                            isl = islike;
                            iss = isstar;
                            likenum.setText(String.valueOf(like[0]));
                            starnum.setText(String.valueOf(star[0]));

                            star_image.setSelected(iss==1);
                            star_image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(iss==1)
                                    {
                                        iss=0;
                                        star[0]--;
                                    }
                                    else{
                                        iss=1;
                                        star[0]++;
                                    }
                                    starnum.setText(String.valueOf(star[0]));
                                    star_image.setSelected(iss==1);
                                }
                            });

                            like_image.setSelected(isl==1);
                            like_image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(isl==1)
                                    {
                                        isl=0;
                                        like[0]--;
                                    }
                                    else{
                                        isl=1;
                                        like[0]++;
                                    }
                                    likenum.setText(String.valueOf(like[0]));
                                    like_image.setSelected(isl==1);
                                }
                            });
                            if(urls.size()==1)
                            {
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(650,
                                        650);//两个400分别为添加图片的大小
                                imageView1.setLayoutParams(params);
                                imageView1.setImageResource(R.drawable.edit_back);
                                Glide.with(InnerActivity.this).load(urls.get(0)).into(imageView1);
                            }
                            if(urls.size()==2)
                            {
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(400,400);//两个400分别为添加图片的大小
                                imageView1.setLayoutParams(params);
                                imageView2.setLayoutParams(params);
                                imageView1.setImageResource(R.drawable.edit_back);
                                imageView2.setImageResource(R.drawable.edit_back);
                                Glide.with(InnerActivity.this).load(urls.get(0)).into(imageView1);
                                Glide.with(InnerActivity.this).load(urls.get(1)).into(imageView2);

                            }
                            if (urls.size()>3)
                            {
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250,
                                        250);
                                imageView1.setLayoutParams(params);
                                imageView2.setLayoutParams(params);
                                imageView3.setLayoutParams(params);
                                imageView1.setImageResource(R.drawable.edit_back);
                                imageView2.setImageResource(R.drawable.edit_back);
                                imageView3.setImageResource(R.drawable.edit_back);
                                Glide.with(InnerActivity.this).load(urls.get(0)).into(imageView1);
                                Glide.with(InnerActivity.this).load(urls.get(1)).into(imageView2);
                                Glide.with(InnerActivity.this).load(urls.get(2)).into(imageView3);
                            }
                        }
                    });

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("pause",String.valueOf(islike)+"1");
        Log.i("pause",String.valueOf(isl)+"2");
        Log.i("pause",String.valueOf(isstar)+"3");
        Log.i("pause",String.valueOf(iss)+"4");
        if(isl!=islike)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    MediaType mediaType = MediaType.parse("text/plain");
                    RequestBody body = RequestBody.create(mediaType, "");
                    Request request = new Request.Builder()
                            .url("http://39.106.195.109/itnews/api/news/operator/"+id+"/like")
                            .method("POST", body)
                            .addHeader("Authorization", token)
                            .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        if(iss!=isstar)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    MediaType mediaType = MediaType.parse("text/plain");
                    RequestBody body = RequestBody.create(mediaType, "");
                    Request request = new Request.Builder()
                            .url("http://39.106.195.109/itnews/api/news/operator/"+id+"/star")
                            .method("POST", body)
                            .addHeader("Authorization",token)
                            .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

    }
}


