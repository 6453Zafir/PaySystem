package com.example.admin.payment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PayActivity extends AppCompatActivity {
    private PaymentDatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        databaseHelper=new PaymentDatabaseHelper(getApplicationContext(),"payment",1);
    }
    private int alipay(String account,String password,float fee)
    {
        Cursor cursor=databaseHelper.getReadableDatabase().rawQuery("select password,balance from alipay where account=?",new String[]{account});
        cursor.moveToNext();
        String ps=cursor.getString(0);
        float balance=cursor.getFloat(1);

        if(!ps.equals(password))
            return 0;
        else if(balance<fee)
            return 1;
        else return 2;

    }
}
