package edu.buaa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.buaa.domain.Info;
import io.swagger.annotations.Authorization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "edge2")
public interface SendClient {

    @RequestMapping(value = "/api/infos/getall", method = RequestMethod.GET, consumes = "application/json")
    public List<Info> get2Infos();

}
