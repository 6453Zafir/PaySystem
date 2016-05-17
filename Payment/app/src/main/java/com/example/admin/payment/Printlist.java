package com.example.admin.payment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Date;

public class Printlist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printlist);
        Bundle extras = getIntent().getExtras();
        final String phoneNum = extras.getString("PhoneNum");
        final String payway = extras.getString("PayWay");
        final String fee = Float.toString(extras.getFloat("fee")) ;
        final String date = extras.getString("Paytime");

        TextView PhoneNum = (TextView)findViewById(R.id.PHONENUM);
        PhoneNum.setText(phoneNum);
        TextView PayWay = (TextView)findViewById(R.id.PAYWAY);
        if(payway.equals("alipay")){
            PayWay.setText("支付宝");
        }else if(payway.equals("bank")){
            PayWay.setText("网上银行");
        }
        TextView Fee = (TextView)findViewById(R.id.TOTAL);
        Fee.setText(fee);
        TextView Paytime = (TextView)findViewById(R.id.PAYTIME);
        Paytime.setText(date);
    }
}
