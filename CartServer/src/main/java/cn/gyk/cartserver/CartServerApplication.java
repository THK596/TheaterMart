package cn.gyk.cartserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "cn.gyk.tmapi.client")
public class CartServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartServerApplication.class, args);
    }

}
