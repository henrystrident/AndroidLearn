package com.example.savingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DataBaseActivity extends AppCompatActivity{

    private Button createTable, insertData, retrieveData, upgradeData, deleteData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
        final MyDatabaseHelper databaseHelper = new MyDatabaseHelper(DataBaseActivity.this, "BookStore.db",null,2);

        createTable = findViewById(R.id.createSQLiteTable);
        createTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.getWritableDatabase();
            }
        });

        insertData = findViewById(R.id.insertData);
        insertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                database.insert("book", null, values);
                values.clear();

                values.put("name", "THe lost Symbol");
                values.put("author", "Dan Brown");
                values.put("pages", 510);
                values.put("price", 19.95);
                database.insert("book", null, values);

                Toast.makeText(DataBaseActivity.this, "插入数据成功", Toast.LENGTH_SHORT).show();
            }
        });

        retrieveData = findViewById(R.id.retrieveData);
        retrieveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                Cursor cursor = database.query("book", null, null, null, null, null, null);
                if (cursor.moveToFirst())
                {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("查询数据", name+" "+author+" "+price+" "+pages);
                    }while (cursor.moveToNext());
                    cursor.close();
                }
            }
        });

        upgradeData = findViewById(R.id.upgradeData);
        upgradeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = databaseHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("price", 20.0);

                database.update("book", values, "name=?",new String[] {"The Da Vinci Code"});
                Toast.makeText(DataBaseActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
            }
        });

        deleteData = findViewById(R.id.deleteData);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                database.delete("book", "pages>?", new String[]{"520"});
                Toast.makeText(DataBaseActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
