package com.example.gulimall.member.dao;

import com.example.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author AutumnWind
 * @email ${email}
 * @date 2023-10-02 23:59:10
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
