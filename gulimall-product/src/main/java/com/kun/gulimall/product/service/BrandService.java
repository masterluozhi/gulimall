package com.kun.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.kun.common.utils.PageUtils;
import com.kun.gulimall.product.entity.BrandEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 品牌
 *
 * @author masterLuo
 * @email masterluozhi@gmail.com
 * @date 2021-09-13 22:40:14
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDatail(BrandEntity brand);

    ;
}

