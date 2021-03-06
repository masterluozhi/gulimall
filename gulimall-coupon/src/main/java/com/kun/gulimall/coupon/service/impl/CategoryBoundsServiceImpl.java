package com.kun.gulimall.coupon.service.impl;

import com.kun.common.utils.Query;
import com.kun.gulimall.coupon.dao.CategoryBoundsDao;
import com.kun.gulimall.coupon.entity.CategoryBoundsEntity;
import com.kun.gulimall.coupon.service.CategoryBoundsService;
import com.kun.common.utils.PageUtils;
import com.kun.common.utils.R;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("categoryBoundsService")
public class CategoryBoundsServiceImpl extends ServiceImpl<CategoryBoundsDao, CategoryBoundsEntity> implements CategoryBoundsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBoundsEntity> page = this.page(
                new Query<CategoryBoundsEntity>().getPage(params),
                new QueryWrapper<CategoryBoundsEntity>()
        );

        return new PageUtils(page);
    }

}