package com.kun.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.kun.common.utils.PageUtils;
import com.kun.gulimall.product.entity.AttrEntity;
import com.kun.gulimall.product.entity.AttrGroupEntity;
import com.kun.gulimall.product.vo.AttrRelDelVo;
import com.kun.gulimall.product.vo.CAttrVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author masterLuo
 * @email masterluozhi@gmail.com
 * @date 2021-09-13 22:40:14
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String,Object> params, Long catId);


    List<AttrEntity> geRelationAttr(long attrgroupId);

    void attrRelationDelete(AttrRelDelVo[] delVo);

    PageUtils getNoRelation(Long attrgroupId, Map<String,Object> params);

    void addRelation(List<AttrRelDelVo> AttrRelDelVos);

    List<CAttrVo> catelogIdWithattr(Long catelogId);
}

