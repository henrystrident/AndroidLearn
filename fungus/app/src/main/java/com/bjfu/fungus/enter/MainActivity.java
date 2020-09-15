package com.bjfu.fungus.enter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bjfu.fungus.R;
import com.bjfu.fungus.User.AboutUs;
import com.bjfu.fungus.User.Help;
import com.bjfu.fungus.User.Login;
import com.bjfu.fungus.User.Register;
import com.bjfu.fungus.User.Setting;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private CircleImageView head;
    private TextView headUsernameView;

    private FragmentCollect fragmentCollect;
    private FragmentRecord fragmentRecord;
    private Fragment currentFragment;

    private final int LOGIN = 0;

    private final int DISPLAY_AVATAR_SERVER = 0;
    private final int DISPLAY_AVATAR_DEFAULT = 1;
    private final int CHECK_CONNECTION_SUCCEED= 2;
    private final int CHECK_CONNECTION_FAIL = 3;

    private final int REQUEST_OPEN_ALBUM = 100;
    private final int REQUEST_SMALL_IMAGE_CUTTING = 101;

    private boolean checkConnection;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case DISPLAY_AVATAR_SERVER:
                    Bitmap avatar = (Bitmap) msg.obj;
                    head.setImageBitmap(avatar);
                    break;
                case DISPLAY_AVATAR_DEFAULT:
                    head.setImageResource(R.drawable.fungus);
                    break;
                case CHECK_CONNECTION_FAIL:
                    checkConnection = false;
                    break;
                case CHECK_CONNECTION_SUCCEED:
                    if (msg.obj.equals("success"))
                    {
                        checkConnection = true;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        initNetwork();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        initMenu();
        drawerLayout.addDrawerListener(new mDrawerListener());

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        initFragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(showFragment);

        getPermission();
    }


    /**
     * 初始化网络参数，ip和port
     */
    private void initNetwork()
    {
        String ip = "http://192.168.31.164:";
        String port = "8085/fungus-admin/android/";
        SharedPreferences.Editor editor = getSharedPreferences("networkSetting", MODE_PRIVATE).edit();
        editor.putString("ip", ip);
        editor.putString("port", port);
        editor.apply();
    }


    /**
     * 用于初始化toolbar中的菜单栏，包括点击出现以及图标的设置
     * 设置滑动菜单中的点击事件
     */
    private void initMenu()
    {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
        }



        navigationView = findViewById(R.id.sideNavigation);
        navigationView.setCheckedItem(R.id.sideNavigationMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId())
                {
                    // 登录
                    case R.id.login_item:
                        intent = new Intent(MainActivity.this, Login.class);
                        startActivityForResult(intent, LOGIN);
                        return true;
                    // 注册
                    case R.id.register_item:
                        intent = new Intent(MainActivity.this, Register.class);
                        startActivity(intent);
                        return true;
                    // 设置
                    case R.id.setting_item:
                        String username = getSharedPreferences("CURRENT_USER_INFO", MODE_PRIVATE)
                                .getString("username", "");
                        if (TextUtils.isEmpty(username))
                        {
                            Toast.makeText(MainActivity.this, "未登录，不能进行用户设置", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            intent = new Intent(MainActivity.this, Setting.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        }
                        return true;
                    // 关于我们
                    case R.id.aboutUs_item:
                        intent = new Intent(MainActivity.this, AboutUs.class);
                        startActivity(intent);
                        return true;
                    // 帮助
                    case R.id.help_item:
                        intent = new Intent(MainActivity.this, Help.class);
                        startActivity(intent);
                        return true;
                    default:
                        return true;
                }

            }
        });

    }


    /**
     设置toolbar点击事件
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                String username = getSharedPreferences("CURRENT_USER_INFO", MODE_PRIVATE).getString("username", "");
                //显示当前用户用户名
                headUsernameView = findViewById(R.id.headUsername);
                headUsernameView.setText(username);
                // 显示当前用户头像
                head = findViewById(R.id.headAvatar);
                initAvatar();

                head.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        String username = getSharedPreferences("CURRENT_USER_INFO", MODE_PRIVATE).getString("username", "");
                        if (!username.equals(""))
                        {
                            checkConnection();
                            updateAvatar();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "未登录，不能上传头像", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
        return true;
    }

    private void initFragment()
    {
        fragmentCollect = new FragmentCollect();
        fragmentRecord = new FragmentRecord();
        currentFragment = fragmentCollect;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainView, fragmentCollect);
        fragmentTransaction.commit();
    }

    /**
     * bottomNavigation点击事件，切换页面
     */
    private BottomNavigationView.OnNavigationItemSelectedListener showFragment = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {

                case R.id.BottomNavigationCollect:
                    switchFragment(fragmentCollect);
                    return true;
                case R.id.BottomNavigationRecord:
                    String username = getSharedPreferences("CURRENT_USER_INFO",MODE_PRIVATE)
                            .getString("username", "");
                    if (TextUtils.isEmpty(username))
                    {
                        Toast.makeText(MainActivity.this, "未登录，不能查看记录", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        switchFragment(fragmentRecord);
                    }
                    return true;
            }
            return false;
        }
    };

    /**
     * 切换fragment
     */
    private void switchFragment(Fragment targetFragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(currentFragment);
        if (!targetFragment.isAdded())
        {
            transaction.add(R.id.mainView, targetFragment);
        }
        transaction.show(targetFragment);
        currentFragment = targetFragment;
        transaction.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case LOGIN:
                if (resultCode == 0)
                {

                    if (data != null)
                    {
                        String usernameLogin;
                        String trueNameLogin;
                        int collectIndex;
                        // 登陆后显示用户名，并得到上次上传的下标
                        usernameLogin = data.getStringExtra("username");
                        trueNameLogin = data.getStringExtra("trueName");
                        collectIndex = data.getIntExtra("collectIndex", 0);

                        headUsernameView.setText(usernameLogin);

                        // 保存当前用户
                        SharedPreferences.Editor editor = getSharedPreferences("CURRENT_USER_INFO", MODE_PRIVATE).edit();
                        editor.putString("username", usernameLogin);
                        editor.putString("trueName", trueNameLogin);
                        editor.putInt("collectIndex", collectIndex);
                        editor.apply();

                        //下载，保存，并显示头像
                        downloadAvatar();
                    }
                }
                break;
            case REQUEST_OPEN_ALBUM:
                Uri uri;
                if (data != null && data.getData()!= null)
                {
                    uri = data.getData();
                    startSmallPhotoZoom(uri);
                }
                break;
            case REQUEST_SMALL_IMAGE_CUTTING:
                if (data != null)
                {
                    Bitmap avatar = Objects.requireNonNull(data.getExtras()).getParcelable("data");

                    if (checkConnection)
                    {
                        head.setImageBitmap(avatar);
                        saveAvatar(avatar);
                        uploadAvatar();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "与服务器连接失败，无法上传", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            default:
                break;
        }
    }

    /**
     * 打开app后头像的显示
     */
    private void initAvatar()
    {
        Log.d("initAvatar", "执行了");
        String username = getSharedPreferences("CURRENT_USER_INFO", MODE_PRIVATE).getString("username", "");
        if (!TextUtils.isEmpty(username))
        {
            File file = new File(this.getExternalCacheDir(), "avatar.jpg");
            if (file.exists())
            {
                try
                {
                    FileInputStream inputStream = new FileInputStream(file.getAbsolutePath());
                    Bitmap avatar = BitmapFactory.decodeStream(inputStream);
                    head.setImageBitmap(avatar);
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                downloadAvatar();
            }
        }
    }

    /**
     * 下载头像，并保存，并且直接显示在head中
     */
    private void downloadAvatar()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("networkSetting", MODE_PRIVATE);
        String ip = sharedPreferences.getString("ip", "");
        String port = sharedPreferences.getString("port", "");
        String httpURL = ip+port+"downloadavatar";
        String username = getSharedPreferences("CURRENT_USER_INFO", MODE_PRIVATE).getString("username", "");
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = null;

        body = builder
                .add("username", username)
                .build();

        Request request = new Request.Builder()
                .post(body)
                .url(httpURL)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(MainActivity.this, "下载头像失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                byte[] avatar = Objects.requireNonNull(response.body()).bytes();
                Log.d("头像测试",String.valueOf(avatar.length));
                if (avatar.length !=0 )
                {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
                    Message message = new Message();
                    message.what = DISPLAY_AVATAR_SERVER;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                    saveAvatar(bitmap);
                }
                else
                {
                    Message message = new Message();
                    message.what = DISPLAY_AVATAR_DEFAULT;
                    handler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 保存下载的头像
     * @param avatar bitmap格式的头像
     */
    private void saveAvatar(Bitmap avatar)
    {
        File file = new File(this.getExternalCacheDir(), "avatar.jpg");
        try
        {
            if (file.exists())
            {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            avatar.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 当前是否与服务器连接，不连接无法更新头像
     */
    private void checkConnection()
    {

        SharedPreferences sharedPreferences = getSharedPreferences("networkSetting", MODE_PRIVATE);
        String ip = sharedPreferences.getString("ip", "");
        String port = sharedPreferences.getString("port", "");
        String HttpURL = ip+port+"checkconnection";
        String username = getSharedPreferences("CURRENT_USER_INFO", MODE_PRIVATE).getString("username", "");

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("username", username).build();
        Request request = new Request.Builder()
                .url(HttpURL)
                .post(requestBody)
                .build();



        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.what = CHECK_CONNECTION_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Message message = new Message();
                message.what = CHECK_CONNECTION_SUCCEED;
                message.obj = Objects.requireNonNull(response.body()).string();
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 点击头像后进行更新
     */
    private void updateAvatar()
    {
        //检查有无权限，无权限则申请
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else
        {
            openAlbum();
        }
    }

    // 申请权限回调结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
                if (grantResults.length !=0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    openAlbum();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "访问相册被拒绝", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //打开相册
    private void openAlbum()
    {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_OPEN_ALBUM);
    }

    // 图片裁剪
    public void startSmallPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300); // 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_SMALL_IMAGE_CUTTING);
    }

    /**
     * 上传头像
     */
    private void uploadAvatar()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("networkSetting", MODE_PRIVATE);
        String ip = sharedPreferences.getString("ip", "");
        String port = sharedPreferences.getString("port", "");
        String requestURL = ip+port+"updateavatar";
        String username = getSharedPreferences("CURRENT_USER_INFO", MODE_PRIVATE).getString("username", "");

        File file = new File(MainActivity.this.getExternalCacheDir(), "avatar.jpg");
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("avatar", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file));
        builder.addFormDataPart("username", username);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(requestURL)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(MainActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Looper.prepare();
                Toast.makeText(MainActivity.this, Objects.requireNonNull(response.body()).string(), Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }

    class mDrawerListener implements DrawerLayout.DrawerListener
    {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            String username = getSharedPreferences("CURRENT_USER_INFO", MODE_PRIVATE).getString("username", "");
            //显示当前用户用户名
            headUsernameView = findViewById(R.id.headUsername);
            headUsernameView.setText(username);
            head = findViewById(R.id.headAvatar);
            initAvatar();

            head.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    String username = getSharedPreferences("CURRENT_USER_INFO", MODE_PRIVATE).getString("username", "");
                    if (!username.equals(""))
                    {
                        checkConnection();
                        updateAvatar();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "未登录，不能上传头像", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }

    private void getPermission()
    {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty())
        {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
        
    }

}
