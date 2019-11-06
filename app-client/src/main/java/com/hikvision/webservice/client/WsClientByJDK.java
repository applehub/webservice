package com.hikvision.webservice.client;

/**
 * @program: webservice
 * @description:
 * @Author: applehub.dong
 * @Date: 2019/11/4 16:53
 */


import com.hikvision.webservice.common.MyService;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class WsClientByJDK {

    public static void main(String[] args) throws Exception {
        URL url = new URL("http://127.0.0.1:8090/myservice?wsdl");
        // 指定命名空间和服务名称
        QName qName = new QName("http://com.soft.ws/my", "MyService");
        Service service = Service.create(url, qName);
        // 通过getPort方法返回指定接口
        MyService myServer = service.getPort(new QName("http://com.soft.ws/my", "LoginPort"), MyService.class);
        // 调用方法 获取返回值
        String result = myServer.authorization("admin", "123456");
        System.out.println(result);
    }

}