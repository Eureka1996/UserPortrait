package com.wufuqiang.logic;

import com.wufuqiang.entity.CarrierInfo;
import com.wufuqiang.map.CarrierMap;
import com.wufuqiang.reduce.CarrierReduce;
import com.wufuqiang.util.MongoUtils;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.bson.Document;

import java.util.List;

/**
 * @ author wufuqiang
 * @ date 2019/4/8/008 - 16:41
 **/
public class LogicTask {
    public static void main(String[] args) {
        final ParameterTool params = ParameterTool.fromArgs(args) ;
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment() ;
        env.getConfig().setGlobalJobParameters(params);

        DataSet<String> text = env.readTextFile(params.get("input")) ;

        DataSet<CarrierInfo> mapResult = text.map(new CarrierMap()) ;
        DataSet<CarrierInfo> reduceResult = mapResult.groupBy("groupfield").reduce(new CarrierReduce()) ;

        try {
            List<CarrierInfo> resultList = reduceResult.collect() ;
            for(CarrierInfo carrierInfo : resultList){
                String carrier = carrierInfo.getCarrier() ;
                Long count = carrierInfo.getCount() ;
                Document doc = MongoUtils.findoneby("carrierstatics","UserPortrait",carrier) ;
                if(doc == null){
                    doc = new Document() ;
                    doc.put("info",carrier) ;
                    doc.put("count",count) ;
                }else{
                    Long countpre = doc.getLong("count") ;
                    Long total = countpre + count ;
                    doc.put("count",total) ;
                }
                MongoUtils.saveorupdatemongo("carrierstatics","UserPortrait",doc);

            }
            env.execute("carrier analyse") ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
