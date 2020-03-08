package com.czw.demo.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品实例对象
 *
 * @author caizw
 */
@Data
public class GoodDTO implements Serializable {

    /**
     * 商品编号
     */
    private Integer goodId;

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 商品价格
     */
    private Integer goodPrice;

    /**
     * 商品数量
     */
    private Integer goodTotal;

    /**
     * 商品描述
     */
    private String goodDescribe;

}
