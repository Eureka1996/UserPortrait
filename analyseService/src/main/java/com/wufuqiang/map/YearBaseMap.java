package com.wufuqiang.map;

import com.wufuqiang.entity.YearBase;
import com.wufuqiang.util.DateUtils;
import com.wufuqiang.util.HBaseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @ author wufuqiang
 * @ date 2019/4/4/004 - 14:42
 **/
public class YearBaseMap implements MapFunction<String,YearBase> {

    @Override
    public YearBase map(String str) throws Exception {
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

        String yearbasetype = DateUtils.getYearBaseByAge(age) ;
        String tablename = "userflaginfo" ;
        String rowkey = userid ;
        String familyname =  "baseinfo";
        String column = "yearbase" ;
        HBaseUtils.putdata(tablename,rowkey,familyname,column,yearbasetype);

        return new YearBase(yearbasetype,1L,"yearbase=="+yearbasetype);
    }
}
