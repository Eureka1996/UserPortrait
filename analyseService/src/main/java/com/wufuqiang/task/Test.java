package com.wufuqiang.task;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;

/**
 * @ author wufuqiang
 * @ date 2019/4/4/004 - 14:20
 **/
public class Test {


    public static void main(String[] args){
        final ParameterTool params = ParameterTool.fromArgs(args) ;
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment() ;

        env.getConfig().setGlobalJobParameters(params);

        DataSet<String> text = env.readTextFile(params.get("info")) ;

        DataSet map = text.flatMap(null) ;
        DataSet reduce = map.groupBy(" ").reduce(null) ;

        try {
            env.execute() ;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
