package com.point.sale.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ResponseQuery {
    private String message;
    private int status;
    private Object result;
}
