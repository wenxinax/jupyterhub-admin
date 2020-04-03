package com.sysu.jupyterhubadmin.hub;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sysu.jupyterhubadmin.component.ConfigBeanHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserRequest {
    @Autowired
    ConfigBeanHub configBeanHub;

    private String HUB_API_URL = "hub/api";
     private String HUB_API_URL_USERS = HUB_API_URL + "/users";
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserRequest.class);


    @Autowired
    private  RestTemplate restTemplate;

    private HttpEntity<String> entity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token "+ configBeanHub.token);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        return entity;
    }

    public JSON getUsers() {
        String url = configBeanHub.address + HUB_API_URL_USERS;

        try {
            ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, entity(), String.class);
            JSONArray json = JSON.parseArray(results.getBody());
            return json;
        }
        catch (HttpClientErrorException e) {
            JSONObject json = JSON.parseObject(e.getResponseBodyAsString());

            return json;
        }
    }

    public JSON deleteUser(String name) {
        String url = configBeanHub.address + HUB_API_URL_USERS + "/" + name;
        try {
            ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.DELETE, entity(), String.class);
            log.info("[DeleteUser] delete user success, info " + results.getBody());
            return JSON.parseObject(results.getBody());
        }
        catch (HttpClientErrorException e) {
            JSONObject json = null;
            json = JSON.parseObject(e.getResponseBodyAsString());
            log.warn("[DeleteUser] delete user error, info " + json);
            return json;

        }
    }

    public JSON editUser(String oldName, String newName, boolean admin) {
        String url = configBeanHub.address +  HUB_API_URL_USERS + "/" + oldName;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "token "+ configBeanHub.token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", newName);
            jsonObject.put("admin", admin);
            HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);
            ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);
            log.info("[EditUser] edit user success, info " + results.getBody());
            return JSON.parseObject(results.getBody());
        }
        catch (HttpClientErrorException e) {
            JSONObject json = null;
            json = JSON.parseObject(e.getResponseBodyAsString());
            log.warn("[EditUser] edit user error, info " + json);
            return json;
        }
    }

    public JSON addUser(String username) {
        String url = configBeanHub.address +  HUB_API_URL_USERS + "/" + username;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "token "+ configBeanHub.token);

            HttpEntity<String> entity = new HttpEntity<String>(headers);
            ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            log.info("[AddUser] add user success, info " + results.getBody());
            return JSON.parseObject(results.getBody());
        }
        catch (HttpClientErrorException e) {
            JSONObject json = null;
            json = JSON.parseObject(e.getResponseBodyAsString());
            log.warn("[AddUser] add user error, info " + json);
            return json;
        }
    }

    public JSON getUserByName(String name) {
        String url = configBeanHub.address + HUB_API_URL_USERS + "/" + name;
        try {
            ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, entity(), String.class);
            JSONObject json = JSON.parseObject(results.getBody());
            return json;
        }
        catch (HttpClientErrorException e) {
            JSONObject json = JSON.parseObject(e.getResponseBodyAsString());

            return json;
        }
    }

    public JSON stopUserServer(String name) {
        String url = configBeanHub.address + HUB_API_URL_USERS + "/" + name +"/server";
        try {
            ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.DELETE, entity(), String.class);
            return JSON.parseObject(results.getBody());
        }
        catch (HttpClientErrorException e) {
            JSONObject json = null;
            json = JSON.parseObject(e.getResponseBodyAsString());
            return json;

        }
        catch (ResourceAccessException e) {
            JSONObject json = new JSONObject();
            json.put("status", 202);
            json.put("message", "server has been stopped");
            return json;
        }
    }

//    public String getUserAccess(String url) {
//        try {
//            ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, entity(), String.class);
////            JSONObject json = JSON.parseObject(results.getBody());
//            return results.getBody();
//        }
//        catch (HttpClientErrorException e) {
////            JSONObject json = JSON.parseObject(e.getResponseBodyAsString());
//
//            return e.getResponseBodyAsString();
//        }
//    }
}
