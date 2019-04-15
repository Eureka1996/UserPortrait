package com.wufuqiang.logic;

import com.wufuqiang.entity.CarrierInfo;
import com.wufuqiang.map.CarrierMap;
import com.wufuqiang.map.LogicMap;
import com.wufuqiang.reduce.CarrierReduce;
import com.wufuqiang.reduce.LogicReduce;
import com.wufuqiang.util.MongoUtils;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.bson.Document;

import java.util.*;

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

        DataSet<LogicInfo> mapResult = text.map(new LogicMap()) ;
        DataSet<ArrayList<Double>> reduceResult = mapResult.groupBy("groupfield").reduceGroup(new LogicReduce()) ;

        try {
            List<ArrayList<Double>> resultList = reduceResult.collect() ;
            int groupsize = resultList.size() ;

            Map<Integer,Double> summap = new TreeMap<Integer,Double>(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });

            for(ArrayList<Double> array : resultList){

                for(Double dou:array){
                    for(int i=0;i<array.size() ;i++){
                        double pre = summap.get(i)==null?0d:summap.get(i);
                        summap.put(i,pre+array.get(i));
                    }
                }

            }

            ArrayList<Double> finalweight = new ArrayList<Double>();
            for(Map.Entry<Integer,Double> mapentry :summap.entrySet()){
                Integer key = mapentry.getKey();
                Double sumvalue = mapentry.getValue();
                double finalvalue = sumvalue/groupsize;
                finalweight.add(finalvalue);
            }

            env.execute("carrier analyse") ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
