package com.sysu.jupyterhubadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JupyterhubAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(JupyterhubAdminApplication.class, args);
    }

}
