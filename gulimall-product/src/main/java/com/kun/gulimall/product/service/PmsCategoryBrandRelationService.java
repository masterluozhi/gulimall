package com.kun.gulimall.product.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.kun.common.utils.PageUtils;
import com.kun.gulimall.product.entity.PmsCategoryBrandRelationEntity;
import com.kun.gulimall.product.vo.BrandVo;


import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author master
 * @email masterluozhi@gmail.com
 * @date 2021-09-30 15:48:06
 */
public interface PmsCategoryBrandRelationService extends IService<PmsCategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(PmsCategoryBrandRelationEntity pmsCategoryBrandRelation);

    void updateBrnd(Long brandId, String name);

    List<BrandVo> getBrandByCatId(Long catId);
}

