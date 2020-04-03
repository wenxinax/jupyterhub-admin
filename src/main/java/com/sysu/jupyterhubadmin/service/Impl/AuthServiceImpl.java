package com.sysu.jupyterhubadmin.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sysu.jupyterhubadmin.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public String getToken(JSONObject user) {
        String token = "";
        token = JWT.create().withAudience(user.getString("username")).sign(Algorithm.HMAC256(user.getString("password")));
        return token;
    }
}
