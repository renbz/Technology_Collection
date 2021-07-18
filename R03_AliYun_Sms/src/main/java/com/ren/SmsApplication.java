package com.ren;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.AddSmsSignRequest;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmsApplication {


    /**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public static void main(String[] args_) throws Exception {

        // 连接阿里云
        java.util.List<String> args = java.util.Arrays.asList(args_);
        Client client = SmsApplication.createClient("", "");
        // 创建对应 API 的 Request 。 类的命名规则为 API 方法名加上 Request
        AddSmsSignRequest addSmsSignRequest = new AddSmsSignRequest()
                .setResourceOwnerAccount("17777784883")
                .setResourceOwnerId(1L)
                .setSignName("AlphaACM博客系统")
                .setRemark("123");
        // 复制代码运行请自行打印 API 的返回值
        client.addSmsSign(addSmsSignRequest);
    }
}
