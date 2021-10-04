package com.kun.gulimall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kun.common.utils.PageUtils;
import com.kun.common.utils.R;

import com.kun.common.valid.SaveValid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.kun.gulimall.product.entity.BrandEntity;
import com.kun.gulimall.product.service.BrandService;


import javax.validation.Valid;


/**
 * 品牌
 *
 * @author masterLuo
 * @email masterluozhi@gmail.com
 * @date 2021-09-13 22:40:14
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")

    public R list(@RequestParam Map<String, Object> params){

        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")

    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")

    public R save(@Validated(value = SaveValid.class) @RequestBody BrandEntity brand){

		brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@RequestBody(required = false) BrandEntity brand){
        brandService.updateDatail(brand);//事务以免关系表不同步
        return R.ok();
    }
    @RequestMapping("/update/status")
    //@RequiresPermissions("product:brand:update")
    public R updateStatus(@RequestBody(required = false) BrandEntity brand){
        brandService.updateById(brand);
        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")

    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
