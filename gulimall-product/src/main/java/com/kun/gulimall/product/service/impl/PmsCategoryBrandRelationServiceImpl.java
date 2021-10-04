package com.kun.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kun.common.utils.PageUtils;
import com.kun.common.utils.Query;
import com.kun.common.utils.R;
import com.kun.gulimall.product.dao.AttrGroupDao;
import com.kun.gulimall.product.dao.BrandDao;
import com.kun.gulimall.product.dao.CategoryDao;
import com.kun.gulimall.product.dao.PmsCategoryBrandRelationDao;
import com.kun.gulimall.product.entity.AttrGroupEntity;
import com.kun.gulimall.product.entity.BrandEntity;
import com.kun.gulimall.product.entity.CategoryEntity;
import com.kun.gulimall.product.entity.PmsCategoryBrandRelationEntity;
import com.kun.gulimall.product.service.PmsCategoryBrandRelationService;
import com.kun.gulimall.product.vo.BrandVo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("pmsCategoryBrandRelationService")
public class PmsCategoryBrandRelationServiceImpl extends ServiceImpl<PmsCategoryBrandRelationDao, PmsCategoryBrandRelationEntity> implements PmsCategoryBrandRelationService {
@Resource
private BrandDao brandDao;
    @Resource
    private CategoryDao CategoryDao;
    @Resource
    private AttrGroupDao attrGroupDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsCategoryBrandRelationEntity> page = this.page(
                new Query<PmsCategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<PmsCategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(PmsCategoryBrandRelationEntity pmsCategoryBrandRelation) {
        Long brandId = pmsCategoryBrandRelation.getBrandId();
        Long catelogId = pmsCategoryBrandRelation.getCatelogId();
        CategoryEntity categoryEntity = CategoryDao.selectById(catelogId );
        BrandEntity brandEntity = brandDao.selectById(brandId);
        pmsCategoryBrandRelation.setBrandName(brandEntity.getName());
        pmsCategoryBrandRelation.setCatelogName(categoryEntity.getName());

        baseMapper.insert(pmsCategoryBrandRelation);

    }

    @Override
    public void updateBrnd(Long brandId, String name) {
        PmsCategoryBrandRelationEntity pmsCategoryBrandRelationEntity = new PmsCategoryBrandRelationEntity();
        pmsCategoryBrandRelationEntity.setBrandId(brandId);
        pmsCategoryBrandRelationEntity.setBrandName(name);
        baseMapper.update(pmsCategoryBrandRelationEntity,new QueryWrapper<PmsCategoryBrandRelationEntity>().eq("brand_id",brandId));
    }

    @Override
    public List<BrandVo> getBrandByCatId(Long catId) {
        List<PmsCategoryBrandRelationEntity> catelog_id = baseMapper.selectList(new QueryWrapper<PmsCategoryBrandRelationEntity>().eq("catelog_id", catId));
          List<BrandVo> brandVos   =catelog_id.stream().map(obj->{
                 BrandVo brandVo = new BrandVo();
                 brandVo.setBrandName( obj.getBrandName());
                 brandVo.setBrandId(obj.getBrandId());
                 return brandVo;
             }).collect(Collectors.toList());
          return brandVos;
    }

}