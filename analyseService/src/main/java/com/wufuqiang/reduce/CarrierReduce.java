package com.wufuqiang.reduce;

import com.wufuqiang.entity.CarrierInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

/**
 * @ author wufuqiang
 * @ date 2019/4/4/004 - 20:21
 **/
public class CarrierReduce implements ReduceFunction<CarrierInfo> {
    @Override
    public CarrierInfo reduce(CarrierInfo carrierInfo, CarrierInfo t1) throws Exception {
        return new CarrierInfo(carrierInfo.getCarrier(),carrierInfo.getCount()+t1.getCount(),carrierInfo.getGroupfield());
    }
}
