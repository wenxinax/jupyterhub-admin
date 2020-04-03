package com.sysu.jupyterhubadmin.kubernetes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sysu.jupyterhubadmin.component.ConfigBeanKube;
import com.sysu.jupyterhubadmin.config.service.RestTemplateConfig;
import com.sysu.jupyterhubadmin.hub.TokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Service
public class KubeApiRequest {
    @Autowired
    private ConfigBeanKube configBeanKube;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TokenRequest.class);
    private RestTemplate restTemplateHttps;

    private HttpEntity<String> entity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + configBeanKube.token);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        return entity;
    }

    public KubeApiRequest() {
        try {
            restTemplateHttps = new RestTemplate(RestTemplateConfig.generateHttpRequestFactory());
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public JSON getNodes() {
        String url = configBeanKube.address + "/api/v1/nodes";
        try {
            ResponseEntity<String> results = restTemplateHttps.exchange(url, HttpMethod.GET, entity(), String.class);
            return JSON.parseObject(results.getBody());
        }
        catch (HttpClientErrorException e) {
            return JSON.parseObject(e.getResponseBodyAsString());
        }
    }
}
