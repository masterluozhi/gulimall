package com.kun.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kun.common.utils.PageUtils;
import com.kun.common.utils.Query;
import com.kun.common.utils.R;
import com.kun.gulimall.product.controller.CategoryBrandRelationController;
import com.kun.gulimall.product.dao.BrandDao;
import com.kun.gulimall.product.entity.BrandEntity;
import com.kun.gulimall.product.service.BrandService;
import com.kun.gulimall.product.service.PmsCategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Autowired
    private PmsCategoryBrandRelationService relationService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id", key).or().like("name", key);
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void updateDatail(BrandEntity brand) {
        //保证冗余字段一样
        this.updateById(brand);
        if (StringUtils.hasText(brand.getName())){
            //同步更新
            relationService.updateBrnd(brand.getBrandId(),brand.getName());
        }


    }


}

