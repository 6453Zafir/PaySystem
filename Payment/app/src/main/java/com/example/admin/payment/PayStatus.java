package com.example.admin.payment;

/**
 * Created by 马二爷 on 2016/5/16.
 */
public enum PayStatus {
    WRONGPASSWORD,//密码错误
    ACCOUNTNOTEXIST,//账号不存在
    NOTENOUGHBALANCE,//余额不足
    SUCCESS //支付成功
}
