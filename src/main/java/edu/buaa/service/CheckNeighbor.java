package edu.buaa.service;


import edu.buaa.domain.Device;
import edu.buaa.domain.Notification;
import edu.buaa.service.messaging.ShareNotiProducer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class CheckNeighbor {
    private Constant constant;
    private ShareNotiProducer shareNotiProducer;

    public CheckNeighbor(Constant constant, ShareNotiProducer shareNotiProducer) {
        this.constant = constant;
        this.shareNotiProducer = shareNotiProducer;
    }
    //3.添加定时任务
    @Scheduled(cron = "0/5 * * * * ?")  //或直接指定时间间隔，例如：5秒//@Scheduled(fixedRate=5000)
    private void configureTasks() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        for(Device device:constant.neigbour){
            Date last = df.parse(device.getLastime());
            Date now = new Date();
            long diff = now.getTime() - last.getTime();//这样得到的差值是毫秒级别
            if(diff > 15000){
                constant.neigbour.remove(device);  /// 15秒设备下线
            }
        }
        System.err.println("执行静态定时检查设备在线状态: " + constant.neigbour.toString());
    }

    @Scheduled(cron = "0/5 * * * * ?")  //或直接指定时间间隔，例如：5秒//@Scheduled(fixedRate=5000)
    private void heartbeat() throws ParseException {    // 每隔5秒发送心跳信息
        Notification msg = new Notification();
        msg.setOwner(constant.Edgename);
        msg.setType("heart");
        msg.setOwnerId(1);
        msg.setBody("hello!");
        shareNotiProducer.sendMsgToEdges(msg);
    }

}
