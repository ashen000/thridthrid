package com.example.myapplication3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BlankFragment1 extends Fragment {

    List<Map<String,Object>> list=new ArrayList<>(),list2=new ArrayList<>();
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    WAdapter wAdapter;
    RefreshLayout refreshLayout;
    int page;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank1, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        refreshLayout=getView().findViewById(R.id.smartrefresh);
        recyclerView=getActivity().findViewById(R.id.recyclerview1);


        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        String token=sharedPreferences.getString("token",null);
        list.clear();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    page=1;
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://39.106.195.109/itnews/api/news/recommend/v4?page="+String.valueOf(page)+"&size=9")
                            .method("GET", null)
                            .addHeader("Authorization", token)
                            .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        JSONObject jsonObject=new JSONObject(response.body().string());
                        int code=jsonObject.getInt("code");
                        String msg=jsonObject.getString("msg");
                        if(code==1000)
                        {
                            JSONObject jsonObject2=new JSONObject(jsonObject.getString("data"));
                            JSONArray jsonArray=jsonObject2.getJSONArray("news");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String id=String.valueOf(jsonObject1.getInt("id"));
                                String title=jsonObject1.getString("title");
                                String head="http://39.106.195.109/media/newspics/up_image/image-20210303144633961_eUlIsoy.png";
                                if(jsonObject1.getString("news_pics_set").length()>4)
                                {
                                    JSONArray jsonArray1=jsonObject1.getJSONArray("news_pics_set");
                                    head=jsonArray1.getString(0);
                                }

                                Map<String,Object> map=new HashMap<>();
                                map.put("id",id);
                                map.put("title",title);
                                map.put("head",head);
                                list.add(map);
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myAdapter=new MyAdapter();
                                    recyclerView.setAdapter(myAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                }
                            });
                        }
                        else
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext() ,code+msg, Toast.LENGTH_SHORT).show();
                                }
                            });

                            Map<String,Object> map=new HashMap<>();
                            map.put("title","加载错误");
                            list2.add(map);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    wAdapter= new WAdapter();
                                    recyclerView.setAdapter(wAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                }
                            });
                        }
                    } catch (IOException | JSONException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext() , "加载错误" , Toast.LENGTH_SHORT).show();

                            }
                        });
                        Map<String,Object> map=new HashMap<>();
                        map.put("title","加载错误");
                        list2.add(map);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wAdapter= new WAdapter();
                                recyclerView.setAdapter(wAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    list.clear();
                    page=1;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            page=1;
                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://39.106.195.109/itnews/api/news/recommend/v4?page="+String.valueOf(page)+"&size=8")
                                    .method("GET", null)
                                    .addHeader("Authorization", token)
                                    .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                    .build();
                            try {
                                Response response = client.newCall(request).execute();
                                JSONObject jsonObject=new JSONObject(response.body().string());
                                Log.i("123",jsonObject.toString());
                                int code=jsonObject.getInt("code");
                                String msg=jsonObject.getString("msg");
                                if(code==1000)
                                {
                                    JSONObject jsonObject2=new JSONObject(jsonObject.getString("data"));
                                    JSONArray jsonArray=jsonObject2.getJSONArray("news");

                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                        String id=String.valueOf(jsonObject1.getInt("id"));
                                        String title=jsonObject1.getString("title");
                                        String head="http://39.106.195.109/media/newspics/up_image/image-20210303144633961_eUlIsoy.png";
                                        if(jsonObject1.getString("news_pics_set").length()>4)
                                        {
                                            JSONArray jsonArray1=jsonObject1.getJSONArray("news_pics_set");
                                            head=jsonArray1.getString(0);
                                        }


                                        Map<String,Object> map=new HashMap<>();
                                        map.put("id",id);
                                        map.put("title",title);
                                        map.put("head",head);
                                        list.add(map);
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            myAdapter=new MyAdapter();
                                            recyclerView.setAdapter(myAdapter);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        }
                                    });
                                }
                                else
                                {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext() ,code+msg, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Map<String,Object> map=new HashMap<>();
                                    map.put("title","加载错误");
                                    list2.add(map);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            wAdapter= new WAdapter();
                                            recyclerView.setAdapter(wAdapter);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        }
                                    });
                                }
                            } catch (IOException | JSONException e) {
                                Map<String,Object> map=new HashMap<>();
                                map.put("title","加载错误");
                                list2.add(map);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wAdapter= new WAdapter();
                                        recyclerView.setAdapter(wAdapter);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    refreshLayout.finishRefresh();

                }
            });
            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    page++;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://39.106.195.109/itnews/api/news/recommend/v4?page="+String.valueOf(page)+"&size=8")
                                    .method("GET", null)
                                    .addHeader("Authorization", token)
                                    .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                    .build();
                            try {
                                Response response = client.newCall(request).execute();
                                JSONObject jsonObject=new JSONObject(response.body().string());
                                Log.i("qweqwe",String.valueOf(jsonObject.getInt("code")));
                                int code=jsonObject.getInt("code");
                                String msg=jsonObject.getString("msg");
                                if(code==1000)
                                {
                                    JSONObject jsonObject2=new JSONObject(jsonObject.getString("data"));
                                    Log.i("qweqwe",jsonObject2.toString());

                                    JSONArray jsonArray=jsonObject2.getJSONArray("news");
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                        String id=String.valueOf(jsonObject1.getInt("id"));
                                        String title=jsonObject1.getString("title");
                                        String head="http://39.106.195.109/media/newspics/up_image/image-20210303144633961_eUlIsoy.png";
                                        if(jsonObject1.getString("news_pics_set").length()>4)
                                        {
                                            JSONArray jsonArray1=jsonObject1.getJSONArray("news_pics_set");
                                            head=jsonArray1.getString(0);
                                        }
                                        Map<String,Object> map=new HashMap<>();
                                        map.put("id",id);
                                        map.put("title",title);
                                        map.put("head",head);
                                        list.add(map);
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                                myAdapter.notifyDataSetChanged();
                                                refreshLayout.finishLoadMore(true);
                                        }
                                    });
                                }
                                else
                                {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext() ,code+msg, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Map<String,Object> map=new HashMap<>();
                                    map.put("title","加载错误");
                                    list2.add(map);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            wAdapter= new WAdapter();
                                            recyclerView.setAdapter(wAdapter);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        }
                                    });
                                }
                            } catch (IOException | JSONException e) {
                                if(page>1)
                                {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity() , "无更多内容" , Toast.LENGTH_SHORT).show();
                                            refreshLayout.finishLoadMore(true);
                                        }
                                    });
                                }

                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.person_list, parent, false);
            MyViewHolder myViewHolder=new MyViewHolder(view);
            return myViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
            holder.title.setText(list.get(i).get("title").toString());
            if(list.get(i).get("head").toString()==null)
            {
                Glide.with(getContext()).load("http://39.106.195.109/media/newspics/up_image/image-20210303144633961_eUlIsoy.png").into(holder.imageView);
            }
            else
            {
                Glide.with(getContext()).load(list.get(i).get("head").toString()).into(holder.imageView);

            }
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
            title=itemView.findViewById(R.id.person);
            imageView=itemView.findViewById(R.id.myhead);
        }
    }

   public class WAdapter extends RecyclerView.Adapter<WViewHolder>{

       @NonNull
       @Override
       public WViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view=LayoutInflater.from(getActivity()).inflate(R.layout.error_list,parent,false);
           WViewHolder wViewHolder=new WViewHolder(view);
           return wViewHolder;
       }

       @Override
       public void onBindViewHolder(@NonNull WViewHolder holder, int i) {
            holder.textView.setText(list2.get(i).get("title").toString());

       }

       @Override
       public int getItemCount() {
           return list2.size();
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
