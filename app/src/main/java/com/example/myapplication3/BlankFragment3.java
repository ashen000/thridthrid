package com.example.myapplication3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class BlankFragment3 extends Fragment {

    private static final int RESULT_OK =-1;
    private static final int CHOSE_PHOTO =2;
    public static final int TAKE_PHOTO =1;
    private static final int SHOW_PHOTO =4;


    Button exit,next,change;
    TextView username, nickname, info, gender,star_num,my_context;
    String susername="null", snickname="null", sinfo="null", sgender="null", savatar,sstar_num="null";
    String token;
    ImageView avatar;
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
    Date curDate = new Date(System.currentTimeMillis());
    String str = format.format(curDate);
    String str2 = format.format(curDate)+"22";
    private Uri imageUri,cropUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        token = bundle.getString("token");
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
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if (code == 1000) {
                        String data = jsonObject.getString("data");
                        JSONObject jsonObject1 = new JSONObject(data);
                        susername = jsonObject1.getString("username");
                        snickname = jsonObject1.getString("nickname");
                        sinfo = jsonObject1.getString("info");
                        sgender = jsonObject1.getString("gender");
                        savatar = jsonObject1.getString("avatar");
                        sstar_num=jsonObject1.getString("star_num");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                username = getView().findViewById(R.id.username);
                                nickname = getView().findViewById(R.id.nickname);
                                info = getView().findViewById(R.id.info);
                                gender = getView().findViewById(R.id.gender);
                                avatar = getView().findViewById(R.id.avatar);
                                star_num=getView().findViewById(R.id.star_num);
//                                Glide.with(getContext()).load(savatar).into(avatar);
                                avatar.setOnClickListener(new View.OnClickListener() {
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
                                my_context.setText("我的文章");
                                star_num.setText("收藏数:"+sstar_num);
                                exit.setText("退出登录");
                                username.setText("用户名:" + susername);
                                nickname.setText("昵称:" + snickname);
                                info.setText("简介:" + sinfo);
                                gender.setText("性别:" + sgender);

                            }
                        });
                    }
                    else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                username = getView().findViewById(R.id.username);
                                nickname = getView().findViewById(R.id.nickname);
                                info = getView().findViewById(R.id.info);
                                gender = getView().findViewById(R.id.gender);
                                avatar = getView().findViewById(R.id.avatar);
                                star_num=getView().findViewById(R.id.star_num);
                                Glide.with(getContext()).load(savatar).into(avatar);
                                avatar.setOnClickListener(new View.OnClickListener() {
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
                                my_context.setText("我的文章");
                                star_num.setText("收藏数:"+sstar_num);
                                exit.setText("退出登录");
                                username.setText("用户名:" + susername);
                                nickname.setText("昵称:" + snickname);
                                info.setText("简介:" + sinfo);
                                gender.setText("性别:" + sgender);

                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return inflater.inflate(R.layout.fragment_blank3, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        next=getActivity().findViewById(R.id.next);
        exit=getActivity().findViewById(R.id.exit);
        change=getActivity().findViewById(R.id.change);
        my_context=getActivity().findViewById(R.id.my_context);
        next.setText("修改信息");
        exit.setText("退出登录");
        change.setText("修改密码");


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ChangeActivity.class);
                startActivity(intent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FindbackActivity.class);
                startActivity(intent);
            }
        });
        my_context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MycontextActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onResume() {
        super.onResume();
        Log.i("blk3","onresume");
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
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    int code = Integer.valueOf(jsonObject.getString("code"));
                    if (code == 1000) {
                        String data = jsonObject.getString("data");
                        JSONObject jsonObject1 = new JSONObject(data);
                        susername = jsonObject1.getString("username");
                        snickname = jsonObject1.getString("nickname");
                        sinfo = jsonObject1.getString("info");
                        sgender = jsonObject1.getString("gender");
                        savatar = jsonObject1.getString("avatar");
                        sstar_num=jsonObject1.getString("star_num");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                username = getView().findViewById(R.id.username);
                                nickname = getView().findViewById(R.id.nickname);
                                info = getView().findViewById(R.id.info);
                                gender = getView().findViewById(R.id.gender);
                                avatar = getView().findViewById(R.id.avatar);
                                star_num=getView().findViewById(R.id.star_num);

//                                Glide.with(getContext()).load(savatar).into(avatar);
                                avatar.setOnClickListener(new View.OnClickListener() {
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
                                star_num.setText("收藏数:"+sstar_num);
                                next.setText("修改信息");
                                exit.setText("退出登录");
                                username.setText("用户名:" + susername);
                                nickname.setText("昵称:" + snickname);
                                info.setText("简介:" + sinfo);
                                gender.setText("性别:" + sgender);
                            }
                        });

                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

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
            imageUri=FileProvider.getUriForFile(getActivity(),"ccv.turbosnail.photo_demo.fileprovider",outputImage);
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

    private void openAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent,CHOSE_PHOTO);
    }
    public void cropPhoto(Uri uri){
        File cropImage = new File(Environment.getExternalStorageDirectory(), "crop_image.jpg");
        String path = cropImage.getAbsolutePath();
        try {
            if (cropImage.exists()) {
                cropImage.delete();
            }
            cropImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        cropUri = Uri.fromFile(cropImage);
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        intent.putExtra("crop","true");
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //设置读权限
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputY",200);
        intent.putExtra("outputX",200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data",false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,cropUri);


        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent,SHOW_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,@NonNull Intent data){
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode==RESULT_OK){       //  当拍照成功后，会返回一个返回码，这个值为 -1 — RESULT_OK
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

                    String path=getRealPathFromUri(getContext(),cropUri);
//                    Log.i("path","path is"+path);
//
//                    String filePath = getContext().getClass().getResource("/storage/emulated/0/crop_image.jpg").getPath();
//                    Log.i("path","path2 is"+filePath);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), path, Toast.LENGTH_SHORT).show();
                        }
                    });

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences sharedPreferences=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
                            String token=sharedPreferences.getString("token",null);

