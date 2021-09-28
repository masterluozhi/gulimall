package com.kun.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kun.common.utils.PageUtils;
import com.kun.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author master
 * @email masterluozhi@gmail.com
 * @date 2021-09-14 10:35:38
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

