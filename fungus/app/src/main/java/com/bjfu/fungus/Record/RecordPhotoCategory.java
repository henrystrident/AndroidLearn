package com.bjfu.fungus.Record;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bjfu.fungus.Collect.FragmentCultivatePhoto;
import com.bjfu.fungus.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RecordPhotoCategory extends AppCompatActivity {

    private String ip, port, collectNumber, username;

    private Fragment currentFragment;
    private FragmentRecordEnvironmentPhoto fragmentRecordEnvironmentPhoto;
    private FragmentRecordWildFormPhoto fragmentRecordWildFormPhoto;
    private FragmentRecordLabPhoto fragmentRecordLabPhoto;
    private FragmentRecordCultivatePhoto fragmentRecordCultivatePhoto;

    private ConstraintLayout photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo_category);

        ip = getIntent().getStringExtra("ip");
        port = getIntent().getStringExtra("port");
        username = getIntent().getStringExtra("username");
        collectNumber = getIntent().getStringExtra("collectNumber");

        initToolbar();

        initFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.photoBottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(showFragment);


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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return true;
        }
    }

    private void initFragment()
    {
        Bundle args = new Bundle();
        args.putString("ip", ip);
        args.putString("port", port);
        args.putString("username", username);
        args.putString("collectNumber", collectNumber);
        fragmentRecordEnvironmentPhoto = new FragmentRecordEnvironmentPhoto();
        fragmentRecordEnvironmentPhoto.setArguments(args);

        fragmentRecordWildFormPhoto = new FragmentRecordWildFormPhoto();
        fragmentRecordWildFormPhoto.setArguments(args);

        fragmentRecordLabPhoto = new FragmentRecordLabPhoto();
        fragmentRecordLabPhoto.setArguments(args);

        fragmentRecordCultivatePhoto = new FragmentRecordCultivatePhoto();
        fragmentRecordCultivatePhoto.setArguments(args);



        photoView = findViewById(R.id.photoView);

        // 显示第一个类别
        getSupportFragmentManager().beginTransaction().replace(R.id.photoView, fragmentRecordEnvironmentPhoto).commit();
        currentFragment = fragmentRecordEnvironmentPhoto;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener showFragment = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {

                case R.id.categoryGrowEnvironment:
                    switchFragment(fragmentRecordEnvironmentPhoto).commit();
                    return true;
                case R.id.categoryWildForm:
                    switchFragment(fragmentRecordWildFormPhoto).commit();
                    return true;
                case R.id.categoryLab:
                    switchFragment(fragmentRecordLabPhoto).commit();
                    return true;
                case R.id.categoryCultivate:
                    switchFragment(fragmentRecordCultivatePhoto).commit();
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

}
