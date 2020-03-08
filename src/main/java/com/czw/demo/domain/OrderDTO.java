package com.czw.demo.domain;

import lombok.Data;

/**
 * 订单实例对象
 *
 * @author caizw
 */
@Data
public class OrderDTO {

    /**
     * 订单编号
     */
    private Integer orderId;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 订单描述
     */
    private String orderDescribe;
}
