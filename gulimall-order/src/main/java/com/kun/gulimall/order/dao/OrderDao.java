package com.kun.gulimall.order.dao;

import com.kun.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author master
 * @email masterluozhi@gmail.com
 * @date 2021-09-14 10:49:22
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
