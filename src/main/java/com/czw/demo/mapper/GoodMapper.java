package com.czw.demo.mapper;

import com.czw.demo.domain.GoodDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品Mapper
 *
 * @author caizw
 */
@Mapper
public interface GoodMapper {

    void insertGood(GoodDTO goodDTO);

    void updateGoodTotal(@Param("goodId") Integer goodId, @Param("goodTotal") Integer goodTotal);

    GoodDTO selectGood(@Param("goodId") Integer goodId);

}
