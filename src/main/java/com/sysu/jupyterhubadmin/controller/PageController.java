package com.sysu.jupyterhubadmin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Api(value="", tags="/")
@EnableSwagger2
@Controller
@RequestMapping("/")
public class PageController {

    @ApiOperation(value = "",notes = "login")
    @RequestMapping(value = "")
    public String root(){
        return "login.html";
    }

    @ApiOperation(value = "login",notes = "login")
    @RequestMapping(value = "login")
    public String login(){
        return "login.html";
    }

    @ApiOperation(value = "users",notes = "users")
    @RequestMapping(value = "users")
    public String users(){
        return "users.html";
    }

    @ApiOperation(value = "nodes",notes = "nodes")
    @RequestMapping(value = "nodes")
    public String nodes(){
        return "nodes.html";
    }

    @ApiOperation(value = "dashboard",notes = "dashboard")
    @RequestMapping(value = "dashboard")
    public String dashboard(){
        return "dashboard.html";
    }
}
