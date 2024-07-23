package diamond.mapper;



import com.alibaba.fastjson.JSONObject;
import diamond.dto.Diamond;
import org.apache.ibatis.annotations.Mapper;


import java.util.HashMap;
import java.util.List;

@Mapper
public interface DiamondMapper {

    long queryHacdsDiamondCount();

    void synchronizationDiamond();

    void filterDiamond();

    void replaceIntoDiamond(List<Diamond> list);

    void deleteFromBak();

    void insertDiamondDetails(List<Diamond> list);

    JSONObject queryDiamondsHacds(HashMap map);

}