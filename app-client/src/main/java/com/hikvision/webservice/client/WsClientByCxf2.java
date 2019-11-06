package com.hikvision.webservice.client;


import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import javax.xml.namespace.QName;

/**
 * @program: webservice
 * @description:
 * @Author: applehub.dong
 * @Date: 2019/11/4 17:06
 */

public class WsClientByCxf2 {

    public static void main(String[] args) throws Exception {

        //采用动态工厂方式 不需要指定服务接口
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://127.0.0.1:8090/myservice?wsdl");
        QName qName = new QName("http://com.soft.ws/my", "authorization");
        Object[] result = client.invoke(qName, new Object[] { "admin", "123456"});
        System.out.println(result[0]);
    }
}
