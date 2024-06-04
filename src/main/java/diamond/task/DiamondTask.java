package diamond.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import diamond.dto.Diamond;
import diamond.service.DiamondDetailService;
import diamond.util.AnalyseDiamond;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
@RequestMapping("/diamond")
public class DiamondTask {

    private static final String CONTENT_TYPE = "Content-type";
    private static final String APPLICATION = "application/json";

    @Value(value = "${diamond.nameurl}")
    private String NAME_URL;
    @Value(value="${diamond.numberurl}")
    private String NUMBER_URL;

    @Value(value="${diamond.totalurl}")
    private String TOTAL_URL;
    @Value(value="${diamond.tradeurl}")
    private String TRADE_URL;
    @Value(value="${diamond.lastblock}")
    private String LAST_URL;

    @Value(value="${diamond.order}")
    private String ORDER_URL;

    @Value(value="${diamond.orderdetails}")
    private String ORDERDETAILS_URL;


    @Autowired
    private DiamondDetailService diamondDetailService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private AnalyseDiamond analyseDiamond;

    @RequestMapping("/syn")
    public void synchronizationDiamond(){
        diamondDetailService.synchronizationDiamond();
    }

    @RequestMapping("/filter")
    public void filterDiamond(){
        diamondDetailService.filterDiamond();
    }

    private HttpEntity<byte[]> httpEntity;

    private long total = 0;

    private long height = 0;


