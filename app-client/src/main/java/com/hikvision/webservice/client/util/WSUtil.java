package com.hikvision.webservice.client.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.xfire.client.Client;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/** webservice 工具类
 *   该工具类使用了Xfire单例的方式，问了解决cas中webservice访问慢的问题;
 *   目前Xfire单例方式使用可能会有多线程并发问题，但是考虑实际业务场景，用户并发的可能性比较小，所以暂时提取公用工具类。
 *   后续计划加入webservice客户端实例池解决并发问题
 *   在具体调用的时候使用：WSUtil.remoteCall(wsdl, "getAjList", req);//“wsdl”为调用的url，“getList”为方法名字，“req”为传递的参数(json)
 * @Author: applehub.dong
 * @Date: 2019/11/5 9:20
 */
public class WSUtil {

   /**
    * 单例模式工具类,防止外部实例化
    * */
    private WSUtil() { }

    /**
     * 请求成功
     */
    final public static String WSCALL_STATUS_SUCCESS = "1";

    /**
     * 无权限
     */
    final public static String WSCALL_STATUS_NORIGHT = "2";

    /**
     * 参数错误
     */
    final public static String WSCALL_STATUS_BADPARAMS = "3";

    /**
     * 异常
     */
    final public static String WSCALL_STATUS_EXCEPTION = "4";

    /**
     * 日志
     */
    private static final Log logger = LogFactory.getLog(WSUtil.class);

    /**
     * 生成代理客户端
     */
    private final static class ProxyClient extends Client {
        private ProxyClient(URL url) throws Exception {
            super(url);
        }
        @Override
        public void close() {
            logger.warn("单例模式的webservice客户端,不需要关闭!");
        }
        /**
         * 真实关闭
         */
        public void reallyClose() {
            super.close();
        }
    }

    /**
     * webservice　客户端连接池
     */
    private static class ClientPool {

        /**
         * 连接池缓存
         */
        private static Map<String, ClientPool> pools = new HashMap<>();

        /**
         * 池对象
         */
        private Client[] pool;

        /**
         * 连接URL
         */
        private String url;

        /**
         * 当前连接标记
         */
        private int current;

        /**
         * 构造器,初始化连接池
         * @param url webservice地址
         * @param size 接池大小
         */
        public ClientPool(String url, int size) {
            this.url = url;
            pool = new Client[size];
            current = 0;
            pools.put(url, this);
        }

        /**
         * 从连接池中获取一个连接
         * @return
         */
        public synchronized Client get() {
            Client client = null;
            synchronized (this) {
                if (current >= 0) {
                    client = pool[current--];
                }
            }
            System.out.println("池大小：" + current);
            if (client == null) {
                client = initWSClient(url);
            }
            return client;
        }

        /**
         * 将连接放回连接池
         */
        public void reuse(Client client) {
            if (client == null) {
                return;
            }
            synchronized (this) {
                if (current < pool.length - 1) {
                    pool[++current] = client;
                } else {
                    destoryWSClient(client);
                }
            }
        }

        /**
         * 销毁某个连接
         */
        public void destory(Client client) {
            if (client != null) {
                destoryWSClient(client);
            }
        }

        /**
         * 获取连接池实例
         */
        public static ClientPool getInstance(String url) {
            if (StringUtils.isEmpty(url)) {
                return null;
            }
            ClientPool pool = pools.get(url);
            if (pool == null) {
                synchronized (pools) {
                    pool = pools.get(url);
                    if (pool == null) {
                        pool = new ClientPool(url, 5);
                        pools.put(url, pool);
                    } else {
                        pool = pools.get(url);
                    }
                }
            }
            return pool;
        }

    }


    /**
     * 初始化webservice客户端
     * @param url wsdl地址
     */
    private static Client initWSClient(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        ProxyClient client = null;
        try {
            client = new ProxyClient(new URL(url));
        } catch (Exception e) {
            logger.error("生成webservice客户端出错,URL:" + url, e);
        } finally {
            if (client != null) {
                try {
                    client.reallyClose();
                } catch (Exception e) {
                    logger.error("释放webservice客户端出错,URL:" + url, e);
                }
            }
        }
        return client;
    }

