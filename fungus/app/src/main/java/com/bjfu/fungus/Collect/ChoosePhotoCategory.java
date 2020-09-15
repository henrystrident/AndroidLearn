package com.bjfu.fungus.Collect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bjfu.fungus.R;
import com.bjfu.fungus.enter.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Map;
import java.util.Stack;

/**
 * 选择照片种类的页面
 * 这个活动给CollectInformationBasic返回4组照片路径
 * 使用底部导航条切换拍照种类
 */

public class ChoosePhotoCategory extends AppCompatActivity {

    private FragmentEnvironmentPhoto fragmentEnvironmentPhoto;
    private FragmentWildFormPhoto fragmentWildFormPhoto;
    private FragmentLabPhoto fragmentLabPhoto;
    private FragmentCultivatePhoto fragmentCultivatePhoto;
    private Fragment currentFragment;
    private ConstraintLayout photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo_category);

        BottomNavigationView bottomNavigationView = findViewById(R.id.photoBottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(showFragment);

        initToolbar();

        initFragment();

    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.takePhotoToolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.saveItem:
                Intent intent = new Intent();
                intent.putExtra("environmentPath", setEnvironmentPah());
                intent.putExtra("wildFormPath", setWildFormPath());
                intent.putExtra("labPath", setLabPath());
                intent.putExtra("cultivatePath", setCultivatePath());
                setResult(RESULT_OK, intent);
                finish();
                return true;
            default:
                return true;
        }
    }

    private void initFragment()
    {


        fragmentEnvironmentPhoto = new FragmentEnvironmentPhoto();
        fragmentWildFormPhoto = new FragmentWildFormPhoto();
        fragmentLabPhoto = new FragmentLabPhoto();
        fragmentCultivatePhoto = new FragmentCultivatePhoto();

        photoView = findViewById(R.id.photoView);

        // 显示第一个类别
        getSupportFragmentManager().beginTransaction().replace(R.id.photoView, fragmentEnvironmentPhoto).commit();
        currentFragment = fragmentEnvironmentPhoto;


    }

        private BottomNavigationView.OnNavigationItemSelectedListener showFragment = new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {

                    case R.id.categoryGrowEnvironment:
                        switchFragment(fragmentEnvironmentPhoto).commit();
                        return true;
                    case R.id.categoryWildForm:
                        switchFragment(fragmentWildFormPhoto).commit();
                        return true;
                    case R.id.categoryLab:
                        switchFragment(fragmentLabPhoto).commit();
                        return true;
                    case R.id.categoryCultivate:
                        switchFragment(fragmentCultivatePhoto).commit();
                        return true;
                }
                return false;
            }
        };


        private FragmentTransaction switchFragment(Fragment fragment)
        {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(currentFragment);
            if (!fragment.isAdded())
            {
                transaction.add(R.id.photoView, fragment, fragment.getClass().getName());
            }
            transaction.show(fragment);
            currentFragment = fragment;
            return transaction;
        }

    /**
     * 将环境的哈希表转化为String
     */
    private String setEnvironmentPah()
    {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<Integer, String> entry: fragmentEnvironmentPhoto.pathMap.entrySet())
        {
            result.append(entry.getValue()).append(";");
        }
        return result.toString();
    }

    private String setWildFormPath()
    {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<Integer, String> entry: fragmentWildFormPhoto.pathMap.entrySet())
        {
            result.append(entry.getValue()).append(";");
        }
        return result.toString();
    }

    private String setLabPath()
    {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<Integer, String> entry: fragmentLabPhoto.pathMap.entrySet())
        {
            result.append(entry.getValue()).append(";");
        }
        return result.toString();
    }

    private String setCultivatePath()
    {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<Integer, String> entry: fragmentCultivatePhoto.pathMap.entrySet())
        {
            result.append(entry.getValue()).append(";");
        }
        return result.toString();
    }


}
