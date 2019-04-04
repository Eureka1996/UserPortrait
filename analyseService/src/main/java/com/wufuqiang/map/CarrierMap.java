package com.wufuqiang.map;

import com.wufuqiang.entity.CarrierInfo;
import com.wufuqiang.entity.YearBase;
import com.wufuqiang.util.CarrierUtils;
import com.wufuqiang.util.DateUtils;
import com.wufuqiang.util.HBaseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @ author wufuqiang
 * @ date 2019/4/4/004 - 19:46
 **/
public class CarrierMap implements MapFunction<String,CarrierInfo> {
    @Override
    public CarrierInfo map(String str) throws Exception {

        if(StringUtils.isBlank(str)){
            return null ;
        }
        String[] infos = str.split(",") ;
        String userid = infos[0] ;
        String username = infos[1] ;
        String sex = infos[2] ;
        String telphone = infos[3] ;
        String email = infos[4] ;
        String age = infos[5] ;
        String registerTime = infos[6] ;
        String usetype = infos[7] ;

        Integer carriertype = CarrierUtils.getCarrierByTel(telphone);
        String carrier = carriertype == 0 ? "未知":carriertype==1?"移动用户":carriertype==2?"联通用户":"电信用户" ;
        String tablename = "userflaginfo" ;
        String rowkey = userid ;
        String familyname =  "baseinfo";
        String column = "yearbase" ;
        HBaseUtils.putdata(tablename,rowkey,familyname,column,carrier);

        return new CarrierInfo(carrier,1L,"yearbase=="+carrier);
    }
}
