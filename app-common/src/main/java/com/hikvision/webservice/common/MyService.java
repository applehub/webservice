package com.hikvision.webservice.common;

/**
 * @program: rpcdemo
 * @description:
 * @Author: applehub.dong
 * @Date: 2019/11/4 20:01
 */

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 基于soap协议(http+xml)的服务
 * 提供一个对外公开的服务
 */
@WebService(name = "Login",// 定义Port名称
        serviceName = "MyService", // 修改WebService服务名称
        targetNamespace = "http://com.soft.ws/my" // 定义命名空间，默认为倒置的包名
)
public interface MyService {
    @WebMethod(operationName = "authorization")
    String authorization(@WebParam(name = "userId") String userId, @WebParam(name = "password") String password);
}