    /**
     * 释放webservice客户端
     */
    private static void destoryWSClient(Client client) {
        if (client != null) {
            try {
                ((ProxyClient) client).reallyClose();
            } catch (Exception e) {
                logger.error("释放webservice客户端失败,URL：" + client.getUrl());
            }
        }
    }

    /**
     * 用指定客户端执行远程调用
     * @param invokeFunc  调用方法
     * @param objArray 参数
     */
    private static String executeRemoteCall4String(Client client,String invokeFunc, Object[] objArray) throws Exception {
        if (client == null) {
            return null;
        }
        Object[] results = client.invoke(invokeFunc, objArray);
        if (results != null && results.length > 0) {
            String resultStr = (String) results[0];
            if (logger.isDebugEnabled()) {
                logger.debug("执行webservice返回结果：" + resultStr);
            }
            return resultStr;
        }
        return null;
    }

    /**
     * 执行远程调用,单例模式调用
     * @param url  wsdl URL
     * @param invokeFunc 调用方法
     */
    public static String remoteCall(String url, String invokeFunc,Object... param) {
        Client client = null;
        ClientPool pool = ClientPool.getInstance(url);
        logger.info("执行webservice使用参数：" + Arrays.toString(param));
        logger.info("执行webservice调用方法：" + invokeFunc);
        try {
            client = pool.get();
            return executeRemoteCall4String(client, invokeFunc, param);
        } catch (Exception e) {
            logger.error("执行webservice调用失败,尝试重新初始化客户端后调用", e);
            pool.destory(client);
            client = pool.get();
            try {
                return executeRemoteCall4String(client, invokeFunc, param);
            } catch (Exception e1) {
                logger.error("error ro invoke function: " + invokeFunc
                        + ", where remote call url=" + url + " and params:"
                        + Arrays.toString(param));
            }
        } finally {
            pool.reuse(client);
        }
        return null;
    }

    public static synchronized String remoteInvoke(String url, String invokeFunc, Object... param) {
        Client client = null;
        try {
            client = new Client(new URL(url));
            logger.info("执行webservice使用参数：" + Arrays.toString(param));
            logger.info("执行webservice调用方法：" + invokeFunc);
            return executeRemoteCall4String(client, invokeFunc, param);
        } catch (MalformedURLException e) {
            logger.error("error invoke function " + invokeFunc + ", params :"
                    + Arrays.toString(param));
        } catch (Exception e) {
            logger.error("error invoke function " + invokeFunc + ", params :"
                    + Arrays.toString(param));
        } finally {
            //client.close();
        }
        return null;
    }

    /**
     * 调用webservice,并封装返回结果
     * @param url wsdl
     * @param invokeFunc 执行的方法
     * @param objArray 参数数组
     */
    public static Document remoteCallForXmlDocument(String url, String invokeFunc, Object... objArray) {
        String result = remoteCall(url, invokeFunc, objArray);
        if (result != null) {
            try {
                return DocumentHelper.parseText(result);
            } catch (DocumentException e) {
                logger.error("webservice调用返回结果不是XML格式,XML解析出错", e);
            }
        }
        return null;
    }

    /**
     * 判断远程调用是否成功
     */
    public static boolean isRemoteCallSuccess(String result) {
        try {
            return isRemoteCallSuccess(DocumentHelper.parseText(result));
        } catch (DocumentException e) {
            logger.error("webservice调用返回结果不是XML格式,XML解析出错", e);
        }
        return false;
    }

    /**
     * 判断远程调用是否成功
     */
    public static boolean isRemoteCallSuccess(Document doc) {
        Element e = doc.getRootElement();
        // 如果不是<Result status='errorCode'></Result>格式的返回,则不进行校验
        if (e.element("Result") == null) {
            return true;
        }
        String status = e.attributeValue("status");
        return WSCALL_STATUS_SUCCESS.equals(status);
    }
}
