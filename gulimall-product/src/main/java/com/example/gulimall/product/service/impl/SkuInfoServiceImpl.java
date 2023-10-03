package com.example.gulimall.product.service.impl;

import com.example.gulimall.common.utils.PageUtils;
import com.example.gulimall.common.utils.Query;
import com.example.gulimall.product.dao.SkuInfoDao;
import com.example.gulimall.product.entity.SkuInfoEntity;
import com.example.gulimall.product.service.SkuInfoService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

}