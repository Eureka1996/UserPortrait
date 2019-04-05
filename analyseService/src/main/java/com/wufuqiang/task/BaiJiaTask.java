package com.wufuqiang.task;

import com.wufuqiang.entity.BaiJiaInfo;
import com.wufuqiang.map.BaiJiaMap;
import com.wufuqiang.reduce.BaiJiaReduce;
import com.wufuqiang.util.MongoUtils;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.bson.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @ author wufuqiang
 * @ date 2019/4/5/005 - 9:39
 **/
public class BaiJiaTask {
    public static void main(String[] args){
        final ParameterTool params = ParameterTool.fromArgs(args) ;
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment() ;
        env.getConfig().setGlobalJobParameters(params);

        DataSet<String> text = env.readTextFile(params.get("input")) ;

        DataSet<BaiJiaInfo> mapResult = text.map(new BaiJiaMap()) ;
        DataSet<BaiJiaInfo> reduceResult = mapResult.groupBy("groupfield").reduce(new BaiJiaReduce()) ;

        try {
            List<BaiJiaInfo> resultList = reduceResult.collect() ;

            for(BaiJiaInfo baijiaInfo : resultList){
                String carrier = baijiaInfo.getUsesrid() ;
                List<BaiJiaInfo> list =  baijiaInfo.getList() ;
                Collections.sort(list, new Comparator<BaiJiaInfo>() {
                    @Override
                    public int compare(BaiJiaInfo o1, BaiJiaInfo o2) {
                        String time1 = o1.getCreatetime() ;
                        String time2 = o2.getCreatetime() ;
                        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd hhmmss") ;

                        Date date1 = new Date() ;
                        Date date2 = date1 ;
                        try {
                            date2 = dateFormat.parse(time2) ;
                            date1 = dateFormat.parse(time1) ;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        return date1.compareTo(date2) ;
                    }
                }) ;

                BaiJiaInfo before = null ;

                for(BaiJiaInfo baijiaInfoItem : list){
                    if(before == null){
                        before = baijiaInfoItem ;
                        continue ;
                    }

                }


            }
            env.execute("baijia analyse") ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
