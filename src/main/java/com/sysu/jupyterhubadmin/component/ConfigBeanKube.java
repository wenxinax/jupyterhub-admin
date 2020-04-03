package com.sysu.jupyterhubadmin.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:/application.properties")
@ConfigurationProperties(prefix = "kube")
public class ConfigBeanKube {

    public String address;

    public String token;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
