package edu.buaa.service.messaging;

import edu.buaa.domain.Notification;
import edu.buaa.service.messaging.channel.GameChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@EnableBinding(GameNotiProducer.class)
public class GameNotiProducer {
    private final Logger log = LoggerFactory.getLogger(GameNotiProducer.class);
    @Autowired

    private GameChannel gameChannel;

    public void sendMsgToEdges(Notification msg){
        log.debug("send game msg to edges ");
        try {
            log.debug("send result: {}",
                gameChannel.messageChannelout().send(MessageBuilder.withPayload(msg).build()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
