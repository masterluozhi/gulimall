package com.kun.gulimall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kun.common.utils.PageUtils;
import com.kun.common.utils.R;
import com.kun.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.kun.gulimall.product.entity.AttrEntity;
import com.kun.gulimall.product.entity.AttrGroupEntity;
import com.kun.gulimall.product.entity.CategoryEntity;
import com.kun.gulimall.product.service.AttrGroupService;

import com.kun.gulimall.product.service.CategoryService;
import com.kun.gulimall.product.vo.AttrRelDelVo;
import com.kun.gulimall.product.vo.AttrVo;
import com.kun.gulimall.product.vo.CAttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 属性分组
 *
 * @author masterLuo
 * @email masterluozhi@gmail.com
 * @date 2021-09-13 22:40:14
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    /**
     * 列表
     */
    @RequestMapping("/list")

    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);

        return R.ok().put("page", page);
    }
    @RequestMapping("/list/{catId}")

    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable Long catId){

       // PageUtils page = attrGroupService.queryPage(params);
        PageUtils page =   attrGroupService.queryPage(params,catId);
        return R.ok().put("page", page);
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")

    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long[] CatelogPath=   categoryService.findCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(CatelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")

    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")

    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }
    @GetMapping("{attrgroupId}/attr/relation")
    public  R attrRelation(@PathVariable long attrgroupId ){
       List<AttrEntity> entities = attrGroupService.geRelationAttr(attrgroupId);
        return  R.ok().put("data",entities);
    }
    @PostMapping("/attr/relation/delete")
    public  R attrRelationDelete(@RequestBody AttrRelDelVo[] delVo ){
              attrGroupService.attrRelationDelete(delVo);
              return R.ok();
    }
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R getNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                           @RequestParam Map<String, Object> params) {
        PageUtils pageUtils = attrGroupService.getNoRelation(attrgroupId, params);
        return R.ok().put("page", pageUtils);
    }
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrRelDelVo> AttrRelDelVos){
            attrGroupService.addRelation(AttrRelDelVos);
        return  R.ok();
    }
    @GetMapping("/{catelogId}/withattr")
    public R catelogIdWithattr(@PathVariable Long catelogId ){
    List<CAttrVo>   cAttrVos=  attrGroupService.catelogIdWithattr(catelogId );
    return  R.ok().put("data",cAttrVos);
    }
}
