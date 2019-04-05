package com.wufuqiang.task;

import com.wufuqiang.entity.EmailInfo;
import com.wufuqiang.entity.YearBase;
import com.wufuqiang.map.EmailMap;
import com.wufuqiang.map.YearBaseMap;
import com.wufuqiang.reduce.EmailReduce;
import com.wufuqiang.reduce.YearBaseReduce;
import com.wufuqiang.util.MongoUtils;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.bson.Document;

import java.util.List;

/**
 * @ author wufuqiang
 * @ date 2019/4/4/004 - 14:32
 **/
public class EmailTask {

    public static void main(String[] args){
        final ParameterTool params = ParameterTool.fromArgs(args) ;
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment() ;
        env.getConfig().setGlobalJobParameters(params);

        DataSet<String> text = env.readTextFile(params.get("input")) ;

        DataSet<EmailInfo> mapResult = text.map(new EmailMap()) ;
        DataSet<EmailInfo> reduceResult = mapResult.groupBy("groupfield").reduce(new EmailReduce()) ;

        try {
            List<EmailInfo> resultList = reduceResult.collect() ;

            for(EmailInfo emailInfo : resultList){
                String emailtype = emailInfo.getEmail() ;
                Long count = emailInfo.getCount() ;
                Document doc = MongoUtils.findoneby("emailstatics","UserPortrait",emailtype) ;
                if(doc == null){
                    doc = new Document() ;
                    doc.put("info",emailtype) ;
                    doc.put("count",count) ;
                }else{
                    Long countpre = doc.getLong("count") ;
                    Long total = countpre + count ;
                    doc.put("count",total) ;
                }
                MongoUtils.saveorupdatemongo("emailstatics","UserPortrait",doc);

            }
            env.execute("email analyse") ;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
