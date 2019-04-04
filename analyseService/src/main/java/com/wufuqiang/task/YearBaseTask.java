package com.wufuqiang.task;

import com.mongodb.Mongo;
import com.wufuqiang.entity.YearBase;
import com.wufuqiang.map.YearBaseMap;
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
public class YearBaseTask {

    public static void main(String[] args){
        final ParameterTool params = ParameterTool.fromArgs(args) ;
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment() ;
        env.getConfig().setGlobalJobParameters(params);

        DataSet<String> text = env.readTextFile(params.get("input")) ;

        DataSet<YearBase> mapResult = text.map(new YearBaseMap()) ;
        DataSet<YearBase> reduceResult = mapResult.groupBy("groupfield").reduce(new YearBaseReduce()) ;

        try {
            List<YearBase> resultList = reduceResult.collect() ;

            for(YearBase yearBase : resultList){
                String yeartype = yearBase.getYeartype() ;
                Long count = yearBase.getCount() ;
                Document doc = MongoUtils.findoneby("yearbasestatics","UserPortrait",yeartype) ;
                if(doc == null){
                    doc = new Document() ;
                    doc.put("info",yeartype) ;
                    doc.put("count",count) ;
                }else{
                    Long countpre = doc.getLong("count") ;
                    Long total = countpre + count ;
                    doc.put("count",total) ;
                }
                MongoUtils.saveorupdatemongo("yearbasestatics","UserPortrait",doc);

            }
            env.execute("year base analyse") ;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
