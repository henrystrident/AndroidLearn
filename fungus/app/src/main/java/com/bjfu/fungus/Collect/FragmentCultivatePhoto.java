package com.bjfu.fungus.Collect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bjfu.fungus.R;
import com.bjfu.fungus.Utils.GeneratePhotoPath;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FragmentCultivatePhoto extends Fragment implements View.OnClickListener {

    private FloatingActionButton shot;
    private ImageView photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9;
    private AlertDialog.Builder alertDialog;

    private List<ImageView> viewList = new ArrayList<>();

    // 空余view下标
    private Deque<Integer> availableView = new LinkedList<>();
    // 对应view的照片数组
    public HashMap<Integer, String> pathMap = new HashMap<Integer, String>();
    // head表示第一个能存储照片的imageView在列表中的下标， tail是最后一个
    private int head=0, tail=0;

    private Uri imageUri;
    private String currentPath;

    // 启动相机的requestCode
    private static final int TAKE_PHOTO=0;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_cultivate, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        initView();
        
    }

    private void initView()
    {
        shot = (getActivity()).findViewById(R.id.takeCultivatePhoto);
        shot.setOnClickListener(this);

        photo1 = (getActivity()).findViewById(R.id.cultivate1);
        photo2 = (getActivity()).findViewById(R.id.cultivate2);
        photo3 = (getActivity()).findViewById(R.id.cultivate3);
        photo4 = (getActivity()).findViewById(R.id.cultivate4);
        photo5 = (getActivity()).findViewById(R.id.cultivate5);
        photo6 = (getActivity()).findViewById(R.id.cultivate6);
        photo7 = (getActivity()).findViewById(R.id.cultivate7);
        photo8 = (getActivity()).findViewById(R.id.cultivate8);
        photo9 = (getActivity()).findViewById(R.id.cultivate9);

        photo1.setOnClickListener(this);photo2.setOnClickListener(this);
        photo3.setOnClickListener(this);photo4.setOnClickListener(this);
        photo5.setOnClickListener(this);photo6.setOnClickListener(this);
        photo7.setOnClickListener(this);photo8.setOnClickListener(this);
        photo9.setOnClickListener(this);

        viewList.add(photo1);viewList.add(photo2);viewList.add(photo3);
        viewList.add(photo4);viewList.add(photo5);viewList.add(photo6);
        viewList.add(photo7);viewList.add(photo8);viewList.add(photo9);



        alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("确定删除这张照片吗");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.takeCultivatePhoto:
                if (pathMap.size() == 9)
                {
                    Toast.makeText(getContext(), "最多拍摄9张照片", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    takePhoto();
                }
                break;
            case R.id.cultivate1:
                if (pathMap.get(0) != null)
                {
                    delPhoto(photo1, pathMap.get(0), 0);
                }
                break;
            case R.id.cultivate2:
                if (pathMap.get(1) != null)
                {
                    delPhoto(photo2, pathMap.get(1), 1);
                }
                break;
            case R.id.cultivate3:
                if (pathMap.get(2) != null)
                {
                    delPhoto(photo3, pathMap.get(2), 2);
                }
            case R.id.cultivate4:
                if (pathMap.get(3) != null)
                {
                    delPhoto(photo4, pathMap.get(3), 3);
                }
                break;
            case R.id.cultivate5:
                if (pathMap.get(4) != null)
                {
                    delPhoto(photo5, pathMap.get(4), 4);
                }
                break;
            case R.id.cultivate6:
                if (pathMap.get(5) != null)
                {
                    delPhoto(photo6, pathMap.get(5), 5);
                }
                break;
            case R.id.cultivate7:
                if (pathMap.get(6) != null)
                {
                    delPhoto(photo7, pathMap.get(6), 6);
                }
                break;
            case R.id.cultivate8:
                if (pathMap.get(7) != null)
                {
                    delPhoto(photo8, pathMap.get(7), 7);
                }
                break;
            case R.id.cultivate9:
                if (pathMap.get(8) != null)
                {
                    delPhoto(photo9, pathMap.get(8), 8);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点击拍照按钮后，调出拍照界面
     */
    private void takePhoto()
    {
        // 申请权限
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }
        else
        {
            // 通过文件获取Uri，传给相机
            currentPath = GeneratePhotoPath.getName()+".jpg";
            File file = new File((getActivity()).getExternalCacheDir(), currentPath);
            if (Build.VERSION.SDK_INT >= 24)
            {
                imageUri = FileProvider.getUriForFile(getContext(), "com.bjfu.fungus.fileProvider", file);
            }
            else
            {
                imageUri = Uri.fromFile(file);
            }
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);

        }
    }

    /**
     * 拍照后，根据结果进行显示和路径的增加
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK)
                {
                    try
                    {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        if ( availableView.peekLast() != null )
                        {
                            head = availableView.peekFirst();
                            viewList.get(head).setImageBitmap(bitmap);
                            pathMap.put(head, getActivity().getExternalCacheDir()+"/"+currentPath);

                            availableView.removeFirst();
                            head = tail;
                        }
                        else
                        {
                            viewList.get(head).setImageBitmap(bitmap);
                            pathMap.put(head, getActivity().getExternalCacheDir()+"/"+currentPath);
                            head += 1;
                            tail += 1;
                        }

                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }

                break;
            default:
                break;
        }
    }

    /**
     * 点击删除照片
     */
    private void delPhoto(final ImageView imageView, final String photoPath, final int index)
    {
        alertDialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 对应imageView不显示图片
                imageView.setImageBitmap(null);
                // 删除对应文件
                File file = new File(photoPath);
                if (file.exists())
                {
                    file.delete();
                }
                pathMap.remove(index);
                if (availableView.peekFirst() != null)
                {
                    if (index < availableView.peekFirst())
                    {
                        availableView.offerFirst(index);
                    }
                    else
                    {
                        availableView.offerLast(index);
                    }
                }
                else
                {
                    availableView.offerLast(index);
                }
            }
        });
        alertDialog.show();
    }
}
