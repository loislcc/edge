package edu.buaa.web.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import edu.buaa.domain.Info;
import edu.buaa.domain.Notification;
import edu.buaa.domain.messaging.TargetNotification;
import edu.buaa.repository.InfoRepository;
import edu.buaa.service.Constant;
import edu.buaa.service.InfoService;
import edu.buaa.service.SendClient;
import edu.buaa.service.SendClient3;
import edu.buaa.service.messaging.GameNotiProducer;
import edu.buaa.service.messaging.ShareNotiProducer;
import edu.buaa.service.messaging.UpdateTargetNotificationProducer;

import io.swagger.models.auth.In;
import org.apache.kafka.common.network.Send;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
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
    private GameNotiProducer gameNotiProducer;
    private Constant constant;
    private InfoService infoService;
    @Resource
    private SendClient sendClient;
    @Resource
    private SendClient3 sendClient3;




    public ApiController(UpdateTargetNotificationProducer updateTargetNotificationProducer, ShareNotiProducer shareNotiProducer,
                         Constant constant, InfoService infoService, SendClient sendClient, SendClient3 sendClient3,
                         GameNotiProducer gameNotiProducer) {
        this.updateTargetNotificationProducer = updateTargetNotificationProducer;
        this.shareNotiProducer = shareNotiProducer;
        this.constant  = constant;
        this.infoService = infoService;
        this.sendClient = sendClient;
        this.sendClient3 = sendClient3;
        this.gameNotiProducer = gameNotiProducer;
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

    @GetMapping("/game")
    public  void runGame() {
        List<Info> infos = infoService.findAllInfo();
        List<Info> infos2 = sendClient.get2Infos();
        List<Info> infos3 = sendClient3.get3Infos();
//        if (infos2 != null) {
//            for(Object info: infos2)
//                System.err.println(info.toString());
//        }
//
//        if (infos3 != null) {
//            for(Info info: infos3)
//                System.err.println(info.toString());
//        }

//        int[][] bintial={{1,2},{2,3},{3}}; //各设备拥有的初始内容块
        String[][] bintial= new String[3][]; //各设备拥有的初始内容块

        bintial[0] = convert(infos);
        bintial[1] = convert(infos2);
        bintial[2] = convert(infos3);
        int[][] sizeofms = new int[3][];
        sizeofms[0] = convertsize(infos);
        sizeofms[1] = convertsize(infos2);
        sizeofms[2] = convertsize(infos3);


        double certaintotal = 0;
        double tst = 0;
        HashSet<String> hashSet = new HashSet<>();
        for(int i=0;i<bintial.length;i++){
            for(int j=0;j<bintial[i].length;j++){
                if(!hashSet.contains(bintial[i][j])){
                    hashSet.add(bintial[i][j]);
                    certaintotal += sizeofms[i][j];
                }
                tst += sizeofms[i][j];
            }
        }
        if(constant.leader.equals(constant.Edgename))
            System.err.println("初始内容百分比:"+(tst/(certaintotal*3)));
        else {
            Notification msg = new Notification();
            msg.setBody("初始内容百分比:"+(tst/(certaintotal*3)));
            msg.setOwner(constant.Edgename);
            msg.setType("gameintial");
            msg.setOwnerId(1);
            gameNotiProducer.sendMsgToEdges(msg);
        }

    }

    private int[] convertsize(List<Info> infos) {
        int size = infos.size();
        int[] first = new int[size];
        int j = 0;
        for(Info info: infos) {
            first[j++] = info.getFile_size().intValue();
        }
        return first;
    }

    private String[] convert(List<Info> infos) {
        int size = infos.size();
        String[] first  = new String[size];
        int j = 0;
        for(Info info: infos) {
            first[j++] = info.getFile_name();
        }
        return first;
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


