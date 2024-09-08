package diamond.service;

import com.alibaba.fastjson.JSONObject;
import diamond.dto.Diamond;

import java.util.HashMap;
import java.util.List;

public interface DiamondDetailService {



    long queryHacdsDiamondCount();

    long queryDiamondTotal();

    JSONObject queryDiamondByAddress(String address);

    void synchronizationDiamond();

    void filterDiamond();

    void replaceIntoDiamond(List<Diamond> list);

    void deleteFromBak();

    void insertDiamondDetails(List<Diamond> list);

    JSONObject queryDiamondsHacds(HashMap map);

}
