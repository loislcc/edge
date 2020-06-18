package edu.buaa.service;


import edu.buaa.domain.Device;
import edu.buaa.service.messaging.ShareNotiProducer;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@EnableBinding(Constant.class)
public class Constant {
    public String Edgename = "edge";
    public int term = 0;
    public String leader = "";
    public List<Integer> neigboursId = null;
    public List<String> neigboursName = null;
    public List<Device> neigbour = new ArrayList<>();
    public int ticket = 1;
}
