package com.example.admin.payment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChoosePayway extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payway);
    }

    public void ToaliPay(View view){
        Intent intent = new Intent(getBaseContext(),PayActivity.class);
        intent.putExtra("PayWay","alipay");
        startActivity(intent);
    }
    public void ToBank(View view){
        Intent intent = new Intent(getBaseContext(),PayActivity.class);
        intent.putExtra("PayWay","bank");
        startActivity(intent);
    }
}
