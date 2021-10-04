package com.kun;

import com.kun.gulimall.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class GulimallProductApplicationTests {
@Autowired
private CategoryService categoryService;
    @Test
    void contextLoads() {
        Long[] catelogPath = categoryService.findCatelogPath((long) 982);
        System.out.println("sb");
        System.out.println(Arrays.asList(catelogPath));
    }

}
