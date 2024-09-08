package diamond.util;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: hacds
 * @Date: 2024/2/24 22:37
 * @Description: success
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SuccessApiResult extends AbstractApiResult {
    /**
     * @author unicom
     *
     */
    private Object data;
    private String resultMsg;

    SuccessApiResult(Object data) {
        this.resultCode = 0;
        this.resultMsg = "success";
        this.data = data;
    }

}
