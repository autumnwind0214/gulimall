package com.example.gulimall.product.vo;

import lombok.Data;

/**
 * @author 黄智龙
 * @description TODO
 * @date 2024年04月23日
 * @version: 1.0
 */
@Data
public class AttrResponseVo extends AttrVo {

    private String catelogName;

    private String groupName;

    private Long[] catelogPath;
}
