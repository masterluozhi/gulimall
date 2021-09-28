package com.kun.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kun.common.utils.PageUtils;
import com.kun.common.utils.Query;

import com.kun.gulimall.product.dao.CategoryDao;
import com.kun.gulimall.product.entity.CategoryEntity;
import com.kun.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //查出所有分类
        List<CategoryEntity> subjectList = baseMapper.selectList(null);
        //查出一级分类
        List<CategoryEntity> oneSubjectList = subjectList.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid()==0;
        }).map(menu -> {
            menu.setChildren(getChildren(menu, subjectList));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort()==null?0:menu1.getSort())-(menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return  oneSubjectList;
    }
	
    public  List<CategoryEntity>getChildren(CategoryEntity root, List<CategoryEntity> allSubjects ){
        List<CategoryEntity> children = allSubjects.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(root.getCatId());
        }).map(menu -> {
            menu.setChildren(getChildren(menu, allSubjects));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort()==null?0:menu1.getSort())-(menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }
}
