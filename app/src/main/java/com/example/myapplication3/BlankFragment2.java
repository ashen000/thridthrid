package com.example.myapplication3;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BlankFragment2 extends Fragment {

    private static final int RESULT_OK =-1;
    private static final int CHOSE_PHOTO =2;
    public static final int TAKE_PHOTO =1;
    private static final int SHOW_PHOTO =4;

    Button backto2,fabiao;
    EditText maketitle,makecontent;
    ImageView makeimage;

    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
    Date curDate = new Date(System.currentTimeMillis());
    String str = format.format(curDate);
    String str2 = format.format(curDate)+"22";
    private Uri imageUri,cropUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_blank2,null);
       makeimage=view.findViewById(R.id.makeimage);

       Log.i("123123","time1");

        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeimage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder dialog = null;
                                dialog = new AlertDialog.Builder(getContext());
                                dialog.setMessage("请选择：");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("相机", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            selectemake();
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                dialog.setNegativeButton("相册", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selectechoose();
                                    }
                                });
                                dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                dialog.show();


                            }
                        });
                    }
                });
            }
        }).start();

        return inflater.inflate(R.layout.fragment_blank2, container, false);
    }

    public void selectemake() throws MalformedURLException {


        File outputImage=new File(getActivity().getExternalCacheDir(),str+".jpg");
        try {
            if(outputImage.exists())
            {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT>=24){
            imageUri= FileProvider.getUriForFile(getActivity(),"ccv.turbosnail.photo_demo.fileprovider",outputImage);
        }
        else {
            imageUri = Uri.fromFile(outputImage);
        }

        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
        URL url=outputImage.toURI().toURL();


    }

    private void selectechoose()
    {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        else {
            openAlbum();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        backto2=getActivity().findViewById(R.id.backto2);
        fabiao=getActivity().findViewById(R.id.fabiao);
        maketitle=getActivity().findViewById(R.id.maketitle);
        makecontent=getActivity().findViewById(R.id.makecontent);
        makeimage=getActivity().findViewById(R.id.makeimage);

        backto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maketitle.setText("");
                makecontent.setText("");
                makeimage.setImageResource(R.drawable.more2);
            }
        });

        fabiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  stitle,scontent;
                stitle=maketitle.getText().toString();
                scontent=makecontent.getText().toString();
                Log.i("123123",stitle);
                Log.i("123123",scontent);
                SharedPreferences sharedPreferences=getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                String token=sharedPreferences.getString("token",null);
                Log.i("123123",token);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, "title="+stitle+"&content="+scontent+"&tag=1&img_ids=12");
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/news/release")
                                .method("POST", body)
                                .addHeader("Authorization", token)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            JSONObject jsonObject=new JSONObject(response.body().string());
                            Log.i("123123",jsonObject.toString());
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }

    public void cropPhoto(Uri uri){
        File cropImage=new File(getActivity().getExternalCacheDir(),str2+".jpg");
        try {
            if(cropImage.exists())
            {
                cropImage.delete();
            }
            cropImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT>=24){
            cropUri=FileProvider.getUriForFile(getActivity(),"ccv.turbosnail.photo_demo.fileprovider",cropImage);
        }
        else {
            cropUri = Uri.fromFile(cropImage);
        }
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        intent.putExtra("crop","true");
        intent.putExtra("aspectX",10);
        intent.putExtra("aspectY",10);
        intent.putExtra("return-data",false);
        intent.putExtra("output",cropUri);
        getActivity().startActivityForResult(intent,SHOW_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,@NonNull Intent data){
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode==RESULT_OK){
                    cropPhoto(imageUri);
                }
                break;
            case CHOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    cropPhoto(uri);
                }
                break;

            case SHOW_PHOTO:

                if (resultCode==RESULT_OK){
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(cropUri));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),cropUri.toString(), Toast.LENGTH_SHORT).show();


                            }
                        });
                        makeimage.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;// 将裁剪后的照片显示出来


        }
    }
}
