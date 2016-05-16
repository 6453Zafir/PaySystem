package com.example.admin.payment;

import android.database.Cursor;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public Button inputPhoneNumBtn;
    public EditText inputPhomeNumtext;
    private PaymentDatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper=new PaymentDatabaseHelper(getApplicationContext(),"payment",1);
        inputPhoneNumBtn = (Button)findViewById(R.id.submitPhoneNum);
        inputPhomeNumtext =(EditText)findViewById(R.id.phoneNum);

        inputPhoneNumBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                       isphoneNumExist(inputPhomeNumtext.getText().toString());
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
        int thisMonthMinutes;
        float moneyToPay=0;
         cursor=databaseHelper.getReadableDatabase().rawQuery("select count* from payrecord" +
                " where phonenumber=? and year=? and hasPaid=false",new String[]{phoneNumber,Integer.toString(year)});

        cursor.moveToNext();
        notPaidNumThisYear=cursor.getInt(0);

        cursor=databaseHelper.getReadableDatabase().rawQuery("select minute from payrecord " +
                "where phonenumber=? and year<? and hasPaid=false",new String[]{phoneNumber,Integer.toString(year)});
        while(cursor.moveToNext())
        {
            owingMoneyBeforeThisYear+=(cursor.getInt(0)*0.25+25);
        }
        cursor=databaseHelper.getReadableDatabase().rawQuery("select minute from payrecord" +
                "where phonenumber=?and year=?and month=?",new String[]{phoneNumber,Integer.toString(year),Integer.toString(month)});
        cursor.moveToNext();
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
