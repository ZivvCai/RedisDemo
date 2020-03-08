package com.czw.demo.mapper;

import com.czw.demo.domain.OrderDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单Mapper
 *
 * @author caizw
 */
@Mapper
public interface OrderMapper {

    void insertOrder(OrderDTO orderDTO);

}
