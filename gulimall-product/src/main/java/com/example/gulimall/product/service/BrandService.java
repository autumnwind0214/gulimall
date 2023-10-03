package com.example.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.gulimall.common.utils.PageUtils;
import com.example.gulimall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author AutumnWind
 * @email ${email}
 * @date 2023-10-02 22:17:21
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

