package edu.buaa.service.messaging;

import edu.buaa.domain.Device;
import edu.buaa.domain.Notification;
import edu.buaa.service.Constant;
import edu.buaa.service.messaging.channel.ShareChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ShareNotiConsumer {
    private final Logger log = LoggerFactory.getLogger(ShareNotiConsumer.class);

    private Constant constant;

    public ShareNotiConsumer(Constant constant) {
        this.constant = constant;
    }

    @StreamListener(ShareChannel.CHANNELIN)
    public void listen(Notification msg) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        log.debug("owner:{}",msg.getOwner());
        if(!msg.getOwner().equals("edge")) {   // 来自其余边缘节点的消息
            log.debug("listen Notification from edge device*: {}", msg.toString());
            if (msg.getType().equals("heart")) {  // 心跳信息，用于记录邻居
                boolean flag = false;
                if(constant.neigbour == null || constant.neigbour.size() == 0) {
                    Device device = new Device();
                    device.setId(msg.getOwnerId());
                    device.setName(msg.getOwner());
                    device.setLastime(df.format(new Date()));
                    constant.neigbour.add(device);
                } else {
                    for (Device device : constant.neigbour) {
                        if (device.getId() == msg.getOwnerId()) {
                            device.setLastime(df.format(new Date()));
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        Device device = new Device();
                        device.setId(msg.getOwnerId());
                        device.setName(msg.getOwner());
                        device.setLastime(df.format(new Date()));
                        constant.neigbour.add(device);
                    }
                }

            }

            // 处理其他类型信息


        }

    }
}
