package com.sysu.jupyterhubadmin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 返回体
 * {
 *   "code": 0,  # 0 or 1
 *   ”msg“: "message" # optional
 *   "data": {        # optional
 *     "user_info": {
 *       "last_activity": "2020-01-12T11:13:07.528287Z",
 *       "kind": "user",
 *       "created": "2019-12-25T13:05:26.211198Z",
 *       "name": "wenxinax",
 *       "admin": true,
 *       "groups": []
 *     }
 *   }
 * }
 */
public class ResponseResult<T> {

    //0表示正常
    public static final int SUCCESS_CODE = 0;

    public static final int ERROR_CODE = -1;

    //未登录状态，无Token
    public static final int LOGOUT_CODE = -10001;


    /**
     * 成功
     */
    public static final ResponseResult SUCCESS = new ResponseResult(SUCCESS_CODE, "SUCCESS");
    /**
     * 失败
     */
    public static final ResponseResult ERROR = new ResponseResult(ERROR_CODE, "ERROR");

    //返回状态码
    private int code;

    //提示信息
    private String msg;

    //数据类型
    private T data;

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public ResponseResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult() {   //默认code都是SUCCESS_CODE
        this.code = SUCCESS_CODE;
    }

    public ResponseResult(T data) {  //默认code都是SUCCESS_CODE
        this.code = SUCCESS_CODE;
        this.data = data;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public String toString() {
        return JSON.toJSONString(this);
    }
    public JSONObject toJSON() {
        return JSON.parseObject(this.toString());
    }
}

