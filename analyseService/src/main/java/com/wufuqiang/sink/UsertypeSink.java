package com.wufuqiang.sink;

import com.wufuqiang.entity.BrandLike;
import com.wufuqiang.entity.UsertypeInfo;
import com.wufuqiang.util.MongoUtils;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.bson.Document;

/**
 * @ author wufuqiang
 * @ date 2019/4/6/006 - 16:16
 **/
public class UsertypeSink implements SinkFunction<UsertypeInfo> {
    @Override
    public void invoke(UsertypeInfo value, Context context) throws Exception {
        String usertype = value.getUsertype();
        long count = value.getCount();
        Document doc = MongoUtils.findoneby("usertypestatics","UserPortrait",usertype);
        if(doc == null){
            doc = new Document();
            doc.put("info",usertype);
            doc.put("count",count);
        }else{
            Long countpre = doc.getLong("count");
            Long total = countpre+count;
            doc.put("count",total);
        }
        MongoUtils.saveorupdatemongo("usertypestatics","UserPortrait",doc);
    }
}
