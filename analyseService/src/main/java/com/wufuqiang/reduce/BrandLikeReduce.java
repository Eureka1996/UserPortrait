package com.wufuqiang.reduce;

import com.wufuqiang.entity.BrandLike;
import org.apache.flink.api.common.functions.ReduceFunction;

/**
 * @ author wufuqiang
 * @ date 2019/4/6/006 - 14:50
 **/
public class BrandLikeReduce implements ReduceFunction<BrandLike> {
    @Override
    public BrandLike reduce(BrandLike brandLike, BrandLike t1) throws Exception {
        return null;
    }
}
