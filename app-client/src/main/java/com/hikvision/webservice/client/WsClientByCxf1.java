package com.hikvision.webservice.client;

import com.hikvision.webservice.client.entity.MobileCodeWSSoap;
import com.hikvision.webservice.client.util.WebServiceUtils;

/**
 * @program: webservice
 * @description:
 * @Author: applehub.dong
 * @Date: 2019/11/4 17:04
 */

public class WsClientByCxf1 {

    public static void main(String[] args) {

//        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
//        factory.setServiceClass(MyService.class);
//        factory.setAddress("http://127.0.0.1:8090/myservice?wsdl");
//        // 需要服务接口文件
//        MyService client = (MyService) factory.create();
//        String result = client.authorization("admin", "123456");
//        System.out.println(result);

//        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
//        factory.setServiceClass(MobileCodeWSSoap.class);
//        factory.setAddress("http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx?wsdl");
//        // 需要服务接口文件
//        MobileCodeWSSoap mobileCodeWSSoap = (MobileCodeWSSoap) factory.create();
//        String result = mobileCodeWSSoap.getMobileCodeInfo("13192024082","");

        MobileCodeWSSoap webService = WebServiceUtils.getWebService(MobileCodeWSSoap.class, "http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx?wsdl");
        System.out.println(webService.getMobileCodeInfo("13192024082",""));
    }
}
