package com.example.uiwidgettest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListViewTest extends AppCompatActivity {

    private List<Fruit> fruits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_test);

        fruits = getData();
        FruitAdapter fruitAdapter = new FruitAdapter(ListViewTest.this, R.layout.fruit_item, fruits);
        ListView listView = findViewById(R.id.testListView);
        listView.setAdapter(fruitAdapter);
        listView.setOnItemClickListener(new showInformation());

    }

    public List<Fruit> getData()
    {
        List<Fruit> fruitList = new ArrayList<>();
        fruitList.add(new Fruit("apple", 3));
        fruitList.add(new Fruit("watermelon", 4));
        fruitList.add(new Fruit("banana", 5));
        return fruitList;
    }

    class showInformation implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Fruit fruit = fruits.get(position);
            Toast.makeText(ListViewTest.this, fruit.getName()+" "+fruit.getPrice(), Toast.LENGTH_SHORT).show();
        }
    }

}
