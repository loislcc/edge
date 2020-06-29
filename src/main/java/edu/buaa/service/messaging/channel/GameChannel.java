package edu.buaa.service.messaging.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface GameChannel {
    String CHANNELIN = "GameInChannel";
    String CHANNELOUT = "GameOutChannel";

    @Input(CHANNELIN)
    SubscribableChannel subscribableChannel();

    @Output(CHANNELOUT)
    MessageChannel messageChannelout();
}
