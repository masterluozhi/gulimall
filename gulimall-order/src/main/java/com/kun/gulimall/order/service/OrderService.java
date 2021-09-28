package com.kun.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kun.common.utils.PageUtils;
import com.kun.gulimall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author master
 * @email masterluozhi@gmail.com
 * @date 2021-09-14 10:49:22
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

