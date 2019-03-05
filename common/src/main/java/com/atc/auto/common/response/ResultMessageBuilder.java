package com.atc.auto.common.response;

import com.atc.auto.common.bean.ApiResult;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: kaKaXi
 * @Date: 2018/12/10 21:55
 * @Version 1.0
 * @Description:
 */
@Data
public class ResultMessageBuilder {

    /**
     * 返回码返回结果
     * @param enums
     * @param result
     * @return
     */
    public static ResponseEntity<ApiResult> buildResullt(ReturnCodeEnum enums, Object result) {
        ApiResult rm = new ApiResult(enums.getCode(), enums.getDesc(), result);
        return new ResponseEntity<ApiResult>(rm, HttpStatus.OK);
    }

    /**
     * 返回码响应 成功
     * @param enums
     * @return
     */
    public static ResponseEntity<ApiResult> buildResullt(ReturnCodeEnum enums) {
        ApiResult rm = new ApiResult(enums.getCode(), enums.getDesc());
        return new ResponseEntity<ApiResult>(rm, HttpStatus.OK);
    }

    /**
     * 返回码响应 成功
     * @param enums
     * @return
     */
    public static ResponseEntity<String> buildResponse(ReturnCodeEnum enums) {

        StringBuilder sb = new StringBuilder();
        sb.append(enums.getCode());
        sb.append(":");
        sb.append(enums.getDesc());
        return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
    }

    public static String buildResponseStr(ReturnCodeEnum enums) {

        StringBuilder sb = new StringBuilder();
        sb.append(enums.getCode());
        sb.append(":");
        sb.append(enums.getDesc());
        return sb.toString();
    }

    /**
     * 返回码响应 失败
     * @param enums
     * @return
     */
    public static ResponseEntity<Map<String,Object>> buildResultResponse(ReturnCodeEnum enums) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(enums.getCode(), enums.getDesc());
        return new ResponseEntity<Map<String,Object>>(map, HttpStatus.BAD_REQUEST);
    }
}
