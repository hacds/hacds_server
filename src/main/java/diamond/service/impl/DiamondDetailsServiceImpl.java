package diamond.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import diamond.dto.Diamond;
import diamond.mapper.DiamondMapper;
import diamond.service.DiamondDetailService;
import diamond.util.ErrorApiResult;
import diamond.util.SuccessApiResult;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@Service
public class DiamondDetailsServiceImpl implements DiamondDetailService {



    @Autowired
    private DiamondMapper diamondMapper;

    @Autowired
    private RestTemplate restTemplate;

    private static final String CONTENT_TYPE = "Content-type";
    private static final String APPLICATION = "application/json";
    private HttpEntity<byte[]> httpEntity;

    @Value(value="${diamond.totalurl}")
    private String TOTAL_URL;

    @Value(value="${diamond.addressurl}")
    private String ADDRESS_URL;
    @Value(value = "${diamond.nameurl}")
    private String NAME_URL;
    @Override
    public long queryHacdsDiamondCount() {
        return diamondMapper.queryHacdsDiamondCount();
    }

    @Override
    public long queryDiamondTotal() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION);
        httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(TOTAL_URL, HttpMethod.GET, httpEntity, byte[].class);
        byte[] resultByte = responseEntity.getBody();
        String resultStr = new String(resultByte);
        JSONObject json = JSONObject.parseObject(resultStr);
        return Long.parseLong(json.getString("minted_diamond"));
    }

    @Override
    public JSONObject queryDiamondByAddress(String address) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(CONTENT_TYPE, APPLICATION);
            httpEntity = new HttpEntity<>(null, headers);
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(ADDRESS_URL+address, HttpMethod.GET, httpEntity, byte[].class);

            byte[] resultByte = responseEntity.getBody();
            String resultStr = new String(resultByte);
            JSONObject json = JSONObject.parseObject(resultStr);
            String diamonds = json.getString("diamonds");
            String[] diamondArray = splitByNumber(diamonds, 6);
            JSONArray list = new JSONArray();
            for (int i = 0; i < diamondArray.length; i++) {
                list.add(getDiamondDetailByName(diamondArray[i]));
            }
            return (JSONObject) JSONObject.toJSON(SuccessApiResult.success(list));
        }catch (Exception e){
            return (JSONObject) JSONObject.toJSON(ErrorApiResult.error(1,  e.getMessage()));
        }
    }

    @Override
    public void synchronizationDiamond() {
        diamondMapper.synchronizationDiamond();
    }

    @Override
    public void filterDiamond() {
        diamondMapper.filterDiamond();
    }

    @Override
    public void replaceIntoDiamond(List<Diamond> list) {
        diamondMapper.replaceIntoDiamond(list);
    }

    @Override
    public void deleteFromBak() {
        diamondMapper.deleteFromBak();
    }

    @Override
    public void insertDiamondDetails(List<Diamond> list) {
        diamondMapper.insertDiamondDetails(list);
    }

    @Override
    public JSONObject queryDiamondsHacds(HashMap map) {
        return diamondMapper.queryDiamondsHacds(map);
    }

    public Diamond getDiamondDetailByName(String name){
        ResponseEntity<byte[]> responseEntity= restTemplate.exchange(NAME_URL+name, HttpMethod.GET, httpEntity, byte[].class);
        return JSONObject.toJavaObject(JSONObject.parseObject(responseEntity.getBody()),Diamond.class);
    }




    public  String[] splitByNumber(String s, int chunkSize){
        int chunkCount = (s.length() / chunkSize) + (s.length() % chunkSize == 0 ? 0 : 1);
        String[] returnVal = new String[chunkCount];
        for(int i=0;i<chunkCount;i++){
            returnVal[i] = s.substring(i*chunkSize, Math.min((i+1)*chunkSize, s.length()));
        }
        return returnVal;
    }





}
