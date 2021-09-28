package com.kun.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kun.common.utils.PageUtils;
import com.kun.gulimall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author master
 * @email masterluozhi@gmail.com
 * @date 2021-09-14 10:50:55
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

