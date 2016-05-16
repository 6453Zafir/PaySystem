package com.example.admin.payment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public Button inputPhoneNumBtn;
    public EditText inputPhomeNumtext;

    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private Context mContext;
    private View billlist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        inputPhoneNumBtn = (Button)findViewById(R.id.submitPhoneNum);
        inputPhomeNumtext =(EditText)findViewById(R.id.phoneNum);

        inputPhoneNumBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        boolean isphoneNumExist = isphoneNumExist(inputPhomeNumtext.getText().toString());
                        if(isphoneNumExist){
                            builder = new AlertDialog.Builder(mContext);
                            final LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                            billlist = inflater.inflate(R.layout.billlist, null,false);
                            builder.setView(billlist);
                            builder.setCancelable(false)
                                    .setTitle("账单")
                                    .setIcon(R.drawable.bill)
                                    .setPositiveButton("现在支付", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){
                                        //支付方式选择
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
        return true;
    }
}
