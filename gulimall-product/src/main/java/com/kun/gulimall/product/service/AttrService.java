package com.kun.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.kun.common.utils.PageUtils;
import com.kun.gulimall.product.entity.AttrEntity;
import com.kun.gulimall.product.vo.AttrRespVo;
import com.kun.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author masterLuo
 * @email masterluozhi@gmail.com
 * @date 2021-09-13 22:40:14
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttrVo(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String,Object> params, long catelogId,String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttrVo(AttrVo attr);


}

