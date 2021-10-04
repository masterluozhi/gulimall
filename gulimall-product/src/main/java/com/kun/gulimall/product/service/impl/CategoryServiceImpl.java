package com.kun.gulimall.product.service.impl;

import com.kun.gulimall.product.entity.PmsCategoryBrandRelationEntity;
import com.kun.gulimall.product.service.PmsCategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    private PmsCategoryBrandRelationService relationService;
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
//单个分类查询完整路劲
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);//集合工具类，把集合数据逆序
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    @Override
    public void updateCategory(CategoryEntity category) {
    baseMapper.updateById(category);
        PmsCategoryBrandRelationEntity entity = new PmsCategoryBrandRelationEntity();
        entity.setCatelogId(category.getCatId());
        entity.setCatelogName(category.getName());
        if (StringUtils.hasText(category.getName())){
            relationService.update(entity,new QueryWrapper< PmsCategoryBrandRelationEntity>().eq("catelog_id",category.getCatId()));

    }
    }

    private List<Long> findParentPath(Long catelogId,  List<Long> paths){
     paths.add(catelogId);
     CategoryEntity byId = this.getById(catelogId);

     if (byId.getParentCid()!=0){
         findParentPath(byId.getParentCid(), paths);
     }
     return paths;
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
