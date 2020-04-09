package com.sysu.jupyterhubadmin.kubesphere;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sysu.jupyterhubadmin.component.ConfigBeanKsapi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class KsApiRequest {
    @Autowired
    ConfigBeanKsapi configBeanKsapi;
    @Autowired
    private RestTemplate restTemplate;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(KsApiRequest.class);

    private HttpEntity<String> entity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + configBeanKsapi.token);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        return entity;
    }

    private void setToken() {
        String url = configBeanKsapi.address + "/kapis/iam.kubesphere.io/v1alpha2/login";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", configBeanKsapi.username);
        jsonObject.put("password", configBeanKsapi.password);
        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);

        ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        JSONObject json = JSON.parseObject(results.getBody());
        String token = json.getString("access_token");
        configBeanKsapi.setToken(token);
    }

    public JSON getNodes() {
        String url = configBeanKsapi.address + "/kapis/monitoring.kubesphere.io/v1alpha2/nodes";

        try {
            ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, entity(), String.class);
            JSONObject json = JSON.parseObject(results.getBody());
//            log.info(">>"+json.toJSONString());
            return json;
        }
        catch (HttpClientErrorException e) {
//            System.out.println(e.getStatusCode());
            if (e.getStatusCode().toString().equals("401 UNAUTHORIZED")) {
//                System.out.println("hh");
                this.setToken();
                return this.getNodes();
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", -1);
            jsonObject.put("msg", e.getMessage());
            return jsonObject;
        }
    }

    public JSON getCluster() {
        String url = configBeanKsapi.address + "/kapis/monitoring.kubesphere.io/v1alpha2/cluster";

        try {
            ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, entity(), String.class);
            JSONObject json = JSON.parseObject(results.getBody());
//            log.info(json.toJSONString());
            return json;
        }
        catch (HttpClientErrorException e) {
            if (e.getStatusCode().toString().equals("401 UNAUTHORIZED")) {
                this.setToken();
                return this.getCluster();
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", -1);
            jsonObject.put("msg", e.getMessage());
            return jsonObject;
        }
    }
}
