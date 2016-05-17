package com.example.admin.payment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PayActivity extends AppCompatActivity {

    private Float fee;
    private PaymentDatabaseHelper databaseHelper;
    private Context mContext;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private AlertDialog.Builder builder1 = null;
    private AlertDialog.Builder builder2= null;
    private AlertDialog.Builder builder3= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        mContext = PayActivity.this;

        databaseHelper=new PaymentDatabaseHelper(getApplicationContext(),"payment",1);

        System.out.println(alipay("1","1223",800));

        TextView accountlabel = (TextView)findViewById(R.id.accountLabel);
        TextView passwordlabel = (TextView)findViewById(R.id.passwordLabel);

        final EditText accountEdit = (EditText)findViewById(R.id.accountinput);
        final EditText passwordEdit = (EditText)findViewById(R.id.passwordinput);

        Bundle extras = getIntent().getExtras();
        final String phoneNum = extras.getString("phoneNum");
        final String payway = extras.getString("PayWay");
        fee = extras.getFloat("fee");

        Button submitBtn = (Button)findViewById(R.id.submit);

        switch (payway){
            case "alipay":
                accountlabel.setText("支付宝账号:");
                passwordlabel.setText("支付宝支付密码:");
                submitBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        final String account = accountEdit.getText().toString();
                        final String password = passwordEdit.getText().toString();
                        PayStatus result =  alipay(account,password,fee);
                        if(result == PayStatus.SUCCESS){
                            builder = new AlertDialog.Builder(mContext);
                            alert = builder.setIcon(R.drawable.error)
                                    .setTitle("支付成功!")
                                    .setIcon(R.drawable.success)
                                    .setMessage("恭喜!手机号 "+ phoneNum + " 已缴费成功！")
                                    .setPositiveButton("打印清单", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getBaseContext(),Printlist.class);
                                            intent.putExtra("fee",fee);
                                            intent.putExtra("PayWay",payway);
                                            intent.putExtra("PhoneNum",phoneNum);
                                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                            Date today = Calendar.getInstance().getTime();
                                            String reportDate = df.format(today);
                                            intent.putExtra("Paytime",reportDate);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){
                                        }
                                    }).create();
                            alert.show();
                        }else if(result == PayStatus.ACCOUNTNOTEXIST){
                            builder1 = new AlertDialog.Builder(mContext);
                            alert = builder1.setIcon(R.drawable.error)
                                    .setTitle("抱歉")
                                    .setIcon(R.drawable.error)
                                    .setMessage("支付宝账号不存在")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create();
                            alert.show();
                        }else if(result == PayStatus.WRONGPASSWORD){
                            builder2 = new AlertDialog.Builder(mContext);
                            alert = builder2.setIcon(R.drawable.error)
                                    .setTitle("抱歉")
                                    .setMessage("密码错误")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getBaseContext(),ChoosePayway.class);
                                            intent.putExtra("phoneNum",phoneNum);
                                            intent.putExtra("fee",fee);
                                            startActivity(intent);
                                        }
                                    }).create();
                                    alert.show();
                        }else if(result == PayStatus.NOTENOUGHBALANCE){
                            builder3 = new AlertDialog.Builder(mContext);
                            alert = builder3.setIcon(R.drawable.error)
                                    .setTitle("抱歉")
                                    .setMessage("该账户余额不足，余额为: "+queryAliPayBalance(account)+" 元")
                                    .setPositiveButton("更换付款方式", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getBaseContext(),ChoosePayway.class);
                                            intent.putExtra("phoneNum",phoneNum);
                                            intent.putExtra("fee",fee);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setNegativeButton("支付所剩余额", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getBaseContext(),ChoosePayway.class);
                                            intent.putExtra("phoneNum",phoneNum);
                                            intent.putExtra("fee",fee-queryAliPayBalance(account));
                                            startActivity(intent);
                                            alipay(account,password,queryAliPayBalance(account));
                                        }
                                    }).create();
                            alert.show();
                        }
                    }
                });
                break;
            case "bank":
                accountlabel.setText("银行卡号:");
                passwordlabel.setText("银行卡支付密码:");
                submitBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        final String account = accountEdit.getText().toString();
                        final String password = passwordEdit.getText().toString();
                        PayStatus result =  alipay(account,password,fee);
                        if(result == PayStatus.SUCCESS){
                            builder = new AlertDialog.Builder(mContext);
                            alert = builder.setIcon(R.drawable.error)
                                    .setTitle("支付成功!")
                                    .setIcon(R.drawable.success)
                                    .setMessage("恭喜!手机号 "+ phoneNum + " 已缴费成功！")
                                    .setPositiveButton("打印清单", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getBaseContext(),Printlist.class);
                                            intent.putExtra("fee",fee);
                                            intent.putExtra("PayWay",payway);
                                            intent.putExtra("PhoneNum",phoneNum);
                                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                            Date today = Calendar.getInstance().getTime();
                                            String reportDate = df.format(today);
                                            intent.putExtra("Paytime",reportDate);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){
                                        }
                                    }).create();
                            alert.show();
                        }else if(result == PayStatus.ACCOUNTNOTEXIST){
                            builder1 = new AlertDialog.Builder(mContext);
                            alert = builder1.setIcon(R.drawable.error)
                                    .setTitle("抱歉")
                                    .setIcon(R.drawable.error)
                                    .setMessage("银行卡账号不存在")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create();
                            alert.show();
                        }else if(result == PayStatus.WRONGPASSWORD){
                            builder2 = new AlertDialog.Builder(mContext);
                            alert = builder2.setIcon(R.drawable.error)
                                    .setTitle("抱歉")
                                    .setMessage("银行卡密码错误")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getBaseContext(),ChoosePayway.class);
                                            intent.putExtra("phoneNum",phoneNum);
                                            intent.putExtra("fee",fee);
                                            startActivity(intent);
                                        }
                                    }).create();
                            alert.show();
                        }else if(result == PayStatus.NOTENOUGHBALANCE){
                            builder3 = new AlertDialog.Builder(mContext);
                            alert = builder3.setIcon(R.drawable.error)
                                    .setTitle("抱歉")
                                    .setMessage("该账户余额不足，余额为: "+queryBankBalance(account)+" 元")
                                    .setPositiveButton("更换付款方式", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getBaseContext(),ChoosePayway.class);
                                            intent.putExtra("phoneNum",phoneNum);
                                            intent.putExtra("fee",fee);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setNegativeButton("支付所剩余额", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getBaseContext(),ChoosePayway.class);
                                            intent.putExtra("phoneNum",phoneNum);
                                            intent.putExtra("fee",fee-queryBankBalance(account));
                                            startActivity(intent);
                                            bankpay(account,password,queryBankBalance(account));
                                        }
                                    }).create();
                            alert.show();
                        }
                    }
                });
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
