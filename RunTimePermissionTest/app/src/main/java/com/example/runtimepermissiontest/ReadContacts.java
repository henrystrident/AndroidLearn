package com.example.runtimepermissiontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadContacts extends AppCompatActivity {

    List<String> contacts = new ArrayList<>();

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_contacts);

        ListView listView = findViewById(R.id.contactsList);
        adapter = new ArrayAdapter<String>(ReadContacts.this, android.R.layout.simple_list_item_1, contacts);
        listView.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(ReadContacts.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ReadContacts.this, new String[] {Manifest.permission.READ_CONTACTS}, 2);
        }
        else
        {
            readContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 2:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    readContacts();
                }
                else
                {
                    Toast.makeText(this, "申请访问联系人权限被拒绝了", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public void readContacts()
    {
        Cursor cursor = null;
        try
        {
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null)
            {
                while (cursor.moveToNext())
                {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contacts.add(name+" "+number);
                }
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }
}
