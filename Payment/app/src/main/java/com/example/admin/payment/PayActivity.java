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
    private PayStatus alipay(String account,String password,float fee)
    {
        String ps;
        float balance;
        Cursor cursor;

         cursor=databaseHelper.getReadableDatabase().rawQuery("select password,balance from alipay where account=?",new String[]{account});
        if(cursor!=null&&cursor.getCount()==0)
            return PayStatus.ACCOUNTNOTEXIST;
        else
        {
            cursor.moveToNext();
            ps=cursor.getString(0);
            balance=cursor.getFloat(1);
            if(!password.equals(ps))
                return PayStatus.WRONGPASSWORD;
            if(balance<fee)
                return PayStatus.NOTENOUGHBALANCE;

            databaseHelper.getReadableDatabase().execSQL("update alipay set balance = ? where account=?",new Object[]{balance-fee,account});
            return PayStatus.SUCCESS;
        }
    }
    private PayStatus bankpay(String account,String password,float fee)
    {
        String ps;
        float balance;
        Cursor cursor;
        cursor=databaseHelper.getReadableDatabase().rawQuery("select password,balance from bank where account=?",new String[]{account});
        if(cursor!=null&&cursor.getCount()==0)
            return PayStatus.ACCOUNTNOTEXIST;
        else
        {
            cursor.moveToNext();
            ps=cursor.getString(0);
            balance=cursor.getFloat(1);
            if(!password.equals(ps))
                return PayStatus.WRONGPASSWORD;
            if(balance<fee)
                return PayStatus.NOTENOUGHBALANCE;

            databaseHelper.getReadableDatabase().execSQL("update bank set balance = ? where account=?",new Object[]{balance-fee,account});
            return PayStatus.SUCCESS;
        }
    }
    public float queryAliPayBalance(String account)
    {
        Cursor cursor=databaseHelper.getReadableDatabase().rawQuery("select balance from alipay where account=?",new String[]{account});
        if(cursor!=null&&cursor.getCount()!=0) {
            cursor.moveToNext();
            return cursor.getFloat(0);
        }

        return -1;
    }
    public float queryBankBalance(String account)
    {
        Cursor cursor=databaseHelper.getReadableDatabase().rawQuery("select balance from bank where account=?",new String[]{account});
        if(cursor!=null&&cursor.getCount()!=0) {
            cursor.moveToNext();
            return cursor.getFloat(0);
        }
        return -1;
    }
}
