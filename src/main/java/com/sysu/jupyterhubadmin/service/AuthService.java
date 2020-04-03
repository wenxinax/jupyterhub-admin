package com.sysu.jupyterhubadmin.service;

import com.alibaba.fastjson.JSONObject;

public interface AuthService {
    String getToken(JSONObject user);
}
