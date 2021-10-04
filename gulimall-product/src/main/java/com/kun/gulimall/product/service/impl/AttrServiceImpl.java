package com.kun.gulimall.product.service.impl;

import com.kun.common.constant.ProductConstant;
import com.kun.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.kun.gulimall.product.dao.AttrGroupDao;
import com.kun.gulimall.product.dao.CategoryDao;
import com.kun.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.kun.gulimall.product.entity.AttrGroupEntity;
import com.kun.gulimall.product.entity.CategoryEntity;
import com.kun.gulimall.product.vo.AttrRespVo;
import com.kun.gulimall.product.vo.AttrVo;
import org.apache.commons.lang3.ObjectUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kun.common.utils.PageUtils;
import com.kun.common.utils.Query;

import com.kun.gulimall.product.dao.AttrDao;
import com.kun.gulimall.product.entity.AttrEntity;
import com.kun.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Resource
    private AttrAttrgroupRelationDao relationDao;
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private AttrGroupDao attrGroupDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttrVo(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        //保存基本关系
        baseMapper.insert(attrEntity);

        //添加组关系
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationDao.insert(relationEntity);
        }
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, long catelogId,String type) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>();
        if (catelogId != 0) {
            wrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");

        if (StringUtils.hasText(key)) {
            wrapper.and((obj) -> {
                return wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        wrapper.eq("attr_type", "base".equalsIgnoreCase(type) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper);
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> attrResVoList = records.stream().map(attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //设置分组和分类名字
            AttrAttrgroupRelationEntity attr_id = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            if ("base".equalsIgnoreCase(type)){
                if (attr_id != null &&attr_id.getAttrGroupId()!=null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attr_id.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(attrResVoList);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrVo = new AttrRespVo();
        ArrayList<Long> longs = new ArrayList<>();
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        BeanUtils.copyProperties(attrEntity, attrVo);
        Long catelogId = attrEntity.getCatelogId();
        List<Long> parent = this.getParent(longs, catelogId);
        //设置分组和分类名字
        AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id", attrEntity.getAttrId()));
        // 将attrGroupId 和 GroupName 返回
        if (relationEntity != null) {
            attrVo.setAttrGroupId(relationEntity.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
            if (attrGroupEntity != null) {
                attrVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }
        CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
        if (categoryEntity != null) {
            attrVo.setCatelogName(categoryEntity.getName());
        }
        Collections.reverse(parent);


        attrVo.setCatelogPath(parent.toArray(new Long[parent.size()]));


        return attrVo;
    }

    @Override
    public void updateAttrVo(AttrVo attr) {
        // 修改基本属性
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        baseMapper.updateById(attrEntity);
        // 修改分组关联
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            Integer count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attr.getAttrId()));
            if (count > 0) {
                relationDao.update(relationEntity, new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attrEntity.getAttrId()));
            } else {
                relationEntity.setAttrId(attr.getAttrId());
                relationDao.insert(relationEntity);
            }
        }
    }

    private List<Long> getParent(List<Long> longs, Long catelogId) {
        longs.add(catelogId);
        CategoryEntity cat_id = categoryDao.selectById(catelogId);
        if (cat_id.getParentCid() != 0) {
            this.getParent(longs,cat_id.getParentCid());
        }
        return longs;
    }
}