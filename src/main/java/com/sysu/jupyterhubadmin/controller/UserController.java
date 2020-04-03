package com.sysu.jupyterhubadmin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sysu.jupyterhubadmin.component.ConfigBeanHub;
import com.sysu.jupyterhubadmin.config.annotation.LoginToken;
import com.sysu.jupyterhubadmin.config.annotation.PassToken;
import com.sysu.jupyterhubadmin.entity.User;
import com.sysu.jupyterhubadmin.hub.UserRequest;
import com.sysu.jupyterhubadmin.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value="", tags="/api/users")
@EnableSwagger2
@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    ConfigBeanHub configBeanHub;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRequest userRequest;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserRequest.class);


    @ApiOperation(value = "/user_login",notes = "get用户登录信息")
    @LoginToken
    @RequestMapping(value = "/user_login_info", method = RequestMethod.GET)
    public Map<String, Object> getAllUser(){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<User> list = userService.getAllUser();
        modelMap.put("user_pwd_list", list);
        ResponseResult res = new ResponseResult();
        res.setData(modelMap);

        return res.toJSON();
    }

    @ApiOperation(value = "/{username}_login_info",notes = "get用户名密码")
    @LoginToken
    @RequestMapping(value = "/{username}_login_info", method = RequestMethod.GET)
    public Object getUserByName(@PathVariable String username){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        User user = userService.getUserByName(username);
        modelMap.put("user_pwd", user);
        ResponseResult res = new ResponseResult();
        res.setData(modelMap);

        return res.toJSON();
    }

    @ApiOperation(value = "",notes = "get所有用户信息(jupyterhub)")
    @LoginToken
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Object getUserInfo(){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        JSONArray users = (JSONArray) userRequest.getUsers();
        List<User> users1 = userService.getAllUser();
        for(int i = 0; i < users.size(); i ++){
            JSONObject u= (JSONObject) users.get(i);
            u.put("role", users1.get(i).getRole());
            u.put("blocked", users1.get(i).getBlocked());
            users.set(i, u);
        }
        modelMap.put("users", users);
        ResponseResult res = new ResponseResult();
        res.setData(modelMap);

        return res.toJSON();
    }

    @ApiOperation(value = "",notes = "跳转到用户的server")
    @PassToken
    @RequestMapping(value = "/{username}/access", method = RequestMethod.GET)
    public void access(@PathVariable String username, HttpServletResponse response) {
        String url = configBeanHub.address + "/user/" + username + "/lab";
        response.setStatus(301);
        //todo 请求头不起作用
//        response.setHeader("Authorization", "token 333943de7dbe4703a0b9ccf779cdccaf");
        response.setHeader("Location", url);
        response.setHeader("Connection", "close");

    }

    @ApiOperation(value = "",notes = "跳转到spawner")
    @PassToken
    @RequestMapping(value = "/{username}/spawn", method = RequestMethod.GET)
    public void spawn(@PathVariable String username, HttpServletResponse response) {
        String url =  configBeanHub.address + "/hub/spawn/" + username;
        response.setStatus(301);
        //todo 请求头不起作用
//        response.setHeader("Authorization", "token 333943de7dbe4703a0b9ccf779cdccaf");
        response.setHeader("Location", url);
        response.setHeader("Connection", "close");

    }
    @ApiOperation(value = "/{username}/server",notes = "stop server")
    @LoginToken
    @RequestMapping(value = "/{username}/server", method = RequestMethod.DELETE)
    public Map<String, Object> stopUserServer(@PathVariable String username){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        JSON msg =  userRequest.stopUserServer(username);
        ResponseResult res = new ResponseResult();
        res.setData(msg);
        return res.toJSON();
    }

    @ApiOperation(value = "/{username}",notes = "delete user")
    @LoginToken
    @RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteUser(@PathVariable String username){
        ResponseResult res = new ResponseResult();
        if(userService.deleteUser(username) > 0){
            res.setCode(0);
        } else {
            res.setCode(-1);
            res.setMsg("delete failed");
            return res.toJSON();
        }
        JSON json =  userRequest.deleteUser(username);
        JSONObject jsonObject = JSON.parseObject(String.valueOf(json));

        if (jsonObject != null) {
            if (jsonObject.containsKey("status") && String.valueOf(jsonObject.get("status")).equals("404")) {
                res.setCode(-1);
                res.setMsg("404 Not Found");
                return res.toJSON();
            }
        } else {
            res.setData(json);
        }
        return res.toJSON();
    }

    public static String ReadAsChars(HttpServletRequest request)
    {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
    @ApiOperation(value = "/{username}",notes = "edit user")
    @LoginToken
    @RequestMapping(value = "/{username}", method = RequestMethod.PATCH)
    @PatchMapping(produces = "application/merge-patch+json;charset=UTF-8")
    public Map<String, Object> editUser(@PathVariable String username, HttpServletRequest request){
        ResponseResult res = new ResponseResult();
//        log.info("[editUser] request body: " + ReadAsChars(request) );
        JSONObject param = JSON.parseObject(ReadAsChars(request));
        String newName = param.getString("newName");
        boolean admin = param.getBoolean("admin");
        log.info("[editUser] edit user, username: " + username + ", newName: " + newName + ", admin: " + admin);


        if(userService.editUserName(username, newName) > 0){
            res.setCode(0);
        } else {
            log.warn("[editUser] edit username failed, username: " + username + ", newName: " + newName);
            res.setCode(-1);
            res.setMsg("edit failed");
            return res.toJSON();
        }
        JSON json =  userRequest.editUser(username, newName, admin);
        JSONObject jsonObject = JSON.parseObject(String.valueOf(json));

        if (jsonObject != null) {
            if (jsonObject.containsKey("status") && String.valueOf(jsonObject.get("status")).equals("404")) {
                res.setCode(-1);
                res.setMsg("404 Not Found");
                return res.toJSON();
            }
        } else {
            res.setData(json);
        }
        return res.toJSON();
    }

    @ApiOperation(value = "/{username}",notes = "add user")
    @LoginToken
    @RequestMapping(value = "/{username}", method = RequestMethod.POST)
    public Map<String, Object> addUser(@PathVariable String username, HttpServletRequest request){
        JSONObject param = JSON.parseObject(ReadAsChars(request));

        System.out.println(param.getString("password") + "  " + param.getInteger("blocked") + "  "+ username + " " + param.getInteger("role"));
        User user = new User();
        user.setUsername(username);
        user.setPassword(param.getString("password"));
        user.setBlocked(param.getInteger("blocked"));
        user.setRole(param.getInteger("role"));
        ResponseResult res = new ResponseResult();
        if(userService.addUser(user) > 0){
            res.setCode(0);
        } else {
            res.setCode(-1);
            res.setMsg("add failed");
            return res.toJSON();
        }
        JSON json =  userRequest.addUser(username);
        JSONObject jsonObject = JSON.parseObject(String.valueOf(json));

        if (jsonObject != null) {
            if (jsonObject.containsKey("status") && String.valueOf(jsonObject.get("status")).equals("404")) {
                res.setCode(-1);
                res.setMsg("404 Not Found");
                return res.toJSON();
            }
        } else {
            res.setData(json);
        }
        return res.toJSON();
    }

}
