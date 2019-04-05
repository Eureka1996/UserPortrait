package com.wufuqiang.reduce;

import com.wufuqiang.entity.BaiJiaInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author wufuqiang
 * @ date 2019/4/5/005 - 10:20
 **/
public class BaiJiaReduce implements ReduceFunction<BaiJiaInfo> {
    @Override
    public BaiJiaInfo reduce(BaiJiaInfo baiJiaInfo, BaiJiaInfo t1) throws Exception {
        String userid = baiJiaInfo.getUsesrid() ;

        List<BaiJiaInfo> baijialist1 = baiJiaInfo.getList() ;
        List<BaiJiaInfo> baijialist2 = t1.getList() ;
        List<BaiJiaInfo> finallist = new ArrayList<BaiJiaInfo>() ;
        finallist.addAll(baijialist1) ;
        finallist.addAll(baijialist2) ;

        BaiJiaInfo baijiaInfoFinal = new BaiJiaInfo() ;
        baijiaInfoFinal.setList(finallist);
        baijiaInfoFinal.setUsesrid(userid);

        return baijiaInfoFinal;
    }
}
