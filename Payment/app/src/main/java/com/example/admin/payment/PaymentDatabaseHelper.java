package com.example.admin.payment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 马二爷 on 2016/5/16.
 */
public class PaymentDatabaseHelper extends SQLiteOpenHelper {

    final String SQL_CREATE_TABLE="create table payrecord(id integer primary key autoincrement," +
                                                                 "phonenumber varchar(20)," +
                                                                 "year integer" +
                                                                 ",month integer," +
                                                                 "minute BIGINT," +
                                                                 "hasPaid BOOLEAN)";
    final String SQL_CREATE_ALIPAY_TABLE="create table alipay(account varchar(40) primary key," +
                                                                 "password varchar(40)," +
                                                                 " balance REAL)";
    public PaymentDatabaseHelper(Context context,String name,int version)
    {
        super(context,name,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        Object [] initial={111,2004,1,150,true};
        Object []ini2={111,2004,2,300,true};
        database.execSQL(SQL_CREATE_TABLE);
        database.execSQL("insert into payrecord values(null,?,?,?,?,?)",initial);
        database.execSQL("insert into payrecord values(null,?,?,?,?,?)",ini2);

    }
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldversion, int newversion)
    {
        System.out.println("call update");
    }
    @Override
    public void onOpen(SQLiteDatabase database)
    {
        super.onOpen(database);
    }
    public void insertData(SQLiteDatabase db,String phonenumber,int year,int month,int minutes,boolean haspaid)
    {
        db.execSQL("insert into payrecord values(null,?,?,?,?,?)",new Object[]{phonenumber,year,month,minutes,haspaid});
    }

}
