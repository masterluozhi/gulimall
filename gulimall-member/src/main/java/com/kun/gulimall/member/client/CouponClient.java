package com.kun.gulimall.member.client;

import com.kun.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-coupon")
@Component
public interface CouponClient {

    @RequestMapping("coupon/coupon/member/list")
    public R menberCoupon();
}
