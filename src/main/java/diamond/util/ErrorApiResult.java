package diamond.util;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: hacds
 * @Date: 2024/2/24 22:35
 * @Description: error
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorApiResult extends AbstractApiResult {
    /**
     *
     */
    private String resultMsg;

    ErrorApiResult(Integer code, String message) {
        this.resultCode = code;
        this.resultMsg = message;
    }

}
