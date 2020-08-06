package com.example.savingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.List;

public class LitePalTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lite_pal_test);

        Button createTable = findViewById(R.id.createLitePalTable);
        createTable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LitePal.getDatabase();
                Toast.makeText(LitePalTest.this, "数据库创建成功", Toast.LENGTH_SHORT).show();
            }
        });

        Button insertData = findViewById(R.id.insertLitePalData);
        insertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setName("The Da Vinci Code");
                book.setAuthor("Dan Brown");
                book.setPages(454);
                book.setPrice(19.56);
                book.save();
                Toast.makeText(LitePalTest.this, "数据插入成功", Toast.LENGTH_SHORT).show();
            }
        });

        Button selectData = findViewById(R.id.selectLitePalData);
        selectData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> bookList = LitePal.findAll(Book.class);
                for (Book book: bookList)
                {
                    Log.d("查询数据",book.getName()+" "+book.getAuthor()+" "+book.getId());
                }

                List<Book> conditionalBookList = LitePal.where("name=? and price<?", "The Da Vinci Code", "200")
                        .find(Book.class);
                for (Book book: conditionalBookList)
                {
                    Log.d("条件查询数据",book.getName()+" "+book.getAuthor()+" "+book.getPrice());
                }
            }
        });

        Button updateData = findViewById(R.id.updateLitePalData);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setPrice(100.3);
                book.updateAll("author=?", "Dan Brown");
            }
        });

        Button deleteData = findViewById(R.id.deleteLitePalData);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LitePal.delete(Book.class, 2);
                LitePal.deleteAll(Book.class, "price>?", "100");
            }
        });
    }


}
