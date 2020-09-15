package com.bjfu.fungus.Record;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bjfu.fungus.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentRecordWildFormPhoto extends Fragment {

    private String ip, port, collectNumber, username;

    private ImageView photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9;
    private ImageView bigPhoto;
    private Dialog dialog;


    private Deque<String> pathList = new LinkedList<>();
    private Deque<ImageView> imageViewList;

    private static final int GET_IMAGE_PATH_SUCCEED=0;
    private static final int NETWORK_ERROR=1;
    private static final int GET_IMAGE_SUCCEED=2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case GET_IMAGE_PATH_SUCCEED:
                    if ((String)msg.obj == null)
                    {
                        Toast.makeText(getContext(), "无野外环境照片", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        for (String path: ((String)msg.obj).split(";"))
                        {
                            pathList.addLast(path);
                        }
                        getImage(pathList.pollFirst());
                    }
                    break;
                case GET_IMAGE_SUCCEED:
                    byte[] byteImage = (byte[]) msg.obj;
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
                    ImageView currentImageView = imageViewList.pollFirst();
                    currentImageView.setImageBitmap(bitmap);

                    currentImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Light);
                            setBigPhoto(bitmap);
                            dialog.setContentView(bigPhoto);
                            dialog.show();
                        }
                    });
                    getImage(pathList.pollFirst());
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(getContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_wild_form_photo, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ip= getArguments().getString("ip");
        port = getArguments().getString("port");
        username = getArguments().getString("username");
        collectNumber = getArguments().getString("collectNumber");

        initView();

        getPhotoPaths();

    }

    private void initView()
    {

        photo1 = (getActivity()).findViewById(R.id.record_wildForm1);
        photo2 = (getActivity()).findViewById(R.id.record_wildForm2);
        photo3 = (getActivity()).findViewById(R.id.record_wildForm3);
        photo4 = (getActivity()).findViewById(R.id.record_wildForm4);
        photo5 = (getActivity()).findViewById(R.id.record_wildForm5);
        photo6 = (getActivity()).findViewById(R.id.record_wildForm6);
        photo7 = (getActivity()).findViewById(R.id.record_wildForm7);
        photo8 = (getActivity()).findViewById(R.id.record_wildForm8);
        photo9 = (getActivity()).findViewById(R.id.record_wildForm9);


        imageViewList = new LinkedList<>();
        imageViewList.addLast(photo1);imageViewList.addLast(photo2);imageViewList.addLast(photo3);
        imageViewList.addLast(photo4);imageViewList.addLast(photo5);imageViewList.addLast(photo6);
        imageViewList.addLast(photo7);imageViewList.addLast(photo8);imageViewList.addLast(photo9);

    }

    /**
     * 询问服务器环境照片有哪些
     */
    private void getPhotoPaths()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("category", "wildForm")
                .add("username", username)
                .add("collectNumber", collectNumber)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(ip+port+"getphotopath")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Message message = new Message();
                message.what = GET_IMAGE_PATH_SUCCEED;
                message.obj = response.body().string();
                handler.sendMessage(message);
            }
        });
    }


    /**
     * 通过路径请求照片
     */
    private void getImage(String path)
    {
        if (path == null)
        {
            return;
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new FormBody.Builder()
                .add("path", path)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(ip+port+"getphoto")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.what = NETWORK_ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Message message = new Message();
                message.what = GET_IMAGE_SUCCEED;
                message.obj = response.body().bytes();
                handler.sendMessage(message);
            }
        });
    }

    private void setBigPhoto(Bitmap bitmap)
    {
        bigPhoto = new ImageView(getContext());
        bigPhoto.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        bigPhoto.setImageBitmap(bitmap);
        bigPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
