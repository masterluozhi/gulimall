package com.kun.gulimall.product.controller;

import com.kun.gulimall.product.entity.CategoryEntity;
import com.kun.gulimall.product.service.CategoryService;

import com.kun.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 商品三级分类
 *
 * @author masterLuo
 * @email masterluozhi@gmail.com
 * @date 2021-09-13 22:40:14
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    //树形结构查出所有分类
    @RequestMapping("/list/tree")
    public R list(){
    List<CategoryEntity> list=categoryService.listWithTree();

        return R.ok().put("list",list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")

    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @PostMapping("/save")

    public R save(@RequestBody(required = false) CategoryEntity category){

		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")

    public R update(@RequestBody(required = false) CategoryEntity category){

		categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //TODO
    public R delete(@RequestBody Long[] catIds){
		categoryService.removeByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
