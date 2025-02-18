package com.dalhousie.Neighbourly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class NeighbourlyApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(NeighbourlyApplication.class, args);
        boolean mailSenderExists = context.containsBean("javaMailSender");
        System.out.println("JavaMailSender Bean exists: " + mailSenderExists);
    }
}
