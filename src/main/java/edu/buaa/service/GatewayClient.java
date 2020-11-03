package edu.buaa.service;

import com.alibaba.fastjson.JSONObject;
import edu.buaa.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "gdata",configuration = FeignConfig.class)
public interface GatewayClient {
    @RequestMapping(value = "/api/esinfos/FiletoGateway",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JSONObject> PostFile(@RequestPart("file") MultipartFile files);
//
//    @RequestMapping(value = "/api/getFile",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public MultipartFile getFile(@RequestParam("name") String name);
}
