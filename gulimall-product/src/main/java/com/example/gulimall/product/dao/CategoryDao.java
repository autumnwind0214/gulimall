package com.example.gulimall.product.dao;

import com.example.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author AutumnWind
 * @email ${email}
 * @date 2023-10-02 22:17:21
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
