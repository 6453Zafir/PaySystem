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
    private Context mContext;
    private View billlist;
    private PaymentDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper=new PaymentDatabaseHelper(getApplicationContext(),"payment",1);

        mContext = MainActivity.this;

//        insertData();//只运行一次

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
        Log.v("year:",Integer.toString(year));
        Log.v("month:",Integer.toString(month));
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
            owingMoneyBeforeThisYear+=(cursor.getInt(0)*0.15+25);
        }
        cursor=database.query("payrecord",new String[]{"minute"},"phonenumber=? and year=? and month=?",new String [] {phoneNumber,Integer.toString(year),Integer.toString(month)},null,null,null);
        /*cursor=databaseHelper.getReadableDatabase().rawQuery("select minute from payrecord" +
                "where phonenumber=?and year=?and month=?",new String[]{phoneNumber,Integer.toString(year),Integer.toString(month)});*/

        if(cursor.moveToNext())
        thisMonthMinutes=cursor.getInt(0);

         if(thisMonthMinutes<=60&&thisMonthMinutes>0)
        {
            if(notPaidNumThisYear>1)
                moneyToPay+=(thisMonthMinutes*0.15+25);
            else moneyToPay+=(thisMonthMinutes*0.15+25)*0.099;
        }
        else if(thisMonthMinutes<=120&&thisMonthMinutes>60)
        {
            if(notPaidNumThisYear>2)
                moneyToPay+=(thisMonthMinutes*0.15+25);
            else moneyToPay+=(thisMonthMinutes*0.15+25)*0.985;
        }
        else if(thisMonthMinutes<=180&&thisMonthMinutes>120)
        {
            if(notPaidNumThisYear>3)
                moneyToPay+=(thisMonthMinutes*0.15+25);
            else moneyToPay+=(thisMonthMinutes*0.15+25)*0.98;
        }
        else if(thisMonthMinutes<=300&&thisMonthMinutes>180)
        {
            if(notPaidNumThisYear>3)
                moneyToPay+=(thisMonthMinutes*0.15+25);
            else moneyToPay+=(thisMonthMinutes*0.15+25)*0.975;
        }
        else if(thisMonthMinutes>300)
        {
            if(notPaidNumThisYear>6)
                moneyToPay+=(thisMonthMinutes*0.15+25);
            else moneyToPay+=(thisMonthMinutes*0.15+25)*0.97;
        }
        moneyToPay+=owingMoneyBeforeThisYear*0.05;

        bill=new PhoneBill(phoneNumber,notPaidNumThisYear,owingMoneyBeforeThisYear,thisMonthMinutes,moneyToPay);
        return bill;
    }
    public void insertPayrecord(String phonenumber,int year,int month , long minute,boolean haspaid)
    {
        databaseHelper.getWritableDatabase().execSQL("insert into payrecord values(null,?,?,?,?,?)",new Object[]{phonenumber,year,month,minute,haspaid});
    }
    public void insertAlipay(String account,String password,float balance)
    {
        databaseHelper.getWritableDatabase().execSQL("insert into alipay values(?,?,?)",new Object[]{account,password,balance});
    }
    public void insertBank(String account,String password,float balance)
    {
        databaseHelper.getWritableDatabase().execSQL("insert into bank values(?,?,?)",new Object[]{account,password,balance});
    }
    public void insertData()
    {
       insertAlipay("little","123",3);
       insertBank("little","123",3);

       insertAlipay("many","123",10000);
       insertBank("many","123",10000);

        insertPayrecord("111",2016,2,24,true);
        insertPayrecord("111",2016,5,30,true);
        insertPayrecord("111",2015,5,300,false);

        insertPayrecord("112",2016,4,234,false);
        insertPayrecord("112",2016,5,30,true);


        insertPayrecord("113",2015,5,300,false);
        insertPayrecord("113",2016,4,30,false);
        insertPayrecord("113",2016,3,30,false);
        insertPayrecord("113",2016,5,30,true);

        insertPayrecord("114",2016,4,30,false);
        insertPayrecord("114",2016,3,30,false);
        insertPayrecord("114",2016,2,30,false);
        insertPayrecord("114",2016,5,30,true);

        insertPayrecord("115",2015,5,300,false);
        insertPayrecord("115",2016,1,30,false);
        insertPayrecord("115",2016,4,30,false);
        insertPayrecord("115",2016,3,30,false);
        insertPayrecord("115",2016,2,30,false);
        insertPayrecord("115",2016,5,30,true);

        insertPayrecord("116",2016,1,30,false);
        insertPayrecord("116",2016,2,30,false);
        insertPayrecord("116",2016,3,30,false);
        insertPayrecord("116",2016,4,30,false);
        insertPayrecord("116",2016,8,30,false);
        insertPayrecord("116",2016,6,30,false);
        insertPayrecord("116",2016,7,30,false);
        insertPayrecord("116",2016,5,30,true);

        insertPayrecord("117",2014,5,300,false);
        insertPayrecord("117",2016,5,80,true);

        insertPayrecord("118",2016,4,80,false);
        insertPayrecord("118",2016,5,80,true);

        insertPayrecord("119",2014,5,300,false);
        insertPayrecord("119",2016,3,80,false);
        insertPayrecord("119",2016,4,80,false);
        insertPayrecord("119",2016,5,80,true);


        insertPayrecord("120",2016,2,80,false);
        insertPayrecord("120",2016,3,80,false);
        insertPayrecord("120",2016,4,80,false);
        insertPayrecord("120",2016,5,80,true);

        insertPayrecord("121",2014,5,300,false);
        insertPayrecord("121",2016,1,80,false);
        insertPayrecord("121",2016,2,80,false);
        insertPayrecord("121",2016,3,80,false);
        insertPayrecord("121",2016,4,80,false);
        insertPayrecord("121",2016,5,80,true);

        insertPayrecord("122",2016,1,30,false);
        insertPayrecord("122",2016,2,30,false);
        insertPayrecord("122",2016,3,30,false);
        insertPayrecord("122",2016,4,30,false);
        insertPayrecord("122",2016,8,30,false);
        insertPayrecord("122",2016,6,30,false);
        insertPayrecord("122",2016,7,30,false);
        insertPayrecord("122",2016,5,80,true);


        insertPayrecord("123",2014,5,300,false);
        insertPayrecord("123",2016,5,150,true);

        insertPayrecord("124",2016,4,150,false);
        insertPayrecord("124",2016,5,150,true);

        insertPayrecord("125",2014,5,300,false);
        insertPayrecord("125",2016,3,150,false);
        insertPayrecord("125",2016,4,150,false);
        insertPayrecord("125",2016,5,150,true);

        insertPayrecord("126",2016,2,150,false);
        insertPayrecord("126",2016,3,150,false);
        insertPayrecord("126",2016,4,150,false);
        insertPayrecord("126",2016,5,150,true);

        insertPayrecord("127",2014,5,300,false);
        insertPayrecord("127",2016,1,150,false);
        insertPayrecord("127",2016,2,150,false);
        insertPayrecord("127",2016,3,150,false);
        insertPayrecord("127",2016,4,150,false);
        insertPayrecord("127",2016,5,150,true);

        insertPayrecord("128",2016,1,150,false);
        insertPayrecord("128",2016,2,150,false);
        insertPayrecord("128",2016,3,150,false);
        insertPayrecord("128",2016,4,150,false);
        insertPayrecord("128",2016,6,150,false);
        insertPayrecord("128",2016,7,150,false);
        insertPayrecord("128",2016,8,150,false);
        insertPayrecord("128",2016,5,150,true);

        insertPayrecord("129",2014,5,300,false);
        insertPayrecord("129",2016,5,230,true);

        insertPayrecord("130",2016,4,150,false);
        insertPayrecord("130",2016,5,230,true);

        insertPayrecord("131",2014,5,300,false);
        insertPayrecord("131",2016,3,150,false);
        insertPayrecord("131",2016,4,150,false);
        insertPayrecord("131",2016,5,230,true);

        insertPayrecord("132",2016,2,150,false);
        insertPayrecord("132",2016,3,150,false);
        insertPayrecord("132",2016,4,150,false);
        insertPayrecord("132",2016,5,230,true);

        insertPayrecord("133",2014,5,300,false);
        insertPayrecord("133",2016,1,150,false);
        insertPayrecord("133",2016,2,150,false);
        insertPayrecord("133",2016,3,150,false);
        insertPayrecord("133",2016,4,150,false);
        insertPayrecord("133",2016,5,230,true);

        insertPayrecord("134",2016,1,150,false);
        insertPayrecord("134",2016,2,150,false);
        insertPayrecord("134",2016,3,150,false);
        insertPayrecord("134",2016,4,150,false);
        insertPayrecord("134",2016,6,150,false);
        insertPayrecord("134",2016,7,150,false);
        insertPayrecord("134",2016,8,150,false);
        insertPayrecord("134",2016,5,230,true);

        insertPayrecord("135",2014,5,300,false);
        insertPayrecord("135",2016,5,500,true);

        insertPayrecord("136",2016,4,150,false);
        insertPayrecord("136",2016,5,500,true);

        insertPayrecord("137",2014,5,300,false);
        insertPayrecord("137",2016,3,150,false);
        insertPayrecord("137",2016,4,150,false);
        insertPayrecord("137",2016,5,500,true);

        insertPayrecord("138",2016,2,150,false);
        insertPayrecord("138",2016,3,150,false);
        insertPayrecord("138",2016,4,150,false);
        insertPayrecord("138",2016,5,500,true);

        insertPayrecord("139",2015,8,300,false);
        insertPayrecord("139",2016,1,150,false);
        insertPayrecord("139",2016,2,150,false);
        insertPayrecord("139",2016,3,150,false);
        insertPayrecord("139",2016,4,150,false);
        insertPayrecord("139",2016,5,500,true);

        insertPayrecord("140",2016,1,150,false);
        insertPayrecord("140",2016,2,150,false);
        insertPayrecord("140",2016,3,150,false);
        insertPayrecord("140",2016,4,150,false);
        insertPayrecord("140",2016,6,150,false);
        insertPayrecord("140",2016,7,150,false);
        insertPayrecord("140",2016,8,150,false);
        insertPayrecord("140",2016,9,150,false);
        insertPayrecord("140",2016,10,150,false);
        insertPayrecord("140",2016,11,150,false);
        insertPayrecord("140",2016,12,150,false);
        insertPayrecord("140",2016,5,230,true);

        System.out.println("insert ok");

    }

}
