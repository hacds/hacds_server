package diamond.util;

import lombok.Data;


@Data
public abstract class AbstractApiResult {
    /**
     * statusCode
     */
    protected Integer resultCode;

    /**
     *
     *
     * @param data
     * @return ApiResult
     */
    public static AbstractApiResult success(Object data) {
        return new SuccessApiResult(data);
    }

    /**
     * error
     *
     * @param errorCode
     * @param errorMessage
     * @return ApiResult
     */
    public static AbstractApiResult error(Integer errorCode, String errorMessage) {
        return new ErrorApiResult(errorCode, errorMessage);
    }

}
