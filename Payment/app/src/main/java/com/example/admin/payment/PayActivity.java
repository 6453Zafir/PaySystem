package com.example.admin.payment;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class PayActivity extends AppCompatActivity {

    private String account;
    private String password;
    private PaymentDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        databaseHelper=new PaymentDatabaseHelper(getApplicationContext(),"payment",1);
        System.out.println(alipay("1","1223",800));

        TextView accountlabel = (TextView)findViewById(R.id.accountLabel);
        TextView passwordlabel = (TextView)findViewById(R.id.passwordLabel);

        EditText accountEdit = (EditText)findViewById(R.id.account);
        EditText passwordEdit = (EditText)findViewById(R.id.password);

        account = accountEdit.getText().toString();
        password =passwordEdit.getText().toString();

        Bundle extras = getIntent().getExtras();
        String payway = extras.getString("PayWay");
        Float fee = extras.getFloat("fee");
        switch (payway){
            case "alipay":
                accountlabel.setText("支付宝账号:");
                passwordlabel.setText("支付宝支付密码:");
                alipay(account,password,fee);
                Log.v("ao","hhh");
                break;
            case "bank":
                accountlabel.setText("银行卡号:");
                passwordlabel.setText("银行卡支付密码:");
                Log.v("ao","NoNoNo");
                break;
            default:break;
        }
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
}
