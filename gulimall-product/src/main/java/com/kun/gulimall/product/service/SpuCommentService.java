package com.kun.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;


import com.kun.common.utils.PageUtils;
import com.kun.gulimall.product.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author masterLuo
 * @email masterluozhi@gmail.com
 * @date 2021-09-13 22:40:14
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

