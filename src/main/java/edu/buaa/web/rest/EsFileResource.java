package edu.buaa.web.rest;


import com.alibaba.fastjson.JSONObject;
import edu.buaa.domain.Constants;
import edu.buaa.service.GatewayClient;
import edu.buaa.service.messaging.GameNotiConsumer;
import edu.buaa.web.rest.util.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class EsFileResource {
    private final Logger log = LoggerFactory.getLogger(GameNotiConsumer.class);
    private GatewayClient gatewayClient;
    public EsFileResource(GatewayClient gatewayClient) {
        this.gatewayClient = gatewayClient;
    }

    @PostMapping(value = "/PostFile")
    public ResponseEntity<JSONObject> postFile(@RequestParam("file") MultipartFile files) throws Exception {
        String path = Constants.filepathtosave;
        File file = new  File ( path );
        String filename = files.getOriginalFilename();
        String  pathFile = path + File.separator + filename;
        File  newFile = new  File(pathFile);
        //判断文件夹是否存在，不存在则创建
        if( !file.exists( ) ){
            //创建文件夹
            file.mkdirs();
        }
        try{
            //文件传输到本地
            files.transferTo(newFile);

        }catch(IOException e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/getFile")
    public ResponseEntity<JSONObject> getFile(@RequestParam("name") String name) {
        String path = Constants.filepathtosave+File.separator+name+".txt";
        File f = new File(path);
        MultipartFile mf = utils.getMulFile(f);
        gatewayClient.PostFile(mf);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
