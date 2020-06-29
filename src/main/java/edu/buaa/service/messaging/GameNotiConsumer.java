package edu.buaa.service.messaging;

import edu.buaa.domain.Notification;
import edu.buaa.service.Constant;
import edu.buaa.service.RaftTask;
import edu.buaa.service.SendTask;
import edu.buaa.service.messaging.channel.GameChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
public class GameNotiConsumer {
    private final Logger log = LoggerFactory.getLogger(GameNotiConsumer.class);

    private Constant constant;


    public GameNotiConsumer(Constant constant) {
        this.constant = constant;

    }

    @StreamListener(GameChannel.CHANNELIN)
    public void listen(Notification msg) {
        if(!msg.getOwner().equals("edge")) {   // 来自其余边缘节点的消息
            if(msg.getType().equals("gameintial") && constant.leader.equals(constant.Edgename)){
                System.err.println(msg.getBody());
            }
        }
    }
}
