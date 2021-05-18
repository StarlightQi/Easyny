package easyny.ludashen.com.easyny.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import easyny.ludashen.com.easyny.YuYing;
import easyny.ludashen.com.easyny.util.Tool;

public class DuanXin extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuffer str=new StringBuffer();
        Object[] pduses = (Object[]) intent.getExtras().get("pdus");// 得到全部的短信
        for (Object pdus : pduses) {
            byte[] pdusmessage = (byte[]) pdus;// 得到每一条短信
            SmsMessage sms = SmsMessage.createFromPdu(pdusmessage);// 创建短信bean
            String mobile = sms.getOriginatingAddress();// 得到发送方的电话号码
            String content = sms.getMessageBody();// 得到短信内容
            Date date = new Date(sms.getTimestampMillis());// 得到发送短信的详细时间
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.US);// 为时间设置格式
            String sendtime = format.format(date);
            str.append(mobile + content + sendtime);
        }
        if(Tool.fileExists(context,"duan")) {
            Intent intentNotifi = new Intent(context, YuYing.class);
            intentNotifi.putExtra("duanxi", str.toString());
            intentNotifi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intentNotifi);
        }
    }
}