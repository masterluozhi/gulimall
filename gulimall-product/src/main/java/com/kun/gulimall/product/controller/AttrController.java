package com.kun.gulimall.product.controller;


import com.kun.gulimall.product.entity.AttrEntity;
import com.kun.gulimall.product.service.AttrService;
import com.kun.common.utils.PageUtils;
import com.kun.common.utils.R;
import com.kun.gulimall.product.vo.AttrRespVo;
import com.kun.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 商品属性
 *
 * @author masterLuo
 * @email masterluozhi@gmail.com
 * @date 2021-09-13 22:40:14
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
 @GetMapping("/{attrType}/list/{catelogId}")
 public R list(@RequestParam Map<String,Object> params,
               @PathVariable("catelogId") long catelogId,
               @PathVariable("attrType")String type){
     PageUtils page =  attrService.queryBaseAttrPage(params,catelogId,type);
     return R.ok().put("page", page);
 }
    /**
     * 列表
     */
    @RequestMapping("/list")

    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")

    public R info(@PathVariable("attrId") Long attrId){
        AttrRespVo attrInfo = attrService.getAttrInfo(attrId);

        return R.ok().put("attr",  attrInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")

    public R save(@RequestBody AttrVo attr){
		attrService.saveAttrVo(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")

    public R update(@RequestBody AttrVo attr){
		attrService.updateAttrVo(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")

    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
