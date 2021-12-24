package com.example.myapplication3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MycontextActivity extends AppCompatActivity {

    Button button;

    List<Map<String,Object>> list=new ArrayList<>();
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    WAdapter wAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycontext_page);

        button=findViewById(R.id.backto3);
        recyclerView=findViewById(R.id.recyclerview_my);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        SharedPreferences sharedPreferences=getSharedPreferences("data",Context.MODE_PRIVATE);
        String token=sharedPreferences.getString("token",null);
        list.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/self/news-ids")
                        .method("GET",null)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    int code=jsonObject.getInt("code");
                    String msg=jsonObject.getString("msg");
                    Log.i("Mycontext","Seccussfully post");
                    if(code==1000)
                    {
                        JSONObject jsonObject2=new JSONObject(jsonObject.getString("data"));
                        JSONArray jsonArray=jsonObject2.getJSONArray("news");
                        Log.i("Mycontext","code is 1000");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
//                            Log.i("Mycontext","JSONobject is"+jsonObject1.toString());
                            String id=String.valueOf(jsonObject1.getInt("id"));
                            String title=jsonObject1.getString("title");
                            String head="http://39.106.195.109/media/newspics/up_image/image-20210303144633961_eUlIsoy.png";
                            if(jsonObject1.getString("news_pics_set").length()>4)
                            {
                                Log.i("Mycontext","进来了");
                                JSONArray jsonArray1=jsonObject1.getJSONArray("news_pics_set");
                                head=jsonArray1.getString(0);
                                Log.i("Mycontext",head);
                            }
                            Map<String,Object> map=new HashMap<>();
                            map.put("id",id);
                            map.put("title",title);
                            map.put("head",head);
                            list.add(map);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(MycontextActivity.this,"long is "+list.size(),Toast.LENGTH_SHORT).show();
                                LinearLayoutManager layoutManager = new LinearLayoutManager(MycontextActivity.this);
                                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(layoutManager);
                                myAdapter= new MyAdapter();
                                recyclerView.setAdapter(myAdapter);
                            }
                        });
                    }
                } catch (IOException | JSONException e) {

                    e.printStackTrace();
                }
            }
        }).start();




    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MycontextActivity.this).inflate(R.layout.item_list, parent, false);
            MyViewHolder myViewHolder= new MyViewHolder(view);
            return myViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
            holder.title.setText(list.get(i).get("title").toString());
            Glide.with(MycontextActivity.this).load(list.get(i).get("head").toString()).into(holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(),InnerActivity.class);
                    intent.putExtra("id",list.get(i).get("id").toString());
                    v.getContext().startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.person2);
            imageView=itemView.findViewById(R.id.myhead2);
        }
    }


    public class WAdapter extends RecyclerView.Adapter<WViewHolder>{

        @NonNull
        @Override
        public WViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(MycontextActivity.this).inflate(R.layout.error_list,parent,false);
            WViewHolder wViewHolder=new WViewHolder(view);
            return wViewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull WViewHolder holder, int i) {
            holder.textView.setText(list.get(i).get("title").toString());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class WViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public WViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.error);

        }
    }

}


