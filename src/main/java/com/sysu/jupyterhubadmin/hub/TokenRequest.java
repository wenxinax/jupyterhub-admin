package com.sysu.jupyterhubadmin.hub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sysu.jupyterhubadmin.component.ConfigBeanHub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenRequest {
    @Autowired
    ConfigBeanHub configBeanHub;

    private String HUB_API_URL = "/hub/api";
    private String HUB_API_TOKEN_URL = HUB_API_URL + "/authorizations/token";
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TokenRequest.class);

    @Autowired
    private RestTemplate restTemplate;

    public JSON VerifyToken(String token) {
        String url = configBeanHub.address + HUB_API_TOKEN_URL + "/" + token;
        HttpHeaders headers = new HttpHeaders();
        System.out.println(url);

        headers.add("Authorization", "token "+configBeanHub.token);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JSONObject json = JSON.parseObject(results.getBody());
            log.info("[VerifyToken] verify user token success, user info:" + json);
            return json;
        }
        catch (HttpClientErrorException e) {
            JSONObject json = JSON.parseObject(e.getResponseBodyAsString());
            log.error("[VerifyToken] verify user token failed, data:" + json);

            return json;
        }
    }
}
