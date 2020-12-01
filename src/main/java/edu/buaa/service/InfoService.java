package edu.buaa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.buaa.domain.Constants;
import edu.buaa.domain.Info;
import edu.buaa.domain.Notification;
import edu.buaa.repository.InfoRepository;
import edu.buaa.service.messaging.GameNotiProducer;
import edu.buaa.service.messaging.ToConsoleProducer;
import edu.buaa.web.rest.util.ImageUtil;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Info.
 */
@Service
@Transactional
public class InfoService {

    private final Logger log = LoggerFactory.getLogger(InfoService.class);

    private final InfoRepository infoRepository;
    private final Constant constant;
    private final GameNotiProducer gameNotiProducer;
    private final ToConsoleProducer toConsoleProducer;

    public InfoService(InfoRepository infoRepository,Constant constant,GameNotiProducer gameNotiProducer,ToConsoleProducer toConsoleProducer) {
        this.infoRepository = infoRepository;
        this.constant  = constant;
        this.gameNotiProducer = gameNotiProducer;
        this.toConsoleProducer = toConsoleProducer;
    }

    /**
     * Save a info.
     *
     * @param info the entity to save
     * @return the persisted entity
     */
    public Info save(Info info) {
        log.debug("Request to save Info : {}", info);
        return infoRepository.save(info);
    }

    /**
     * Get all the infos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Info> findAll(Pageable pageable) {
        log.debug("Request to get all Infos");
        return infoRepository.findAll(pageable);
    }


    /**
     * Get one info by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Info> findOne(Long id) {
        log.debug("Request to get Info : {}", id);
        return infoRepository.findById(id);
    }

    /**
     * Delete the info by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Info : {}", id);
        infoRepository.deleteById(id);
    }

    public void deleteall() {
        log.debug("Request to delete all infos");
        infoRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<Info> findAllInfo() {
        log.debug("Request to get all Infos");
        return infoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public boolean existsbyname(String name) {
        return infoRepository.existsByfileName(name);
    }

    @Transactional(readOnly = true)
    public Info findbyname(String filename) {
        return infoRepository.findByfileName(filename).get();
    }

    public void translate(String Tnode, String Vnode, String[] transTtoV) {
        if(Tnode.equals(constant.Edgename)){
            log.debug("translate from : {} to : {}, *{}*,",Tnode,Vnode,fomat(transTtoV));
            String str = "["+ constant.Edgename + "] translate from : " + Tnode+" to : " + Vnode +", * " + fomat(transTtoV) +" *";
            toConsoleProducer.sendMsgToGatewayConsole(str);
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
        } else {
            Notification notification = new Notification();
            notification.setType("translate");
            notification.setOwner(constant.Edgename);    // 用于接收
            notification.setTarget(Tnode);
            JSONObject msg = new JSONObject();
            msg.put("source",Tnode);
            msg.put("target",Vnode);
            msg.put("content", transTtoV);
            notification.setBody(msg.toJSONString());
            String str = "["+ Tnode + "] translate from : " + Tnode+" to : " + Vnode +", * " + fomat(transTtoV) +" *";
            toConsoleProducer.sendMsgToGatewayConsole(str);
            gameNotiProducer.sendMsgToEdges(notification);
        }
    }

    public String fomat(String[] transTtoV){
        StringBuilder back = new StringBuilder();
        for(String tmp: transTtoV) {
            back.append(tmp).append(",");
        }
        String backstring = String.valueOf(back);
        return backstring.substring(0,backstring.length()-1);
    }

    public void saveIMG(Info info) {
        String targetPath = Constants.filepathtosaveimg + File.separator + Constants.Edgename + File.separator + info.getFile_name() + ".png";
        byte[] bytes = info.getFile_body();
        InputStream input = new ByteArrayInputStream(bytes);
        ImageUtil.readBin2Image(input, targetPath);
    }
}
