package com.example.uiwidgettest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

public class TitleLayout extends ConstraintLayout
{

    private Button back;
    private Button edit;

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);

        initView();

        back.setOnClickListener(new Back());

        edit.setOnClickListener(new Edit());
    }

    public void initView()
    {
        back = findViewById(R.id.backButton);
        edit = findViewById(R.id.editButton);
    }

    class Back implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            ((Activity) getContext()).finish();
        }
    }

    class Edit implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), recyclerViewTest.class);
            getContext().startActivity(intent);
        }
    }
}
