package com.example.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.common.constant.ProductConstant;
import com.example.common.utils.PageUtils;
import com.example.gulimall.product.dao.AttrDao;
import com.example.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.example.gulimall.product.entity.AttrEntity;
import com.example.gulimall.product.entity.AttrGroupEntity;
import com.example.gulimall.product.entity.CategoryEntity;
import com.example.gulimall.product.service.AttrAttrgroupRelationService;
import com.example.gulimall.product.service.AttrGroupService;
import com.example.gulimall.product.service.CategoryService;
import com.example.gulimall.product.vo.AttrGroupRelationVo;
import com.example.gulimall.product.vo.AttrResponseVo;
import com.example.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
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
    AttrAttrgroupRelationService relationService;

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
        // 1.保存基本数据
        this.save(entity);
        // 2.保存关联关系
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(entity.getAttrId());
            relationService.save(relationEntity);
        }
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type", "base".equalsIgnoreCase(type) ?
                ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

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

            // 1.设置分类和分组的名字
            if ("base".equalsIgnoreCase(type)) {
                AttrAttrgroupRelationEntity attrId = relationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",
                        item.getAttrId()));
                if (attrId != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrId.getAttrGroupId());
                    vo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
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

    @Override
    public AttrResponseVo getAttrInfo(Long attrId) {
        AttrResponseVo vo = new AttrResponseVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, vo);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            // 1.设置分组信息
            AttrAttrgroupRelationEntity relationEntity = relationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",
                    attrId));
            if (relationEntity != null) {
                vo.setAttrGroupId(relationEntity.getAttrGroupId());
                AttrGroupEntity groupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
                if (groupEntity != null) {
                    vo.setGroupName(groupEntity.getAttrGroupName());
                }
            }
        }

        // 2.设置分类信息
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        vo.setCatelogPath(catelogPath);
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        if (categoryEntity != null) {
            vo.setCatelogName(categoryEntity.getName());
        }

        return vo;
    }

    @Override
    @Transactional
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            // 1.修改分组关联
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());

            int count = relationService.count(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            if (count > 0) {
                relationService.update(relationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attr.getAttrId()));
            } else {
                relationService.save(relationEntity);
            }
        }


    }

    /**
     * 根据分组id查找关联的所有基本属性
     *
     * @param attrgroupId
     * @return List<AttrEntity>
     **/
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> entities = relationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id",
                attrgroupId));

        List<Long> attrIds = entities.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);
        return (List<AttrEntity>) attrEntities;
    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        relationService.remove(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", 1).eq("attr_id", 1));
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map(item -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        relationService.deleteBatchRelation(entities);
    }

}