//                            Log.i("123123","the path is    "+path);
//                            Log.i("123123","the token is"+token);


                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            MediaType mediaType = MediaType.parse("text/plain");
                            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("img","file",
                                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                                    new File(path)))
                                    .addFormDataPart("type","1")
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://39.106.195.109/itnews/api/img-upload")
                                    .method("POST", body)
                                    .addHeader("Authorization",token)
                                    .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                    .build();
                            try
                            {
                                Log.i("123123","time 123");
                                Response response = client.newCall(request).execute();
                                Log.i("123123","time 123");
                                JSONObject jsonObject=new JSONObject(response.body().string());
                                Log.i("123123","iiii"+jsonObject.toString());

                            } catch (IOException | JSONException e) {
                                Log.i("123123","jjjjjjjjjjjjjjjj 123");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "time" , Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    }
                    break;// 将裁剪后的照片显示出来
        }
    }




    public static String getRealPathFromUri(Context context, Uri uri) {
        if(context == null || uri == null) {
            return null;
        }

        Log.i("asdasd",uri.toString());
        if("file".equalsIgnoreCase(uri.getScheme())) {
            Log.i("asdasd","path is"+getRealPathFromUri_asd(context,uri));
            return getRealPathFromUri_asd(context,uri);
        } else if("content".equalsIgnoreCase(uri.getScheme())) {
            Log.i("asdasd","cotent");

            return getRealPathFromUri_Api11To18(context,uri);
        }
        return getRealPathFromUri_AboveApi19(context, uri);//没用到
    }
    private static String getRealPathFromUri_asd(Context context,Uri uri){
        String uri2Str = uri.toString();
        return uri2Str.substring(uri2Str.indexOf(":") + 3);
    }
    @SuppressLint("NewApi")
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = null;

        wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = { MediaStore.Images.Media.DATA };
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = { id };

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    /**
     * //适配api11-api18,根据uri获取图片的绝对路径。
     * 针对图片URI格式为Uri:: content://media/external/images/media/1028
     */
    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };

        CursorLoader loader = new CursorLoader(context, uri, projection, null,
                null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }


}
