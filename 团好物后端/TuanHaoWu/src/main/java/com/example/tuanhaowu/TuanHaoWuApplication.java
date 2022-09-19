package com.example.tuanhaowu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class TuanHaoWuApplication {

    public static void main(String[] args) {
        SpringApplication.run(TuanHaoWuApplication.class, args);
    }

}
