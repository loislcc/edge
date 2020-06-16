package edu.buaa.service.messaging;

import edu.buaa.domain.Notification;
import edu.buaa.service.messaging.channel.ShareChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
public class ShareNotiConsumer {
    private final Logger log = LoggerFactory.getLogger(ShareNotiConsumer.class);


    @StreamListener(ShareChannel.CHANNELIN)
    public void listen(Notification msg) {
        log.debug("owner:{}",msg.getOwner());
        if(!msg.getOwner().equals("edge")){
            log.debug("listen Notification from edge device*: {}", msg.toString());
        }
    }
}
