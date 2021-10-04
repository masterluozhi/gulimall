package com.kun.gulimall.product.service.impl;

import com.kun.common.constant.ProductConstant;
import com.kun.gulimall.product.entity.*;
import com.kun.gulimall.product.service.*;
import com.kun.gulimall.product.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kun.common.utils.PageUtils;
import com.kun.common.utils.Query;

import com.kun.gulimall.product.dao.SpuInfoDao;

import javax.annotation.Resource;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
@Resource
private SpuInfoDescService spuInfoDescService;
@Resource
    private SpuImagesService spuImagesService;
@Resource
private AttrService attrService;
@Resource
private ProductAttrValueService productAttrValueService;
@Resource
private  SkuInfoService skuInfoService;
@Resource
private SkuImagesService skuImagesService;
@Resource
private SkuSaleAttrValueService  skuSaleAttrValueService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSpuSaveVo(SpuSaveVo spuSaveVo) {
        //1保存spu基本 信息
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        baseMapper.insert(spuInfoEntity);
        //2，保存描述spu的图片
        List<String> decript = spuSaveVo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", decript));
        spuInfoDescService.save(spuInfoDescEntity);
        //3保存spu的图片集体
        List<String> images = spuSaveVo.getImages();
        if (images != null && images.size() > 0) {
            List<SpuImagesEntity> collect = images.stream().map(image -> {
                SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
                spuImagesEntity.setImgUrl(image);
                spuImagesEntity.setSpuId(spuInfoEntity.getId());
                return spuImagesEntity;
            }).collect(Collectors.toList());
            spuImagesService.saveBatch(collect);
        }

        // 四、保存spu的规格参数
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        if (baseAttrs != null && baseAttrs.size() > 0) {
            List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(baseAttr -> {
                ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
                productAttrValueEntity.setAttrId(baseAttr.getAttrId());
                productAttrValueEntity.setAttrValue(baseAttr.getAttrValues());
                productAttrValueEntity.setQuickShow(baseAttr.getShowDesc());
                AttrEntity attrEntity = attrService.getById(baseAttr.getAttrId());
                productAttrValueEntity.setAttrName(attrEntity.getAttrName());
                productAttrValueEntity.setSpuId(spuInfoEntity.getId());
                return productAttrValueEntity;
            }).collect(Collectors.toList());
            productAttrValueService.saveBatch(productAttrValueEntities);
        }
        // 五、保存spu的积分信息 gulimall_sms sms_spu_bounds
//        Bounds bounds = spuSaveVo.getBounds();
//        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
//        BeanUtils.copyProperties(bounds, spuBoundsTo);
//        spuBoundsTo.setSpuId(spuInfoEntity.getId());
//        R result1 = couponFeignService.saveSpuBounds(spuBoundsTo);
//        if (result1.getCode() != ProductConstant.AttrEnum.SUCCESS_FEIGN.getCode()) {
//            log.error("保存spu积分信息失败");
//        }
        // 六、保存spu的所有sku信息
        List<Skus> skus = spuSaveVo.getSkus();
        if (skus != null && skus.size() > 0) {
            // 获取默认图片路径
            skus.stream().forEach(sku -> {
                String default_img = "";
                for (Images image : sku.getImages()) {
                    if (image.getDefaultImg() == ProductConstant.AttrEnum.DEFAULT_IMG.getCode()) {
                        default_img = image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatelogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(default_img);
                // 1. sku基本信息 pms_sku_info
                skuInfoService.save(skuInfoEntity);
                // skuId
                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> imagesEntityList = sku.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    return skuImagesEntity;
                }).filter(item -> {
                    // 只保存有图片路径的
                    String imgUrl = item.getImgUrl();
                    return (!StringUtils.isEmpty(imgUrl));
                }).collect(Collectors.toList());
                // 2. sku的图片信息 pms_sku_images
                skuImagesService.saveBatch(imagesEntityList);
                List<Attr> attr = sku.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                // 3. sku的属性信息 pms_sku_sale_attr_value
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntityList);
                // 4. sku的优惠满减信息 gulimall_sms  sms_sku_full_reduction / sms_sku_ladder / sms_member_price
//              SkuReductionTo skuReductionTo = new SkuReductionTo();
//               BeanUtils.copyProperties(sku, skuReductionTo);
//               skuReductionTo.setSkuId(skuId);
//                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
//                  R result2 = couponFeignService.saveSkuReduction(skuReductionTo);
//                   if (result2.getCode() != ProductConstant.AttrEnum.SUCCESS_FEIGN.getCode()) {
//                     log.error("远程的sku优惠满减信息保存失败");
//                   }
//                }
            });
//           }

        }
    }
}