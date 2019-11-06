package com.hikvision.webservice.client;

/**
 * @program: webServices
 * @description:
 * @Author: applehub.dong
 * @Date: 2019/11/4 20:25
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

public class WsClientByAxis {
    /**
     * 跨平台调用Web Service出现
     *  faultString: 服务器未能识别 HTTP 头 SOAPAction 的值:
     * JAX-WS规范不需要SoapAction，但是.NET需要，所以产生了这个错误。
     * options.setAction("目标的TargetNameSpace"+"调用的方法名");
     */
    public static void main(String[] args) {
        String url = "http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx?wsdll";
        Service service = new Service();
        try {
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(url));
            // WSDL里面描述的接口名称(要调用的方法)
            call.setOperationName(new QName("http://WebXml.com.cn/", "getMobileCodeInfo"));
            //跨平台调用加上这个
            call.setUseSOAPAction(true);
            call.setSOAPActionURI("http://WebXml.com.cn/getMobileCodeInfo");
            // 接口方法的参数名, 参数类型,参数模式 IN(输入), OUT(输出) or INOUT(输入输出)
            call.addParameter("mobileCode", XMLType.XSD_STRING, ParameterMode.IN);
            call.addParameter("userID", XMLType.XSD_STRING, ParameterMode.IN);
            // 设置被调用方法的返回值类型
            call.setReturnType(XMLType.XSD_STRING);
            // 设置方法中参数的值
            Object result = call.invoke(new Object[] { "13192024082", "" });

            System.out.println(result.toString());
        } catch (ServiceException | RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

}