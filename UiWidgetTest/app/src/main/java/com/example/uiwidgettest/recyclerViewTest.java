package com.example.uiwidgettest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class recyclerViewTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_test);

        RecyclerView recyclerView = findViewById(R.id.fruitRecyclerVIew);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        FruitRecyclerViewAdapter fruitRecyclerViewAdapter = new FruitRecyclerViewAdapter(getData());
        recyclerView.setAdapter(fruitRecyclerViewAdapter);

    }

    public List<Fruit> getData()
    {
        List<Fruit> fruitList = new ArrayList<>();
        fruitList.add(new Fruit("apple", 3));
        fruitList.add(new Fruit("watermelon", 4));
        fruitList.add(new Fruit("banana", 5));
        fruitList.add(new Fruit("aaaaaa", 6));
        fruitList.add(new Fruit("bbbbbb", 7));
        fruitList.add(new Fruit("cccccc", 8));
        return fruitList;
    }
}
