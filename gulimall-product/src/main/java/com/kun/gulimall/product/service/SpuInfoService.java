package com.kun.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.kun.common.utils.PageUtils;
import com.kun.gulimall.product.entity.SpuInfoEntity;
import com.kun.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author masterLuo
 * @email masterluozhi@gmail.com
 * @date 2021-09-13 22:40:14
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuSaveVo(SpuSaveVo spuSaveVo);
}

