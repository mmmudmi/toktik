package com.scalable.toktik;

import com.scalable.toktik.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ToktikApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToktikApplication.class, args);
    }

}