    /**
     *
     */
    @Scheduled(cron = "${task.updateDiamond}")
    @SchedulerLock(name = "updateDiamondsTask" , lockAtLeastFor = 55000, lockAtMostFor = 55000)
    public void updateDiamonds(){
        long current = 0;
        List<Diamond> list = new ArrayList<Diamond>();
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION);
        //采用setDoInput(true); 和output进行请求
        httpEntity = new HttpEntity<>(null, headers);
        JSONObject block = getLastBlock();
        if(height == 0){
            height = block.getLongValue("height");
            return;
        }
        current = block.getLongValue("height");
        log.info(current+"");
        if(current>height && block.getLongValue("txs")>0){
            JSONArray array = queryOrders(current);
            for(int i=1;i<array.size();i++){
                String order = array.getString(i);
                String names = queryOrderDetails(order);
                if(!names.equals("")){
                String[] nameArray = names.split(",");
                for(int j =0 ; j<nameArray.length;j++){
                    Diamond d = getDiamondDetailByName(nameArray[j]);
                    d = getAttributeDiamond(d);
                    if(d.getInscriptions().toUpperCase().contains("HACDS")){
                        list.add(d);
                    }


                }
                }



            }



        }
        if(list.size()>0){
            diamondDetailService.replaceIntoDiamond(list);
        }
        log.info(list.size()+"diamonds");
        height = current;
    }

    public String queryOrderDetails(String order){
        StringJoiner joiner = new StringJoiner(",");
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(ORDERDETAILS_URL+order, HttpMethod.GET, httpEntity, byte[].class);
        JSONObject json = JSONObject.parseObject(responseEntity.getBody());
        JSONArray array = json.getJSONArray("actions");
        for(int i=0;i<array.size();i++){
            JSONObject o =array.getJSONObject(i);
             int k = o.getIntValue("k");
            if(k==5 || k==6 || k==4 || k==32 || k==33){
                  if(k==4){
                      joiner.add(o.getString("name"));
                  }else{
                      joiner.add(o.getString("names"));
                  }
            }

        }
        return joiner.toString();
    }


    public JSONArray queryOrders(long current){
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(ORDER_URL+current, HttpMethod.GET, httpEntity, byte[].class);
        JSONObject json = JSONObject.parseObject(responseEntity.getBody());
        JSONArray array = json.getJSONArray("trshxs");
        return array;
    }

    @Scheduled(cron = "${task.updateDiamondTotal}")
    @PostConstruct
    @RequestMapping("/query")
    @Async
    @SchedulerLock(name = "collectDiamondTask" , lockAtLeastFor = 55000, lockAtMostFor = 55000)
    public void collectDiamond(){

        diamondDetailService.deleteFromBak();

        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION);

        httpEntity = new HttpEntity<>(null, headers);
        total = getTotalDiamond();
        long startTime = System.currentTimeMillis();

        dealData(total, 10);
        log.info((System.currentTimeMillis() - startTime)+"");




        synchronizationDiamond();

        filterDiamond();


        log.info(""+(System.currentTimeMillis() - startTime));






    }











    @RequestMapping("/deal")

    public void dealData(@RequestParam(value = "total", required = false)long total,int num){
        CountDownLatch currentQueryLatch;
        int avg = (int) (total / num);
        int mod=(int) (total % num);
        if(mod==0){
             currentQueryLatch = new CountDownLatch(10);
        }else {
             currentQueryLatch = new CountDownLatch(11);
        }

        for(int i=1;i<=num;i++){
            int count = i;
            taskExecutor.execute(()->queryAndInsert((count -1) * avg + 1, count * avg,currentQueryLatch));
        }
        if(mod > 0) {
            taskExecutor.execute(() -> queryAndInsert(total - mod+1, total, currentQueryLatch));
        }
        try {
            currentQueryLatch.await();
            log.info(""+total);
        } catch (Exception e) {
            log.error("", e);
        }

    }


    public void queryAndInsert(long start,long end ,CountDownLatch currentQueryLatch){
        List list = new ArrayList();
        for(long i =start;i<=end ; i++){
            Diamond d = getDiamondDetail(i);
//            Diamond d = new Diamond(i+"",i+"",i+"",i+"",i+"",i+"",i+"",i+"",i+"",i+"",i+"",i+"",i+"",i+"");
            if(d.getInscriptions().toUpperCase().contains("hacds".toUpperCase())){
                d = getAttributeDiamond(d);
            }

            list.add(d);
        }
        diamondDetailService.insertDiamondDetails(list);
        currentQueryLatch.countDown();
    }

    public JSONObject getLastBlock(){
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(LAST_URL, HttpMethod.GET, httpEntity, byte[].class);
        //请求结果中的body值为byte了性
        byte[] resultByte = responseEntity.getBody();
        String resultStr = new String(resultByte);
        JSONObject json = JSONObject.parseObject(resultStr);
        return json;
    }



    public long getTotalDiamond(){
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(TOTAL_URL, HttpMethod.GET, httpEntity, byte[].class);

        byte[] resultByte = responseEntity.getBody();
        String resultStr = new String(resultByte);
        JSONObject json = JSONObject.parseObject(resultStr);
        return Long.parseLong(json.getString("minted_diamond"));
    }

    public Diamond getDiamondDetail(long number){
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(NUMBER_URL+number, HttpMethod.GET, httpEntity, byte[].class);
        JSONObject json = JSONObject.parseObject(responseEntity.getBody());
        String name = json.getString("name");
        responseEntity= restTemplate.exchange(NAME_URL+name, HttpMethod.GET, httpEntity, byte[].class);
        return JSONObject.toJavaObject(JSONObject.parseObject(responseEntity.getBody()),Diamond.class);
    }


    public Diamond getDiamondDetailByName(String name){
        ResponseEntity<byte[]> responseEntity= restTemplate.exchange(NAME_URL+name, HttpMethod.GET, httpEntity, byte[].class);
        return JSONObject.toJavaObject(JSONObject.parseObject(responseEntity.getBody()),Diamond.class);
    }
    @RequestMapping("/test1")
    public void test1(){
       Diamond d=  getDiamondDetailByName("SHHHKS");
       d.getVisualGene();
    }

    public Diamond getAttributeDiamond(Diamond d){
        d= analyseDiamond.analyseDiamond(d);
        d.setGroupName(d.getName());
        return d;
    }






}
