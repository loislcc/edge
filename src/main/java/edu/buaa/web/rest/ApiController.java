package edu.buaa.web.rest;

import com.alibaba.fastjson.JSONObject;
import edu.buaa.domain.Notification;
import edu.buaa.domain.messaging.TargetNotification;
import edu.buaa.service.Constant;
import edu.buaa.service.messaging.ShareNotiProducer;
import edu.buaa.service.messaging.UpdateTargetNotificationProducer;

import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {


    private UpdateTargetNotificationProducer updateTargetNotificationProducer;
    private ShareNotiProducer shareNotiProducer;
    private Constant constant;




    public ApiController(UpdateTargetNotificationProducer updateTargetNotificationProducer,ShareNotiProducer shareNotiProducer,
                         Constant constant) {
        this.updateTargetNotificationProducer = updateTargetNotificationProducer;
        this.shareNotiProducer = shareNotiProducer;
        this.constant  = constant;
    }
    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public
    @ResponseBody
    String map(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        JSONObject res = new JSONObject();
       res.put("x","116.41995287496285");
       res.put("y","39.917274760176798");
        return res.toJSONString();

    }

    @GetMapping("/detectTarget")
    public void detectTarget() {
        TargetNotification targetNotification = new TargetNotification();
        SimpleDateFormat sdf = new SimpleDateFormat();
        String localip = getLocalIpAddr();
        targetNotification.setIp(localip);
        targetNotification.setCurrentTime(sdf.format(new Date()));
        targetNotification.setCategory("Car");
        targetNotification.setLongitude(120.191157);
        targetNotification.setLatitude(30.274664);
//        targetNotification.setSelfLongitude(116.434924);
//        targetNotification.setSelfLatitude(39.915671);116.433547
        targetNotification.setSelfLongitude(120.188426);
        targetNotification.setSelfLatitude(30.273884);
        updateTargetNotificationProducer.sendMsgToGateway(targetNotification);
    }

    @GetMapping("/sendfromEdge")
    public void sendEdge() {
        Notification msg = new Notification();
        msg.setOwner(constant.Edgename);
        msg.setBody("hello!");
        shareNotiProducer.sendMsgToEdges(msg);

    }





    // 通用获取本机ip
    public String getLocalIpAddr() {

        String clientIP = null;
        Enumeration<NetworkInterface> networks = null;
        try {
            //获取所有网卡设备
            networks = NetworkInterface.getNetworkInterfaces();
            if (networks == null) {
                //没有网卡设备 打印日志  返回null结束
//                log.debug("networks  is null");
                return null;
            }
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }
        InetAddress ip;
        Enumeration<InetAddress> addrs;
        // 遍历网卡设备
        while (networks.hasMoreElements()) {
            NetworkInterface ni = networks.nextElement();
            try {
                //过滤掉 loopback设备、虚拟网卡
                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) {
                    continue;
                }
            } catch (SocketException e) {
//                logger.info(e.getMessage());
            }
            addrs = ni.getInetAddresses();
            if (addrs == null) {
//                logger.info("InetAddress is null");
                continue;
            }
            // 遍历InetAddress信息
            while (addrs.hasMoreElements()) {
                ip = addrs.nextElement();
                if (!ip.isLoopbackAddress() && ip.isSiteLocalAddress() && ip.getHostAddress().indexOf(":") == -1) {
                    try {
                        clientIP = ip.toString().split("/")[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        clientIP = null;
                    }
                }
            }
        }
        return clientIP;
    }

}


