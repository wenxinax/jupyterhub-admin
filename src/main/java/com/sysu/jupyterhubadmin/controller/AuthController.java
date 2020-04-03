package com.sysu.jupyterhubadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.sysu.jupyterhubadmin.component.ConfigBeanHub;
import com.sysu.jupyterhubadmin.component.ConfigBeanKsapi;
import com.sysu.jupyterhubadmin.hub.TokenRequest;
import com.sysu.jupyterhubadmin.service.AuthService;
import com.sysu.jupyterhubadmin.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashMap;
import java.util.Map;

@Api(value="", tags="/api/auth")
@EnableSwagger2
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    TokenRequest tokenRequest;
    @Autowired
    ConfigBeanHub configBeanHub;
    @Autowired
    ConfigBeanKsapi configBeanKsapi;

    @Autowired
    AuthService authService;
    @Autowired
    UserService userService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthController.class);



    @ApiOperation(value = "/signin",notes = "登录")
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public JSONObject signin(String username, String password){
        ResponseResult res = new ResponseResult();

//        JSONObject jj = (JSONObject) tokenRequest.VerifyToken(token);
//        log.info("[signin] get user from jupyterhub by token, data:" + jj);
//        if(jj.containsKey("status")) {
//            log.warn("[signin] get user from jupyterhub by token failed, data:" + jj);
//            ResponseResult res = new ResponseResult(-1, jj.getString("message"));
//            return res.toJSON();
//        }
        if (!userService.signin(username, password)) {
            res.setCode(-1);
            res.setMsg("error");
            return res.toJSON();
        }
        JSONObject jj = new JSONObject();
        jj.put("username", username);
        jj.put("password", password);
        Map<String, Object> modelMap = new HashMap<>(10);
        modelMap.put("token", authService.getToken(jj));
        res.setData(modelMap);
        log.info("[signin] signin success." + modelMap);
        return res.toJSON();
    }

    @ApiOperation(value = "/setHubToken",notes = "设置hub token")
    @RequestMapping(value = "/setHubToken", method = RequestMethod.POST)
    public String setHubToken(String token){
        configBeanHub.setToken(token);
        return "ok";
    }
    @ApiOperation(value = "/setKsToken",notes = "设置Ks token")
    @RequestMapping(value = "/setKsToken", method = RequestMethod.POST)
    public String setKsToken(String token){
        configBeanKsapi.setToken(token);
        return "ok";
    }
    @ApiOperation(value = "/setKsAddr",notes = "设置ks api地址")
    @RequestMapping(value = "/setKsAddr", method = RequestMethod.POST)
    public String setKsAddr(String addr){
        configBeanKsapi.setAddress(addr);
        return "ok";
    }
    @ApiOperation(value = "/setHubAddr",notes = "设置hub api地址")
    @RequestMapping(value = "/setHubAddr", method = RequestMethod.POST)
    public String setHubAddr(String addr){
        configBeanHub.setAddress(addr);
        return "ok";
    }
}
