package com.kun.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kun.gulimall.product.entity.PmsCategoryBrandRelationEntity;
import com.kun.gulimall.product.service.PmsCategoryBrandRelationService;
import com.kun.gulimall.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.kun.common.utils.PageUtils;
import com.kun.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author master
 * @email masterluozhi@gmail.com
 * @date 2021-09-30 15:48:06
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private PmsCategoryBrandRelationService pmsCategoryBrandRelationService;

    /**
     * 列表
     */
    @GetMapping("/brands/list")
    public R brandsList(@RequestParam Long catId){
       List<BrandVo> brandVos= pmsCategoryBrandRelationService.getBrandByCatId(catId);
        return  R.ok().put("data",brandVos);
    }

    @GetMapping("/catelog/list")
    public R list(@RequestParam long brandId){
        QueryWrapper<PmsCategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id",brandId);
        List<PmsCategoryBrandRelationEntity> data = pmsCategoryBrandRelationService.list(wrapper);

        return R.ok().put("data", data);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")

    public R info(@PathVariable("id") Long id){
		PmsCategoryBrandRelationEntity pmsCategoryBrandRelation = pmsCategoryBrandRelationService.getById(id);

        return R.ok().put("pmsCategoryBrandRelation", pmsCategoryBrandRelation);
    }

    /**
     * 保存
     */
    @PostMapping("/save")

    public R save(@RequestBody PmsCategoryBrandRelationEntity pmsCategoryBrandRelation){
		pmsCategoryBrandRelationService.saveDetail(pmsCategoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")

    public R update(@RequestBody PmsCategoryBrandRelationEntity pmsCategoryBrandRelation){
		pmsCategoryBrandRelationService.updateById(pmsCategoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")

    public R delete(@RequestBody Long[] ids){
		pmsCategoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
