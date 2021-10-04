package com.kun.gulimall.product.service.impl;

import com.kun.common.utils.R;
import com.kun.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.kun.gulimall.product.dao.AttrDao;
import com.kun.gulimall.product.dao.CategoryDao;
import com.kun.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.kun.gulimall.product.entity.AttrEntity;
import com.kun.gulimall.product.entity.CategoryEntity;
import com.kun.gulimall.product.service.AttrAttrgroupRelationService;
import com.kun.gulimall.product.service.AttrService;
import com.kun.gulimall.product.vo.AttrRelDelVo;
import com.kun.gulimall.product.vo.CAttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kun.common.utils.PageUtils;
import com.kun.common.utils.Query;

import com.kun.gulimall.product.dao.AttrGroupDao;
import com.kun.gulimall.product.entity.AttrGroupEntity;
import com.kun.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;
import org.w3c.dom.Attr;

import javax.annotation.Resource;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private AttrAttrgroupRelationDao relationDao;
    @Resource
    private AttrAttrgroupRelationService relationService;
    @Resource
    private AttrDao attrDao;
    @Resource
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((obj) -> {
                return obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        if (catId == 0) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        } else {
            wrapper.eq("catelog_id", catId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        }
    }

    @Override
    public List<AttrEntity> geRelationAttr(long attrgroupId) {
        List<AttrAttrgroupRelationEntity> attr_id = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> attrIds = attr_id.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        if (attrIds == null || attr_id.size() == 0) {
            return null;
        }
        List<AttrEntity> entities = attrDao.selectBatchIds(attrIds);
        return entities;
    }

    @Override
    public void attrRelationDelete(AttrRelDelVo[] delVo) {
        List<AttrAttrgroupRelationEntity> entityList = Arrays.stream(delVo).map((item) -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, attrAttrgroupRelationEntity);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        relationDao.deleteBathRelations(entityList);
    }

    @Override
    public PageUtils getNoRelation(Long attrgroupId, Map<String, Object> params) {
        // 只能是当前分类
        AttrGroupEntity attrGroupEntity = baseMapper.selectById(attrgroupId);

        PageUtils pageUtils = null;
        if (attrGroupEntity != null) {
            // 没有被其他引用(查询当前分类下的其他分组)
            List<AttrGroupEntity> groupEntityList = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>()
                    .eq("catelog_id", attrGroupEntity.getCatelogId()));
            List<Long> groupIdList = groupEntityList.stream().map((item) -> {
                return item.getAttrGroupId();
            }).collect(Collectors.toList());
            QueryWrapper<AttrAttrgroupRelationEntity> wrapper1 = new QueryWrapper<>();
            if (groupIdList != null && groupEntityList.size() > 0) {
                wrapper1.in("attr_group_id", groupIdList);
            }
            // 查询这些分组关联的属性
            List<AttrAttrgroupRelationEntity> relationEntityList = relationDao.selectList(wrapper1);
            List<Long> attrIdList = relationEntityList.stream().map((item) -> {
                return item.getAttrId();
            }).collect(Collectors.toList());

            // 获取未被关联的属性的条件
            QueryWrapper<AttrEntity> wrapper2 = new QueryWrapper<AttrEntity>()
                    .eq("catelog_id", attrGroupEntity.getCatelogId());
            if (attrIdList != null && attrIdList.size() > 0) {
                wrapper2.notIn("attr_id", attrIdList);
            }
            String key = (String) params.get("key");
            if (!StringUtils.isEmpty(key)) {
                wrapper2.and((w) -> {
                  return   w.eq("attr_id", key).or().like("attr_name", key);
                });
            }
            IPage<AttrEntity> page = attrService.page(new Query<AttrEntity>().getPage(params), wrapper2);
            pageUtils = new PageUtils(page);
        }
        return pageUtils;
    }

    @Override
    public void addRelation(List<AttrRelDelVo> AttrRelDelVos) {
     List<AttrAttrgroupRelationEntity> relationEntityList =AttrRelDelVos.stream().map(AttrRelDelVo->{
            AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(AttrRelDelVo,relation);
            return  relation;
        }).collect(Collectors.toList());
        relationService.saveBatch(relationEntityList);
    }

    @Override
    public List<CAttrVo> catelogIdWithattr(Long catelogId) {
        //写的脑疼
          List<AttrGroupEntity> groupEntitys = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
          List<CAttrVo> cAttrVos= groupEntitys.stream().map(attrGroupEntity->{
          List<AttrAttrgroupRelationEntity> relationEntityList = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupEntity.getAttrGroupId()));
          CAttrVo cAttrVo = new CAttrVo();
          BeanUtils.copyProperties(attrGroupEntity,cAttrVo);
          List<AttrEntity> attrEntities= new ArrayList<>();
          for (int i = 0; i <relationEntityList.size() ; i++) {
              Long attrId = relationEntityList.get(i).getAttrId();
              AttrEntity byId = attrService.getById(attrId);
             attrEntities.add(byId);
          }
           cAttrVo.setAttrs(attrEntities);
            return  cAttrVo;
        }).collect(Collectors.toList());

      return  cAttrVos;
    }
}

