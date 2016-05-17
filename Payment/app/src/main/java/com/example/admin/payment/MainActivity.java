package com.example.admin.payment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.FloatRange;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public Button inputPhoneNumBtn;
    public EditText inputPhomeNumtext;

    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private AlertDialog.Builder buildernew = null;
    private Context mContext;
    private View billlist;
    private PaymentDatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper=new PaymentDatabaseHelper(getApplicationContext(),"payment",1);
        mContext = MainActivity.this;

        inputPhoneNumBtn = (Button)findViewById(R.id.submitPhoneNum);
        inputPhomeNumtext =(EditText)findViewById(R.id.phoneNum);


        inputPhoneNumBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        final String phoneNum = inputPhomeNumtext.getText().toString();
                        boolean isphoneNumExist = isphoneNumExist(phoneNum);
                        if(isphoneNumExist){

                            final PhoneBill phoneBill= queryBill(inputPhomeNumtext.getText().toString());

                            builder = new AlertDialog.Builder(mContext);
                            final LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                            billlist = inflater.inflate(R.layout.billlist, null,false);

                            builder.setView(billlist);
                            TextView PhoneNumInBill = (TextView)billlist.findViewById(R.id.PhoneNuminBill);
                            PhoneNumInBill.setText(
                                    phoneBill.getPhonenumber());
                            TextView lastYear = (TextView)billlist.findViewById(R.id.LastYear);
                            lastYear.setText(
                                    Float.toString(phoneBill.getOwingMoneyBeforeThisYear()));
                            TextView TimeDelay = (TextView)billlist.findViewById(R.id.Timedelay);
                            TimeDelay.setText(
                                    Integer.toString(phoneBill.getNotPaidNumThisYear()));
                            TextView Calltime = (TextView)billlist.findViewById(R.id.phonecalltime);
                            Calltime.setText(
                                    Integer.toString(phoneBill.getThisMonthMinutes()));
                            TextView Total = (TextView)billlist.findViewById(R.id.MoneyTopay);
                            Total.setText(
                                    Float.toString(phoneBill.getMoneyToPay()));
                            builder.setCancelable(false)
                                    .setTitle("账单")
                                    .setIcon(R.drawable.bill)
                                    .setPositiveButton("现在支付", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){
                                        //支付方式选择页面
                                            Intent intent = new Intent(getBaseContext(),ChoosePayway.class);
                                            intent.putExtra("phoneNum",phoneNum);
                                            intent.putExtra("fee",phoneBill.getMoneyToPay());
                                            startActivity(intent);
                                    }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                }
                            });
                            alert = builder.create();
                            alert.show();
                        }else {
                            alert = null;
                            builder = new AlertDialog.Builder(mContext);
                            alert = builder.setIcon(R.drawable.error)
                                    .setTitle("抱歉：")
                                    .setMessage("查无此手机号，请检查您的输入是否有误")

                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create();
                            alert.show();
                        }
                    }
                }
        );
    }

    public boolean isphoneNumExist(String phoneNum){
        Cursor cursor;
        cursor=databaseHelper.getReadableDatabase().rawQuery("select id from payrecord where phonenumber=?",new String[]{phoneNum});
        return (cursor!=null&&cursor.getCount()>0);
    }

    public PhoneBill queryBill(String phoneNumber)
    {
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        Cursor cursor;
        PhoneBill bill;
        int notPaidNumThisYear;
        float owingMoneyBeforeThisYear=0;
        int thisMonthMinutes=0;
        float moneyToPay=0;
        SQLiteDatabase database= databaseHelper.getReadableDatabase();
        cursor=database.query("payrecord",new String[]{"id"},"phonenumber=? and year=? and hasPaid=0",new String[]{phoneNumber,Integer.toString(year)},null,null,null);
        /*cursor=databaseHelper.getReadableDatabase().rawQuery("select count* from payrecord" +
                " where phonenumber=? and year=? and hasPaid=false",new String[]{phoneNumber,Integer.toString(year)});
*/
        notPaidNumThisYear=cursor.getCount();

        cursor=databaseHelper.getReadableDatabase().rawQuery("select minute from payrecord " +
                "where phonenumber=? and year<? and hasPaid=0",new String[]{phoneNumber,Integer.toString(year)});
        while(cursor.moveToNext())
        {
            owingMoneyBeforeThisYear+=(cursor.getInt(0)*0.25+25);
        }
        cursor=database.query("payrecord",new String[]{"minute"},"phonenumber=? and year=? and month=?",new String [] {phoneNumber,Integer.toString(year),Integer.toString(month)},null,null,null);
        /*cursor=databaseHelper.getReadableDatabase().rawQuery("select minute from payrecord" +
                "where phonenumber=?and year=?and month=?",new String[]{phoneNumber,Integer.toString(year),Integer.toString(month)});*/

        if(cursor.moveToNext())
        thisMonthMinutes=cursor.getInt(0);

        if(thisMonthMinutes<=60&&thisMonthMinutes>0)
        {
            if(notPaidNumThisYear>1)
                moneyToPay+=(thisMonthMinutes*0.25+25);
            else moneyToPay+=(thisMonthMinutes*0.25+25)*0.099;
        }
        else if(thisMonthMinutes<=120&&thisMonthMinutes>60)
        {
            if(notPaidNumThisYear>2)
                moneyToPay+=(thisMonthMinutes*0.25+25);
            else moneyToPay+=(thisMonthMinutes*0.25+25)*0.985;
        }
        else if(thisMonthMinutes<=180&&thisMonthMinutes>120)
        {
            if(notPaidNumThisYear>3)
                moneyToPay+=(thisMonthMinutes*0.25+25);
            else moneyToPay+=(thisMonthMinutes*0.25+25)*0.98;
        }
        else if(thisMonthMinutes<=300&&thisMonthMinutes>180)
        {
            if(notPaidNumThisYear>3)
                moneyToPay+=(thisMonthMinutes*0.25+25);
            else moneyToPay+=(thisMonthMinutes*0.25+25)*0.975;
        }
        else if(thisMonthMinutes>300)
        {
            if(notPaidNumThisYear>6)
                moneyToPay+=(thisMonthMinutes*0.25+25);
            else moneyToPay+=(thisMonthMinutes*0.25+25)*0.97;
        }
        moneyToPay+=owingMoneyBeforeThisYear*0.05;

        bill=new PhoneBill(phoneNumber,notPaidNumThisYear,owingMoneyBeforeThisYear,thisMonthMinutes,moneyToPay);
        return bill;
    }

}
