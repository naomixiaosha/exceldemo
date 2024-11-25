package com.reminis.exceldemo.util;

import lombok.Data;

/**
 * 定义前端接口调用返回信息
 * @param <T>
 */
@Data
public class Result<T> {

    private int code = 0;

    private String message = "SUCCESS";

    private Integer number = 0;

    private T data;

}
