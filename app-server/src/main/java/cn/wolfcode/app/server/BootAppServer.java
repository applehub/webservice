package cn.wolfcode.app.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BootAppServer {
    public static void main(String[] args) {
        //启动Spring容器
        new ClassPathXmlApplicationContext("classpath:application.xml");
    }
}
