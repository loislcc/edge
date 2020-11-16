package edu.buaa.service.messaging;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.buaa.domain.Info;
import edu.buaa.domain.Notification;
import edu.buaa.repository.InfoRepository;
import edu.buaa.service.Constant;
import edu.buaa.service.InfoService;
import edu.buaa.service.RaftTask;
import edu.buaa.service.SendTask;
import edu.buaa.service.messaging.channel.GameChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameNotiConsumer {
    private final Logger log = LoggerFactory.getLogger(GameNotiConsumer.class);

    private Constant constant;
    private InfoService infoService;
    private final InfoRepository infoRepository;
    private final GameNotiProducer gameNotiProducer;
    private final ToConsoleProducer toConsoleProducer;


    public GameNotiConsumer(Constant constant,InfoService infoService,
                            InfoRepository infoRepository,GameNotiProducer gameNotiProducer,ToConsoleProducer toConsoleProducer) {
        this.constant = constant;
        this.infoService  = infoService;
        this.infoRepository = infoRepository;
        this.gameNotiProducer = gameNotiProducer;
        this.toConsoleProducer = toConsoleProducer;

    }

    @StreamListener(GameChannel.CHANNELIN)
    public void listen(Notification msg) {
        if(!msg.getOwner().equals("edge")) {   // 来自其余边缘节点的消息
            if(msg.getType().equals("gameintial") && constant.leader.equals(constant.Edgename)){
                System.err.println(msg.getBody());
                String str = "["+ constant.Edgename + "] 启动博弈，" +msg.getBody();
                toConsoleProducer.sendMsgToGatewayConsole(str);
            }
            if(msg.getType().equals("translateFile") && msg.getTarget().equals(constant.Edgename)){
                System.err.println("Get File from " + msg.getOwner() );
                // 处理接收到的文件信息并存储
                JSONArray files = JSONArray.parseArray(msg.getBody());
                for(Object file: files) {
                    System.err.println( file.toString() );
                    JSONObject one = (JSONObject) file;
                    Info info = new Info();
                    info.setFile_type(one.getString("filetype"));
                    info.setNote(one.getString("note"));
                    info.setFile_size(one.getLong("filesize"));
                    info.setFile_name(one.getString("filename"));
                    info.setFile_body(one.getBytes("filebody"));
                    info.setFile_bodyContentType(one.getString("filebodyContentType"));
                    if(!infoService.existsbyname(info.getFile_name())){   // 不存在重复的name 就存储
                        log.debug("!!!!!!!!!!!!!1,{}",info.toString());
                        infoService.save(info);
                        infoService.saveIMG(info);
                    }
                }
            }
            if(msg.getType().equals("translate") && msg.getTarget().equals(constant.Edgename)){
                JSONObject object = JSONObject.parseObject(msg.getBody());
                String Tnode = object.getString("source");
                String Vnode = object.getString("target");
                JSONArray tmp =  object.getJSONArray("content");
                String[] transTtoV = new String[tmp.size()];
                int j=0;
                for(Object s: tmp){
                    transTtoV[j++] = s.toString();
                }
                log.debug("translate from : {} to : {}, *{}*",Tnode,Vnode,fomat(transTtoV));
                JSONArray trans = new JSONArray();
                for(String filename: transTtoV){
                    Optional<Info> infoOptional =  infoRepository.findByfileName(filename);
                    if(infoOptional.isPresent()){
                        Info info  = infoOptional.get();
                        JSONObject one = new JSONObject();
                        one.put("filename", info.getFile_name());
                        one.put("filesize",info.getFile_size());
                        one.put("filebody",info.getFile_body());
                        one.put("filebodyContentType",info.getFile_bodyContentType());
                        one.put("filetype",info.getFile_type());
                        one.put("note",info.getNote());
                        trans.add(one);
                    }
                }
                Notification notification = new Notification();
                notification.setType("translateFile");
                notification.setOwner(Tnode);
                notification.setTarget(Vnode);
                System.err.println(trans.toJSONString());
                notification.setBody(trans.toJSONString());
                gameNotiProducer.sendMsgToEdges(notification);
            }
        }
    }
    private String fomat(String[] transTtoV){
        StringBuilder back = new StringBuilder();
        for(String tmp: transTtoV) {
            back.append(tmp).append(",");
        }
        String backstring = String.valueOf(back);
        return backstring.substring(0,backstring.length()-1);
    }
}
