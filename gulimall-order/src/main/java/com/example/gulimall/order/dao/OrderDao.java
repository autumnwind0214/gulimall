package com.example.gulimall.order.dao;

import com.example.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author AutumnWind
 * @email ${email}
 * @date 2023-10-03 00:04:10
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
