package com.wufuqiang.reduce;

import com.wufuqiang.entity.CarrierInfo;
import com.wufuqiang.entity.EmailInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

/**
 * @ author wufuqiang
 * @ date 2019/4/4/004 - 20:21
 **/
public class EmailReduce implements ReduceFunction<EmailInfo> {

    @Override
    public EmailInfo reduce(EmailInfo emailInfo, EmailInfo t1) throws Exception {
        return new EmailInfo(emailInfo.getEmail(),emailInfo.getCount()+t1.getCount(),emailInfo.getGroupfield());
    }

}
