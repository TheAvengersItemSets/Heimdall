package org.heimdall.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 */
@SpringBootApplication
@ComponentScan(basePackages={"org.heimdall"})
public class HeimdallDubboWebAppliaction {
    public static void main(String[] args) {
        SpringApplication.run(HeimdallDubboWebAppliaction.class, args);
    }
}
