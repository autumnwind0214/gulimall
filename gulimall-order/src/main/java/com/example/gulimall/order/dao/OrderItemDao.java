package com.example.gulimall.order.dao;

import com.example.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author AutumnWind
 * @email ${email}
 * @date 2023-10-03 00:04:10
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
