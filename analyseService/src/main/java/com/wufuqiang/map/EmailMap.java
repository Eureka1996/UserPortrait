package com.wufuqiang.map;

import com.wufuqiang.entity.EmailInfo;
import com.wufuqiang.entity.YearBase;
import com.wufuqiang.util.DateUtils;
import com.wufuqiang.util.EmailUtils;
import com.wufuqiang.util.HBaseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @ author wufuqiang
 * @ date 2019/4/4/004 - 14:42
 **/
public class EmailMap implements MapFunction<String,EmailInfo> {

    @Override
    public EmailInfo map(String str) throws Exception {
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

        String emailtype = EmailUtils.getEmailtypeBy(email);
        String tablename = "userflaginfo" ;
        String rowkey = userid ;
        String familyname =  "baseinfo";
        String column = "emailinfo" ;
        HBaseUtils.putdata(tablename,rowkey,familyname,column,emailtype);

        return new EmailInfo(emailtype,1L,"emailtype=="+emailtype);
    }
}
