package edu.buaa.service.messaging;

import edu.buaa.domain.Notification;
import edu.buaa.domain.messaging.TargetNotification;
import edu.buaa.service.messaging.channel.ShareChannel;
import edu.buaa.service.messaging.channel.UpdateTargetChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(ShareNotiProducer.class)
public class ShareNotiProducer {
    private final Logger log = LoggerFactory.getLogger(ShareNotiProducer.class);

    @Autowired
    private ShareChannel shareChannel;

    public void sendMsgToEdges(Notification msg){
        log.debug("send msg to edges ");
        try {
            log.debug("send result: {}",
                shareChannel.messageChannelout().send(MessageBuilder.withPayload(msg).build()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


