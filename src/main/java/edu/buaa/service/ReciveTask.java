package edu.buaa.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(value=1)   // 启动级别，首先启动接收当前系统中其他leader的心跳信息（如果存在leader）
public class ReciveTask implements CommandLineRunner {
    public void run(String... args) throws Exception {
        //代码块
        System.err.println("执行自启动任务时间: " + LocalDateTime.now());

    }
}
