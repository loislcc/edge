package edu.buaa.service;

import edu.buaa.domain.Notification;
import edu.buaa.service.messaging.ShareNotiProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Order(value=1)
public class RaftTask implements CommandLineRunner {
    private Constant constant;
    private ShareNotiProducer shareNotiProducer;

    public RaftTask(Constant constant, ShareNotiProducer shareNotiProducer){
        this.constant = constant;
        this.shareNotiProducer = shareNotiProducer;
    }

    @Override
    public void run(String... args) throws Exception {
        //代码块
        System.err.println("启动开始: " + LocalDateTime.now());
        if(constant.leader.equals("")){
            Notification msg = new Notification();
            msg.setOwner(constant.Edgename);
            msg.setOwnerId(1);
            msg.setType("request");
            msg.setBody("request for leader!");
            shareNotiProducer.sendMsgToEdges(msg);
            System.err.println("启动请求leader: " + LocalDateTime.now());
        }
    }
}
