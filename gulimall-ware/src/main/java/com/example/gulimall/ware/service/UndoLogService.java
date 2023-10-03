package com.example.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.gulimall.ware.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author AutumnWind
 * @email ${email}
 * @date 2023-10-03 00:06:04
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

