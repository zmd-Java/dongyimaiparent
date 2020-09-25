package com.offcn.util;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Component
public class smsListener implements MessageListener {
    @Autowired
    private SmsUtil smsUtil;
    @Override
    public void onMessage(Message message) {
        if(message instanceof MapMessage){

            MapMessage map=(MapMessage)message;

            try {
                System.out.println("收到短信发送请求---》mobile:"+map.getString("mobile")+"  param:"+map.getString("param"));
                HttpResponse response = smsUtil.sendSms(map.getString("mobile"), map.getString("param"));

// 结果是 00000 则为正常
                System.out.println("data:"+response.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
