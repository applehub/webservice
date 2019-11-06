
package com.hikvision.webservice.client.entity;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "MobileCodeWSSoap", targetNamespace = "http://WebXml.com.cn/")
public interface MobileCodeWSSoap {

    @WebMethod(action = "http://WebXml.com.cn/getMobileCodeInfo")
    @WebResult(name = "getMobileCodeInfoResult", targetNamespace = "http://WebXml.com.cn/")
    @RequestWrapper(localName = "getMobileCodeInfo", targetNamespace = "http://WebXml.com.cn/", className = "cn.com.webxml.GetMobileCodeInfo")
    @ResponseWrapper(localName = "getMobileCodeInfoResponse", targetNamespace = "http://WebXml.com.cn/", className = "cn.com.webxml.GetMobileCodeInfoResponse")
    public String getMobileCodeInfo(
            @WebParam(name = "mobileCode", targetNamespace = "http://WebXml.com.cn/") String mobileCode,
            @WebParam(name = "userID", targetNamespace = "http://WebXml.com.cn/") String userID);
}
