package com.kun.gulimall.coupon.dao;

import com.kun.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author masterluo
 * @email masterluozhi@gmail.com
 * @date 2021-09-14 10:14:58
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
