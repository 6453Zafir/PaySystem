package com.example.admin.payment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public Button inputPhoneNumBtn;
    public EditText inputPhomeNumtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    return true;
    }
}
