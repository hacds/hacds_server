package diamond.controller;

import com.alibaba.fastjson.JSONObject;
import diamond.dto.Diamond;
import diamond.service.DiamondDetailService;
import diamond.util.AbstractApiResult;
import diamond.util.ErrorApiResult;
import diamond.util.SuccessApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/diamond")
public class BusinessController {

    @Autowired
    private DiamondDetailService diamondDetailService;

    @PostMapping("/queryDiamondsHacds")
    public AbstractApiResult queryDiamondsHacds(){
        try {
            JSONObject result = diamondDetailService.queryDiamondsHacds();
            return SuccessApiResult.success(result);
        }catch (Exception e){
            return ErrorApiResult.error(1001,"error");
        }
    }


    @PostMapping("/queryDiamondByAddress")
    public JSONObject queryDiamondByAddress(@RequestBody JSONObject json) {
        return diamondDetailService.queryDiamondByAddress(json.getString("address"));
    }

    @RequestMapping("/queryDiamondTotal")
    public long querHacdsDiamondCount(){
        return diamondDetailService.queryDiamondTotal();
    }


}