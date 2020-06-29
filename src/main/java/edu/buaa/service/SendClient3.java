package edu.buaa.service;

import edu.buaa.domain.Info;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "edge3")
public interface SendClient3 {
    @RequestMapping(value = "/api/infos/getall", method = RequestMethod.GET, consumes = "application/json")
    public List<Info> get3Infos();
}
