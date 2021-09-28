package com.kun.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.kun.common.utils.PageUtils;
import com.kun.gulimall.coupon.entity.CouponHistoryEntity;


import java.util.Map;

/**
 * 优惠券领取历史记录
 *
 * @author masterluo
 * @email masterluozhi@gmail.com
 * @date 2021-09-14 10:14:58
 */
public interface CouponHistoryService extends IService<CouponHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

