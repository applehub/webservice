package com.hikvision.webservice.client.util;

/**
 * @program: webservice
 * @description:
 * @Author: applehub.dong
 * @Date: 2019/11/4 18:43
 */

import java.util.ResourceBundle;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.stereotype.Service;
@Service
public class WebServiceUtils {

    public static final int CXF_CLIENT_CONNECT_TIMEOUT = 30 * 1000;
    public static final int CXF_CLIENT_RECEIVE_TIMEOUT = 30 * 1000;


    /**
     *
     * @param clazz
     * @param url
     * @param timeout
     * @return
     */
    public static <T> T getWebService(Class<T> clazz, String url, Integer... timeout) {

//        ResourceBundle dBResources = ResourceBundle.getBundle("application-config");
//        String url = dBResources.getString(paraName);
        if (timeout == null || timeout.length == 0) {
            return getWebServiceByUrl(clazz, url);
        } else if (timeout.length == 1) {
            return getWebServiceByUrl(clazz, url, timeout[0], timeout[0]);
        } else {
            return getWebServiceByUrl(clazz, url, timeout[0], timeout[1]);
        }
    }

    private static <T> T getWebServiceByUrl(Class<T> clazz, String url, Integer connectTimeout, Integer receiveTimeout) {
        // 用于创建JAX-WS代理的工厂，此类提供对用于设置代理的内部属性的访问。使用它可以提供比标准JAX-WS API更多的控制。
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(clazz);
        factory.setAddress(url);
        @SuppressWarnings("unchecked")
        // 创建可用于进行远程调用的代理对象。
        T webService = (T) factory.create();
        // 设置接口 连接超时和请求超时
        // 通过代理对象获取本地客户端
        Client proxy = ClientProxy.getClient(webService);
        // 通过本地客户端设置 网络策略配置
        HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
        // 用于配置客户端HTTP端口的属性
        HTTPClientPolicy policy = new HTTPClientPolicy();
        // 超时控制 单位 : 毫秒
        policy.setConnectionTimeout(connectTimeout);
        policy.setReceiveTimeout(receiveTimeout);
        conduit.setClient(policy);
        return webService;
    }

    private static <T> T getWebServiceByUrl(Class<T> clazz, String url) {
        return getWebServiceByUrl(clazz, url, CXF_CLIENT_CONNECT_TIMEOUT, CXF_CLIENT_RECEIVE_TIMEOUT);
    }

}