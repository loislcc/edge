package edu.buaa.service;


import com.alibaba.fastjson.JSONObject;
import edu.buaa.domain.Info;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "gdata")
public interface gdata {
    @RequestMapping(value = "/api/loginfos/test", method = RequestMethod.GET, consumes = "application/json")
    public JSONObject gettest();
}
