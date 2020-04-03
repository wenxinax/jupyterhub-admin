package com.sysu.jupyterhubadmin.controller;

import com.alibaba.fastjson.JSON;
import com.sysu.jupyterhubadmin.config.annotation.LoginToken;
import com.sysu.jupyterhubadmin.hub.UserRequest;
import com.sysu.jupyterhubadmin.kubernetes.KubeApiRequest;
import com.sysu.jupyterhubadmin.kubesphere.KsApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Api(value="", tags="/api/monitor")
@EnableSwagger2
@RestController
@CrossOrigin
@RequestMapping("/api/monitor")
public class MonitorController {
    @Autowired
    KsApiRequest ksApiRequest;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserRequest.class);

    @ApiOperation(value = "/nodes",notes = "get nodes")
    @LoginToken
    @RequestMapping(value = "/nodes", method = RequestMethod.GET)
    public JSON getNodes(){
        return ksApiRequest.getNodes();
    }

    @ApiOperation(value = "/cluster",notes = "get cluster")
    @LoginToken
    @RequestMapping(value = "/cluster", method = RequestMethod.GET)
    public JSON getCluster(){
        return ksApiRequest.getCluster();
    }
}