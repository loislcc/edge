package edu.buaa.web.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import edu.buaa.domain.Info;
import edu.buaa.domain.Notification;
import edu.buaa.domain.messaging.TargetNotification;
import edu.buaa.repository.InfoRepository;
import edu.buaa.service.*;
import edu.buaa.service.messaging.*;

import edu.buaa.web.rest.util.IPUtils;
import io.swagger.models.auth.In;
import org.apache.kafka.common.network.Send;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {


    private UpdateTargetNotificationProducer updateTargetNotificationProducer;
    private ShareNotiProducer shareNotiProducer;
    private GameNotiProducer gameNotiProducer;
    private ToConsoleProducer toConsoleProducer;
    private Constant constant;
    private InfoService infoService;
    private final Logger log = LoggerFactory.getLogger(GameNotiConsumer.class);

    @Resource
    private SendClient sendClient;
    @Resource
    private SendClient3 sendClient3;

    private gdata  gdata1;

    private HashMap<Stragey,Integer> history = new HashMap<>();
    private int m = 0;
    private double Msize = 0;
    private int n = 3;// 设备数量
    private int[][] oldgrap = new int[n][n];
    private int[][] grap = new int[n][n]; // 最终网络结构邻接矩阵
    private double wofout = 1;
    private double wofin = 1;
    private int bound = 200; // 策略选择上界
    private String[] deviceName = {"edge","edge2","edge3"};
    private String[][] bintial= new String[n][]; //各设备拥有的初始内容块
    private HashMap<String,HashSet<String>> map=new HashMap<>();
    private HashMap<String,Integer> sizeofms = new HashMap<>();



    public ApiController(UpdateTargetNotificationProducer updateTargetNotificationProducer, ShareNotiProducer shareNotiProducer,
                         Constant constant, InfoService infoService, SendClient sendClient, SendClient3 sendClient3,
                         GameNotiProducer gameNotiProducer, ToConsoleProducer toConsoleProducer,gdata gdata) {
        this.updateTargetNotificationProducer = updateTargetNotificationProducer;
        this.shareNotiProducer = shareNotiProducer;
        this.constant  = constant;
        this.infoService = infoService;
        this.sendClient = sendClient;
        this.sendClient3 = sendClient3;
        this.gameNotiProducer = gameNotiProducer;
        this.toConsoleProducer = toConsoleProducer;
        this.gdata1 = gdata;
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




    @GetMapping("/sendfromEdge")
    public void sendEdge() {
        Notification msg = new Notification();
        msg.setOwner(constant.Edgename);
        msg.setBody("hello!");
        shareNotiProducer.sendMsgToEdges(msg);

    }

    @GetMapping("/tests")
    public void tests() {
        JSONObject RES = gdata1.gettest();
        log.debug("%%%%%%%%%%%%%%%%,{}",RES.get("res"));
    }

    @GetMapping("/test")
    public void test() {
//        Info info = infoService.findbyname("file01");
//        System.err.println("!!!!"+info.toString());

        double alltuntu = 0;
        int n = 3;// 设备数量
        String[][] transmission = new String[n][];
        int[][] grap = {{0,0,0},{1,0,0},{0,0,0}}; // 最终网络结构邻接矩阵
        String[][] b= {{"file01","file02"},{"file02","file03"},{"file03"}};
        String[] all = {"file01","file02","file03"};
        String[] deviceName = {"edge","edge2","edge3"};
        HashMap<String,Integer> sizeofms = new HashMap<>();
        sizeofms.put("file01",12);
        sizeofms.put("file02",12);
        sizeofms.put("file03",12);
        double certaintotal = 108;
        double tuntu = 0;
        for(int v=0;v<n;v++){
            boolean flag = false;
            for(int t=0;t<n;t++){
                if(grap[t][v] == 1) {
                    String[] vv = b[v];
                    String[] tt = b[t] ;
                    String[] transTtoV =  BJC.getJ(BJC.getC(all,vv),tt);     // 传输内容块
                    // 传输
                    infoService.translate(deviceName[t],deviceName[v],transTtoV);    // 传输
                    String[] newv = BJC.getB(transTtoV,vv);
                    transmission[v] = newv;
                    flag = true;
                    for(String one: transTtoV){
                        tuntu += sizeofms.get(one);
                    }
                    break;
                }
            }
            if(!flag){
                transmission[v] = b[v];
            }
        }
        for(int g = 0;g < transmission.length;g++){   // 传输后的内容情况 作为下次迭代的输入
            b[g] = transmission[g].clone();
        }
        System.out.println("-----------");
        double total = 0;
        for(int g = 0;g < b.length;g++){   // 传输后的内容情况 作为下次迭代的输入
            for(int h=0;h<b[g].length;h++){
                System.out.print(b[g][h]);
                total+=sizeofms.get(b[g][h]);
            }
            System.out.println("");
        }
        System.out.println("吞吐量："+ tuntu);
        alltuntu += tuntu;
        double average = (double) total/ (certaintotal*n);
        System.out.println("平均内容量："+ average);
        System.out.println("-----------");

    }

    @GetMapping("/game")
    public  ResponseEntity<JSONArray> runGame() {
        JSONArray links = new JSONArray();
        List<Info> infos = infoService.findAllInfo();
        List<Info> infos2 = sendClient.get2Infos();
        List<Info> infos3 = sendClient3.get3Infos();
        List<List<Info>> anyinfos = new ArrayList<>();
        anyinfos.add(infos);
        anyinfos.add(infos2);
        anyinfos.add(infos3);




        bintial[0] = convert(infos);
        bintial[1] = convert(infos2);
        bintial[2] = convert(infos3);   // 存放文件名filename 唯一
//        int[][] sizeofms = new int[3][];
//        sizeofms[0] = convertsize(infos);
//        sizeofms[1] = convertsize(infos2);
//        sizeofms[2] = convertsize(infos3);
        sizeofms = convertsize(anyinfos);

        double certaintotal = 0;
        double tst = 0;


        HashSet<String> hashSet = new HashSet<>();
        for(int i=0;i<bintial.length;i++){
            for(int j=0;j<bintial[i].length;j++){
                if(!hashSet.contains(bintial[i][j])){
                    hashSet.add(bintial[i][j]);
                    certaintotal += sizeofms.get(bintial[i][j]);
                }
                tst += sizeofms.get(bintial[i][j]);
            }
        }
        m = hashSet.size();
        Msize = certaintotal;
        double cer = tst/(certaintotal*3);
        DecimalFormat df = new DecimalFormat("0.00%");
        String tmp = df.format(cer);
        if(constant.leader.equals(constant.Edgename)){
            System.err.println("初始内容百分比:"+tmp);
            String str = "["+ constant.Edgename + "] 启动博弈，初始内容百分比 : " + tmp;
            toConsoleProducer.sendMsgToGatewayConsole(str);
        }
        else {
            Notification msg = new Notification();
            msg.setBody("初始内容百分比:"+tmp);
            msg.setOwner(constant.Edgename);
            msg.setType("gameintial");
            msg.setOwnerId(1);
            gameNotiProducer.sendMsgToEdges(msg);
        }
        if(tst/(certaintotal*3) == 1.0) {
            return ResponseEntity.ok().body(links);
        }
        String[] all = new String[hashSet.size()];
        int index = 0;
        for(String temp: hashSet)
            all[index++] = temp;                           // 存放目前全局所有类型的内容块
        String[][] b= new String[bintial.length][];
        for(int g = 0;g < bintial.length;g++){
            b[g] = bintial[g].clone();
        }
        int outinter = 0;
        double alltuntu = 0;

        while(true){
            try {
                Thread.sleep(1500);   // 休眠秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            iterations(b);
            outinter++;
            // 按结构传输
            String str = "-------- 第" + outinter + "次外层迭代 --------";
            JSONArray onesteplinks = new JSONArray();
            toConsoleProducer.sendMsgToGatewayConsole(str);
            String[][] transmission = new String[n][];
            double tuntu = 0;
            for(int v=0;v<n;v++){
                boolean flag = false;
                for(int t=0;t<n;t++){
                    if(grap[t][v] == 1) {
                        String[] vv = b[v];
                        String[] tt = b[t] ;
                        String[] transTtoV =  BJC.getJ(BJC.getC(all,vv),tt);     // 传输内容块
                        JSONObject jsonObject = new JSONObject();      // 记录links
                        jsonObject.put("source",deviceName[t]);
                        jsonObject.put("target",deviceName[v]);
                        jsonObject.put("content",transTtoV);
                        onesteplinks.add(jsonObject);
                        // 传输
                        infoService.translate(deviceName[t],deviceName[v],transTtoV);
                        String[] newv = BJC.getB(transTtoV,vv);
                        transmission[v] = newv;
                        flag = true;
                        for(String one: transTtoV){
                            tuntu += sizeofms.get(one);
                        }
                        break;
                    }
                }
                if(!flag){
                    transmission[v] = b[v];
                }
            }
            links.add(onesteplinks);  // 添加一次外次迭代的Links
            for(int g = 0;g < transmission.length;g++){   // 传输后的内容情况 作为下次迭代的输入
                b[g] = transmission[g].clone();
            }
//            System.out.println("-----------");
            double total = 0;
            for(int g = 0;g < b.length;g++){   // 传输后的内容情况 作为下次迭代的输入
                for(int h=0;h<b[g].length;h++){
//                    System.out.print(b[g][h]);
                    total+=sizeofms.get(b[g][h]);
                }
//                System.out.println("");
            }
//            System.out.println("吞吐量："+ tuntu);
            String str1 = "-------- 第" + outinter + "次 吞吐量： "+ tuntu +" --------";
            toConsoleProducer.sendMsgToGatewayConsole(str1);
            alltuntu += tuntu;
            double average = (double) total/ (certaintotal*n);
//            System.out.println("平均内容量："+ average);
            String tmp2 = df.format(average);
            String str2 = "-------- 第" + outinter + "次 平均内容量： "+ tmp2 +" --------";
            toConsoleProducer.sendMsgToGatewayConsole(str2);
//            System.out.println("-----------");

            boolean same = true;
            for(int ch = 0; ch<n; ch++){
                if(b[ch].length != hashSet.size()) {
                    same = false;
                    break;
                }
            }
            if (same ) {
//                System.out.println("全局一致:" + outinter);
                String str3 = "-------- 全局一致 次数： "+ outinter +" --------";
                toConsoleProducer.sendMsgToGatewayConsole(str3);
//                System.out.println("平均吞吐:" + (double)alltuntu/outinter);
                String str4= "-------- 全局一致 平均吞吐： "+ String.format("%.2f",alltuntu/outinter) +" --------";
                toConsoleProducer.sendMsgToGatewayConsole(str4);
                break;
            }
            for(int j = 0;j<grap.length;j++) {
                for(int k=0;k<grap[0].length;k++) grap[j][k] = 0;
            }
            for(int g = 0;g < grap.length;g++){
                oldgrap[g] = grap[g].clone();
            }
            history.clear();
        }

        return  ResponseEntity.ok().body(links);

    }

    private  void iterations(String[][] b){  // a:deviceName
        for(int i=0;i<n;i++){
            HashSet<String> set = new HashSet<>();
            Collections.addAll(set, b[i]);
            map.put(deviceName[i],set);   //保存每个设备的内容块，其中内容块唯一
        }
        boolean outflag = true;
        int iter = 0;

        for(int i=0;i<n;i++){
            System.out.println("process："+i);
            process(i,b);

            for(int y=0; y<n; y++){
                for(int x=0; x<n; x++){
                    if(grap[y][x] == 1){
                        System.out.println("ins:"+ (y+1) +"->" + (x+1));
                    }
                }
            }

            if(!tool.deepEquals(oldgrap,grap)){
                for(int g = 0;g < grap.length;g++){
                    oldgrap[g] = grap[g].clone();
                }
                iter++;
            } else {
                break;
            }
        }
        int converge = 0;
        while (true){
            Random rand = new Random();
            int any = rand.nextInt(n);
            System.out.println("process："+any);

            for(int y=0; y<n; y++){
                for(int x=0; x<n; x++){
                    if(grap[y][x] == 1){
                        System.out.println("ins:"+ (y+1) +"->" + (x+1));
                    }
                }
            }

            process(any,b);
            if(!tool.deepEquals(oldgrap,grap)){
                for(int g = 0;g < grap.length;g++){
                    oldgrap[g] = grap[g].clone();
                }
                iter++;
            } else {
                converge++;
            }
            if(converge>10) break;
        }

//        for(int i=0;i<n;i++){   // 遍历ED;
//            process(i);
//        }
        for(int y=0; y<n; y++){
            for(int x=0; x<n; x++){
                if(grap[y][x] == 1){
                    System.out.println("s:"+ (y+1) +"->" + (x+1));
                }
            }
        }
        System.out.println("内层迭代："+iter);

    }

    private  void process(int i,String[][] b){ // a:deviceName
//        double ut = utility[i];   // 当前有向图所有的效用

        double oldvalue = calcu(grap,i,b);
        List<Integer> listofout = new ArrayList<>();
        List<Integer> listofin = new ArrayList<>();
        List<Double> valueofout = new ArrayList<>();
        List<Double> valueofin = new ArrayList<>();
        for(int j=0; j<n; j++){    // 遍历所有邻居
            if(j==i) continue;
            boolean contiout = false,contiin = false;
            int blocktemp=0,blockin = 0;
            double blocksizetemp=0,blocksizein = 0;

            // 如果当前邻居的入度为1时则不能再进行连接
            for(int nn=0;nn<n;nn++){
                if(grap[nn][j]==1) {
                    contiout = true;
                    break;
                }
            }
            if(!contiout) {
                for(int k=0;k<b[i].length;k++){   //遍历当前玩家i的所有内容块 记录valueofout
                    HashSet temp = map.get(deviceName[j]);
                    if(!temp.contains(b[i][k])){
                        blocktemp += 1;
                        blocksizetemp += sizeofms.get(b[i][k]);
                    }
                }
                // 记录当前玩家j的 i->j out value
                if(blocktemp>0){   // 当有所贡献时进行记录
                    Stragey exit = new Stragey(i,j);   // 0开始计数
                    if(!history.containsKey(exit) || history.get(exit) != -1){
                        listofout.add(j);
                        valueofout.add(wofout*((double) blocktemp/(m-b[j].length))*blocksizetemp/Msize) ;
//                    valueofout.add(wofout*(double) blocktemp) ;
                    }
                }
            }

            // 如果当前邻居的出度为1时则不能再进行连接
            for(int nn=0;nn<n;nn++){
                if(grap[j][nn]==1) {
                    contiin = true;
                    break;
                }
            }
            if(!contiin) {
                for(int k=0;k<b[j].length;k++){ //遍历当前玩家j的所有内容块 记录valueofin
                    HashSet tempin = map.get(deviceName[i]);
                    if(!tempin.contains(b[j][k])){
                        blockin += 1;
                        blocksizein += sizeofms.get(b[j][k]);
                    }
                }
                if(blockin >0){  // 当有所贡献时进行记录
                    // 记录当前玩家j的 j->i in value
                    Stragey exit2 = new Stragey(j,i);   // 0开始计数
                    if(!history.containsKey(exit2) || history.get(exit2) != -1){
                        listofin.add(j);
                        valueofin.add(wofin*((double)blockin/(m-b[i].length))*blocksizein/Msize);
//                    valueofin.add(wofin*(double)blockin);
                    }
                }

            }
        }

        // 对valueofin valueofout index和value都进行排序
        int lengthin = listofin.size();
        int lengthout = listofout.size();
        pair[] sortin = new pair[lengthin];
        for(int q=0;q<lengthin;q++){
            sortin[q] = new pair(listofin.get(q),valueofin.get(q));
        }
        Arrays.sort(sortin);
        int[] indexsin = new int[lengthin];
        double[] valuesin = new double[lengthin];
        int ih = 0;
        for(pair element : sortin){
            indexsin[ih] = element.index;
            valuesin[ih] = element.value;
            ih++;
        }

        pair[] sortout = new pair[lengthout];
        for(int q=0;q<lengthout;q++){
            sortout[q] = new pair(listofout.get(q),valueofout.get(q));
        }
        Arrays.sort(sortout);
        int[] indexsout = new int[lengthout];
        double[] valuesout = new double[lengthout];
        ih = 0;
        for(pair element : sortout){
            indexsout[ih] = element.index;
            valuesout[ih] = element.value;
            ih++;
        }


        if(valuesin.length !=0 && valuesout.length!=0){
            double nowvlaue = valuesin[0] + valuesout[0] ;
//            double nowvlaue = valuesin[0] + valuesout[0] - dist[i][indexsin[0]]*dist[i][indexsout[0]]*0.001;

            if(nowvlaue >= oldvalue){
                Stragey best  = new Stragey(indexsin[0],i);
                if(history.containsKey(best)){
                    int old = history.get(best);
                    if(old+1>bound){
                        history.put(best,-1);   // 达到上界，即将舍弃该策略
                    }else {
                        history.put(best,old+1);  // 策略值加一
                    }
                } else {
                    history.put(best,1);
                }

                Stragey best2  = new Stragey(i,indexsout[0]);
                if(history.containsKey(best2)){
                    int old = history.get(best2);
                    if(old+1>bound){
                        history.put(best2,-1);   // 达到上界，舍弃该策略
                    }else {
                        history.put(best2,old+1);  // 策略值加一
                    }

                }else {
                    history.put(best2,1);
                }
                for(int pp=0; pp<n; pp++){
                    grap[pp][i] = 0;
                    grap[i][pp] = 0;
                }
                grap[indexsin[0]][i] = 1;
                grap[i][indexsout[0]] = 1;
            }
        }
        if(valuesin.length ==0 && valuesout.length!=0){
            double nowvalue = valuesout[0] ;
//            double nowvalue = valuesout[0] - dist[i][indexsout[0]]*0.001;

            if(nowvalue >= oldvalue){
                Stragey best  = new Stragey(i,indexsout[0]);
                if(history.containsKey(best)){
                    int old = history.get(best);
                    if(old+1>bound){
                        history.put(best,-1);   // 达到上界，舍弃该策略
                    }else {
                        history.put(best,old+1);  // 策略值加一
                    }
                } else {
                    history.put(best,1);
                }
                for(int pp=0; pp<n; pp++){
                    grap[pp][i] = 0;
                    grap[i][pp] = 0;
                }
                grap[i][indexsout[0]] = 1;

            }
        }
        if(valuesout.length ==0 && valuesin.length !=0){
            double nowvalue = valuesin[0] ;
//            double nowvalue = valuesin[0] - dist[i][indexsin[0]]*0.001;

            if( nowvalue >= oldvalue){
                Stragey best  = new Stragey(indexsin[0],i);
                if(history.containsKey(best)){
                    int old = history.get(best);
                    if(old+1>bound){
                        history.put(best,-1);   // 达到上界，舍弃该策略
                    }else {
                        history.put(best,old+1);  // 策略值加一
                    }
                } else {
                    history.put(best,1);
                }
                for(int pp=0; pp<n; pp++){
                    grap[pp][i] = 0;
                    grap[i][pp] = 0;
                }
                grap[indexsin[0]][i] = 1;
            }
        }
    }

    private  double calcu(int[][] network,int t ,String[][] b){
        double valueoft = 0;
        int blockin=0,blocktemp=0;
        double blocksizein = 0,blocksizetemp=0;
        int in = -1;
        for(int i=0; i<network.length; i++){
            if(network[i][t] == 1){
                in = i;
//                System.out.println("找到"+i+"->"+t);
                for(int p=0;p<b[i].length;p++){
                    HashSet temp = map.get(deviceName[t]);
                    if(!(temp.contains(b[i][p]))){
                        blockin += 1;
                        blocksizein += sizeofms.get(b[i][p]);
                    }
                }
            }
        }
        if(in!=-1)
//        valueoft += wofin*(double)blockin;
            valueoft += wofin*((double)blockin/(m-b[t].length))*blocksizein/Msize;
        int out = -1;
        for(int i=0;i<network[0].length;i++){
            if(network[t][i] == 1){
                out = i;
//                System.out.println("找到"+t+"->"+i);
                for(int p=0; p<b[t].length;p++){
                    HashSet temp = map.get(deviceName[i]);
                    if(!(temp.contains(b[t][p]))){
                        blocktemp +=1;
                        blocksizetemp += sizeofms.get(b[t][p]);
                    }
                }

            }
        }
//        valueoft += wofout*(double)blocktemp;
        if(out!=-1){
            valueoft += wofout*((double) blocktemp/(m-b[out].length))*blocksizetemp/Msize;
        }
//        System.out.println("over");
        return valueoft;
    }

//    private int[] convertsize(List<Info> infos) {
//        int size = infos.size();
//        int[] first = new int[size];
//        int j = 0;
//        for(Info info: infos) {
//            first[j++] = info.getFile_size().intValue();
//        }
//        return first;
//    }
    private HashMap<String,Integer> convertsize(List<List<Info>> anyinfos ) {
        HashMap<String, Integer> map  = new HashMap<>();
        for (List<Info> oneinfos : anyinfos) {
            for (Info info : oneinfos) {
                if (!map.containsKey(info.getFile_name())) {
                    map.put(info.getFile_name(), info.getFile_size().intValue());
                }
            }
        }
        return map;
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

    public static double[] randomLonLat(double MinLon, double MaxLon, double MinLat, double MaxLat) {
        Random random = new Random();
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        String lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();// 小数后6位
        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        String lat = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        double[] res = new double[2];
        res[0] = Double.valueOf(lon);
        res[1] = Double.valueOf(lat);
        return res;
}

    @GetMapping("/detectTarget")
    public ResponseEntity<JSONObject> detectTarget() {
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String strDateFormat2 = "yyyyMMddHHmmss";
        SimpleDateFormat sdf2 = new SimpleDateFormat(strDateFormat2);

        TargetNotification targetNotification = new TargetNotification();
        String localip = IPUtils.getLocalIpAddr();
        targetNotification.setIp(localip);
        targetNotification.setCurrentTime(sdf.format(new Date()));
        targetNotification.setName(sdf2.format(new Date()));
        targetNotification.setCategory("plane");
        double[] ran = randomLonLat(116.315157, 116.385297,39.97073, 39.974511);
        targetNotification.setLongitude(ran[0]);
        targetNotification.setLatitude(ran[1]);
        targetNotification.setSelfLongitude(ran[0]);
        targetNotification.setSelfLatitude(ran[1]);
        targetNotification.setOwner(Constant.Edgename);
        targetNotification.setBrief(localip+" ---> "+targetNotification.getCategory()+" in ("+targetNotification.getLongitude()+"，"+targetNotification.getLatitude()+");");
        try {
            System.out.println("start");
            toConsoleProducer.sendMsgToGatewayConsole(localip + " start task!");
            Process pr = Runtime.getRuntime().exec("python3 /home/nvidia/Desktop/edgeComputing/distributed/yolo.py");
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            System.out.println("in"+in.toString());
            System.out.println("pr"+pr.getErrorStream());
            toConsoleProducer.sendMsgToGatewayConsole("ip:10.4.10.200 collaboraitve computing");
            while ((line = in.readLine()) != null) {
                // line = decodeUnicode(line);
                System.out.println(line);
                if(line.startsWith("object is")){
                    String target = line.split(" ")[2];
                    targetNotification.setCategory(target);
                }
                if(line.startsWith("time")){
                    String target = line.split(":")[1];
                    toConsoleProducer.sendMsgToGatewayConsole("computing time: " + target +"s");
                }

            }
            in.close();
            pr.waitFor();
            toConsoleProducer.sendMsgToGatewayConsole("task end!");
            System.out.println("end");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("class is "+targetNotification.getCategory());
        targetNotification.setBrief(localip+" ---> "+targetNotification.getCategory()+" in ("+targetNotification.getLongitude()+"，"+targetNotification.getLatitude()+");");
        updateTargetNotificationProducer.sendMsgToGateway(targetNotification);
        toConsoleProducer.sendMsgToGatewayConsole(targetNotification.getBrief());
        return new ResponseEntity<>(HttpStatus.OK);

    }
}


