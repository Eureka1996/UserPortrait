package com.wufuqiang.reduce;

import com.wufuqiang.entity.YearBase;
import org.apache.flink.api.common.functions.ReduceFunction;

/**
 * @ author wufuqiang
 * @ date 2019/4/4/004 - 15:59
 **/
public class YearBaseReduce implements ReduceFunction<YearBase> {

    @Override
    public YearBase reduce(YearBase yearBase, YearBase t1) throws Exception {

        return new YearBase(yearBase.getYeartype(),yearBase.getCount()+t1.getCount(),yearBase.getGroupfield());
    }
}
