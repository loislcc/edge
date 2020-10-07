package edu.buaa.service.messaging;

import edu.buaa.domain.messaging.TargetNotification;
import edu.buaa.service.messaging.channel.ToConsoleChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(ToConsoleChannel.class)
public class ToConsoleProducer {
    private final Logger log = LoggerFactory.getLogger(ToConsoleProducer.class);

    @Autowired
    private ToConsoleChannel toConsoleChannel;

    public void sendMsgToGatewayConsole(TargetNotification targetNotification){
        log.debug("send msg to gateway console ");
        try {
            log.debug("send result: {}",
                toConsoleChannel.messageChannel().send(MessageBuilder.withPayload(targetNotification).build()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
