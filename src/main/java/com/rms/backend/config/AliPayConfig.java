package com.rms.backend.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @description: 支付宝配置文件
 * @author: huang
 * @date 2024年02月18日 10:07
 **/
@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AliPayConfig {
    //支付宝的appId
   private String appId;
    //应用私钥
   private String appPrivateKey;
    //应用公钥
   private String alipayPublicKey;
    //支付宝通知本地接口的完整地址
   private String notifyUrl;
}
