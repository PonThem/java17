package com.valuelab.dto;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class ResponseBodyDto<T> {

    private int statusCode;
    private T data;
    private ErrorDetailDto error;

}
