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
    final String SQL_CREATE_BANK_TABLE="create table bank (account varchar(40) primary key," +
                                                                  "password varchar(40)," +
                                                                  "balance REAL)";
    public PaymentDatabaseHelper(Context context,String name,int version)
    {
        super(context,name,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        System.out.println("create!!");
        Object [] initial={"15221399767",2004,1,150,true};
        Object []ini2={"17717090597",2004,2,300,true};
        database.execSQL(SQL_CREATE_TABLE);
        database.execSQL(SQL_CREATE_ALIPAY_TABLE);
        database.execSQL(SQL_CREATE_BANK_TABLE);
        database.execSQL("insert into payrecord values(null,?,?,?,?,?)",initial);
        database.execSQL("insert into payrecord values(null,?,?,?,?,?)",ini2);
        database.execSQL("insert into alipay values(1,123,1000)");
        database.execSQL("insert into bank values(1,123,1000)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldversion, int newversion)
    {

    }
    @Override
    public void onOpen(SQLiteDatabase database)
    {
        super.onOpen(database);
    }


}
