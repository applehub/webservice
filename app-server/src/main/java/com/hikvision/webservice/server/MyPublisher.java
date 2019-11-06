package com.hikvision.webservice.server;

/**
 * @program: webservice
 * @description:
 * @Author: applehub.dong
 * @Date: 2019/11/4 16:49
 */



import com.hikvision.webservice.common.MyService;

import javax.xml.ws.Endpoint;

/**
 * 发布服务
 *
 */
public class MyPublisher {

    public static void main(String[] args) {
        //指定服务url
        String url = "http://127.0.0.1:8090/myservice";
        //指定服务实现类
        MyService server = new MyServiceImpl();
        //采用命令行发布者Endpoint发布服务
        Endpoint.publish(url, server);
    }

}