package com.hikvision.webservice.server;

/**
 * @program: webservice
 * @description:服务实现类
 * @Author: applehub.dong
 * @Date: 2019/11/4 16:48
 */



import com.hikvision.webservice.common.MyService;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(endpointInterface = "com.hikvision.webservice.common.MyService",
        name = "Login",// 定义Port名称
        serviceName = "MyService", // 修改WebService服务名称
        targetNamespace = "http://com.soft.ws/my" // 定义命名空间，默认为倒置的包名
//服务实现类和接口的注解要一样全
)
public class MyServiceImpl implements MyService {

    @WebMethod(operationName = "authorization" // 修改方法名
    )
    @Override
    public String authorization(@WebParam(name = "userId") String userId, @WebParam(name = "password") String password) {
        if ("admin".equals(userId) && "123456".equals(password)) {
            return "success";
        }
        return "error";
    }

}