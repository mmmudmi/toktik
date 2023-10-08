package com.scalable.toktik;

import com.scalable.toktik.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ToktikApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToktikApplication.class, args);
    }

}
