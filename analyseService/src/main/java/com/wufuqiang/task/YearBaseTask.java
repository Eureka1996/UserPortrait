package com.wufuqiang.task;

import com.wufuqiang.entity.YearBase;
import com.wufuqiang.map.YearBaseMap;
import com.wufuqiang.reduce.YearBaseReduce;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            env.execute() ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
