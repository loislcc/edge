package edu.buaa.service;

import edu.buaa.repository.InfoRepository;
import edu.buaa.service.Constant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
@Component
@Order(value=2)
public class ShareTask implements CommandLineRunner {
    private final InfoRepository infoRepository;

    public ShareTask(InfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    public void run(String... args) throws Exception {
        //代码块
        System.err.println("执行自启动任务时间: " + LocalDateTime.now());

    }
//    @Scheduled(cron = "0/5 * * * * ?")
    //或直接指定时间间隔，例如：5秒 @Scheduled(fixedRate=5000)
    // 检查全局信息是否一致
    private void checkConsistency() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());

    }
}
