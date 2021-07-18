package com.ren;

/**
 * @author Ren
 */

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;

public class Sample {

    public static void main(String[] args) throws Exception {
        sendMsm("17777784883","123456");
    }

    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

    public static void sendMsm(String phoneNumber, String checkCode) throws Exception {
        Client client = Sample.createClient("", "");
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phoneNumber)
                .setSignName("AlphaACM博客系统")
                .setTemplateCode("SMS_219746838")
                .setTemplateParam("{\"code\":" + checkCode + "}");
        // 复制代码运行请自行打印 API 的返回值
        client.sendSms(sendSmsRequest);
    }
}
