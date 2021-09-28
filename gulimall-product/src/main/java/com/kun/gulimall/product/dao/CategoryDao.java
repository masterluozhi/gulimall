package com.kun.gulimall.product.dao;

import com.kun.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author masterLuo
 * @email masterluozhi@gmail.com
 * @date 2021-09-13 22:40:14
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
