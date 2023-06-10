package com.yzj.yzjclientsdk;

import com.yzj.yzjclientsdk.client.YuApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//作用是可以读取，application.properties的配置，读取的配置可以设置到属性中
@ConfigurationProperties("yzj.client")
@Data
@ComponentScan
public class YuApiClientConfig {

    private String accessKey;
    private String secretKey;

    @Bean
    public YuApiClient yuApiClient(){
        return new YuApiClient(accessKey,secretKey);
    }
}
