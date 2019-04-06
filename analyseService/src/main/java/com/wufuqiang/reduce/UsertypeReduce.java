package com.wufuqiang.reduce;

import com.wufuqiang.entity.UsertypeInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

/**
 * @ author wufuqiang
 * @ date 2019/4/6/006 - 16:30
 **/
public class UsertypeReduce implements ReduceFunction<UsertypeInfo> {
    @Override
    public UsertypeInfo reduce(UsertypeInfo usertypeInfo, UsertypeInfo t1) throws Exception {
        return new UsertypeInfo(usertypeInfo.getUsertype(),usertypeInfo.getCount()+t1.getCount(),usertypeInfo.getGroupbyfield());
    }
}
