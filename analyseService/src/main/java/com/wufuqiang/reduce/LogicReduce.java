package com.wufuqiang.reduce;

import com.wufuqiang.logic.CreateDataSet;
import com.wufuqiang.logic.LogicInfo;
import com.wufuqiang.logic.Logistic;
import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.Iterator;


public class LogicReduce implements GroupReduceFunction<LogicInfo,ArrayList<Double>> {
    @Override
    public void reduce(Iterable<LogicInfo> iterable, Collector<ArrayList<Double>> collector) throws Exception {
        Iterator<LogicInfo> iterator = iterable.iterator() ;
        CreateDataSet trainingSet = new CreateDataSet() ;
        while(iterator.hasNext()){
            LogicInfo logicInfo = iterator.next() ;
            String variable1 = logicInfo.getVariable1() ;
            String variable2 = logicInfo.getVariable2() ;
            String variable3 = logicInfo.getVariable3() ;
            String labase = logicInfo.getLabase() ;
            ArrayList<String> as = new ArrayList<String>() ;
            as.add(variable1) ;
            as.add(variable2) ;
            as.add(variable3) ;
            trainingSet.data.add(as) ;
            trainingSet.labels.add(labase) ;
        }
        ArrayList<Double> weights = new ArrayList<Double>() ;
        weights = Logistic.gradAscent1(trainingSet,trainingSet.labels,500) ;
        collector.collect(weights);

    }
}
