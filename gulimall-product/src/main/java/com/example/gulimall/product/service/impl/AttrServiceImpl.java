package com.example.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.common.utils.PageUtils;
import com.example.gulimall.product.dao.AttrDao;
import com.example.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.example.gulimall.product.entity.AttrEntity;
import com.example.gulimall.product.entity.AttrGroupEntity;
import com.example.gulimall.product.entity.CategoryEntity;
import com.example.gulimall.product.service.AttrAttrgroupRelationService;
import com.example.gulimall.product.service.AttrGroupService;
import com.example.gulimall.product.service.CategoryService;
import com.example.gulimall.product.vo.AttrResponseVo;
import com.example.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.Query;

import com.example.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Resource
    AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Resource
    AttrGroupService attrGroupService;

    @Resource
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveAttr(AttrVo attr) {
        AttrEntity entity = new AttrEntity();
        BeanUtils.copyProperties(attr, entity);
        // 保存基本数据
        this.save(entity);
        // 保存关联关系
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(entity.getAttrId());
        attrAttrgroupRelationService.save(relationEntity);
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();

        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((item) -> {
                item.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params), queryWrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();

        List<AttrResponseVo> responseVoList = records.stream().map((item) -> {
            AttrResponseVo vo = new AttrResponseVo();
            BeanUtils.copyProperties(item, vo);

            // 设置分类和分组的名字
            AttrAttrgroupRelationEntity attrId = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",
                    item.getAttrId()));
            if (attrId != null) {
                AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrId.getAttrGroupId());
                vo.setGroupName(attrGroupEntity.getAttrGroupName());
            }

            CategoryEntity category = categoryService.getById(item.getCatelogId());
            if (category != null) {
                vo.setCatelogName(category.getName());
            }

            return vo;
        }).collect(Collectors.toList());

        pageUtils.setList(responseVoList);
        return pageUtils;
    }

}