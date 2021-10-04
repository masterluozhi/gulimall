package com.kun.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.kun.common.utils.PageUtils;
import com.kun.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author masterLuo
 * @email masterluozhi@gmail.com
 * @date 2021-09-13 22:40:14
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    Long[] findCatelogPath(Long catelogId);

    void updateCategory(CategoryEntity category);
}

