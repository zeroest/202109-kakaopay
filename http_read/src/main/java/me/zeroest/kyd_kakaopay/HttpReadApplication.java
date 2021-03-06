package me.zeroest.kyd_kakaopay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class HttpReadApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpReadApplication.class, args);
    }

}
