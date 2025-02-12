package com.valuelab.common.util;

import org.springframework.http.HttpStatus;
import com.valuelab.dto.ErrorDetailDto;
import com.valuelab.dto.ResponseBodyDto;

public class CreateResponseFactory {

    /**
     * 正常時レスポンス作成
     * 
     * @param normalResponse
     * @param errorDetail
     * @return normalResponse
     */
    public static <T> ResponseBodyDto<T> getNormalResponse(ResponseBodyDto<T> normalResponse,
            ErrorDetailDto errorDetail) {

        normalResponse.setStatusCode(HttpStatus.OK.value());

        errorDetail.setCode("00");
        errorDetail.setMessage("");

        normalResponse.setError(errorDetail);
        normalResponse.setData(null);

        return normalResponse;
    }

    /**
     * 正常時レスポンス作成
     * 
     * @param data
     * @param errorDetail
     * @return normalResponse
     */
    public static <T> ResponseBodyDto<T> normal(T data) {
        ResponseBodyDto<T> normalResponse = new ResponseBodyDto<>();

        normalResponse.setStatusCode(HttpStatus.OK.value());

        ErrorDetailDto errorDetail = new ErrorDetailDto();
        errorDetail.setCode("00");
        errorDetail.setMessage("");

        normalResponse.setError(errorDetail);
        normalResponse.setData(data);

        return normalResponse;
    }

    /**
     * 異常時レスポンス作成
     * 
     * @param errorResponse
     * @param errorDetail
     * @return errorResponse
     */
    public static <T> ResponseBodyDto<T> getErrorResponse(ResponseBodyDto<T> errorResponse,
            ErrorDetailDto errorDetail) {

        errorResponse.setError(errorDetail);
        errorResponse.setData(null);

        return errorResponse;
    }

}
