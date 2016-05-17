package com.example.admin.payment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChoosePayway extends AppCompatActivity {

    public String phoneNum;
    public Float fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payway);
        Bundle extras = getIntent().getExtras();
        phoneNum = extras.getString("phoneNum");
        fee = extras.getFloat("fee");
    }

    public void ToaliPay(View view){
        Intent intent = new Intent(getBaseContext(),PayActivity.class);
        intent.putExtra("PayWay","alipay");
        intent.putExtra("phoneNum",phoneNum);
        intent.putExtra("fee",fee);
        startActivity(intent);
    }
    public void ToBank(View view){
        Intent intent = new Intent(getBaseContext(),PayActivity.class);
        intent.putExtra("PayWay","bank");
        intent.putExtra("phoneNum",phoneNum);
        intent.putExtra("fee",fee);
        startActivity(intent);
    }
}
