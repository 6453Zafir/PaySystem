package com.example.admin.payment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
   private PaymentDatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper=new PaymentDatabaseHelper(getApplicationContext(),"payment",1);
      //  queryRecord();
    }

    private void queryRecord(String phonenumber)
    {
        Cursor cursor=databaseHelper.getReadableDatabase().rawQuery("select * from payrecord",null);
        while (cursor.moveToNext())
        {
            System.out.println(cursor.getInt(1));
            System.out.println(cursor.getString(2));
        }
    }
}
