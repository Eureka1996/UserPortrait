package com.wufuqiang.map;

import com.wufuqiang.entity.BaiJiaInfo;
import com.wufuqiang.entity.CarrierInfo;
import com.wufuqiang.util.CarrierUtils;
import com.wufuqiang.util.HBaseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author wufuqiang
 * @ date 2019/4/5/005 - 9:38
 **/
public class BaiJiaMap implements MapFunction<String,BaiJiaInfo> {
    @Override
    public BaiJiaInfo map(String str) throws Exception {
        if(StringUtils.isBlank(str)){
            return null ;
        }
        String[] orderinfos = str.split(",") ;
        String id = orderinfos[0] ;
        String productid = orderinfos[1] ;
        String producttypeid = orderinfos[2] ;
        String createtime = orderinfos[3] ;
        String amount = orderinfos[4] ;
        String paytype = orderinfos[5] ;
        String paytime = orderinfos[6] ;
        String paystatus = orderinfos[7] ;
        String couponamount = orderinfos[8] ;
        String totalamount = orderinfos[9] ;
        String refundamount = orderinfos[10] ;
        String num = orderinfos[11] ;
        String userid = orderinfos[12] ;
        BaiJiaInfo baijiaInfo = new BaiJiaInfo(userid,createtime,amount,paytype,paytime,paystatus,couponamount,totalamount,refundamount) ;

        baijiaInfo.setGroupfield("baijia=="+userid);
        List<BaiJiaInfo> list = new ArrayList<BaiJiaInfo>() ;
        list.add(baijiaInfo) ;
        baijiaInfo.setList(list);

        return baijiaInfo;
    }
}
