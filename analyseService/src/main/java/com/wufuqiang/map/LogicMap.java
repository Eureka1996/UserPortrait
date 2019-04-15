package com.wufuqiang.map;

import com.wufuqiang.entity.CarrierInfo;
import com.wufuqiang.logic.LogicInfo;
import com.wufuqiang.util.CarrierUtils;
import com.wufuqiang.util.HBaseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

import java.util.Random;

/**
 * @ author wufuqiang
 * @ date 2019/4/4/004 - 19:46
 **/
public class LogicMap implements MapFunction<String,LogicInfo> {
    @Override
    public LogicInfo map(String str) throws Exception {

        if(StringUtils.isBlank(str)){
            return null ;
        }

        Random random = new Random() ;
        String[] temps = str.split(",") ;
        String variable1 = temps[0] ;
        String variable2 = temps[1] ;
        String variable3 = temps[2] ;
        String labase = temps[3] ;

        return new LogicInfo(variable1,variable2,variable3,labase,"logic=="+random.nextInt(10));
    }
}
