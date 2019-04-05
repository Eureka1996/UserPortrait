package com.wufuqiang.task;

import com.wufuqiang.entity.BaiJiaInfo;
import com.wufuqiang.map.BaiJiaMap;
import com.wufuqiang.reduce.BaiJiaReduce;
import com.wufuqiang.util.DateUtils;
import com.wufuqiang.util.HBaseUtils;
import com.wufuqiang.util.MongoUtils;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.bson.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
                String userid = baijiaInfo.getUsesrid() ;
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
                Map<Integer , Integer> frequencymap = new HashMap<Integer,Integer>() ;
                double maxamount = 0.0d ;
                double sum = 0d ;

                for(BaiJiaInfo baijiaInfoItem : list){
                    if(before == null){
                        before = baijiaInfoItem ;
                        maxamount = Double.valueOf(before.getTotalamount()) ;
                        sum += maxamount ;
                        continue ;
                    }
                    // 计算购买的频率
                    String beforetime = before.getCreatetime() ;
                    String endstime = baijiaInfoItem.getCreatetime() ;
                    int days = DateUtils.getDaysBetweenbyStartAndend(beforetime,endstime,"yyyyMMdd hhmmss") ;
                    int beforenum = frequencymap.get(days) == null ? 0 : frequencymap.get(days) ;
                    frequencymap.put(days,beforenum+1) ;

                    //计算最大金额
                    Double totalamount = Double.valueOf(baijiaInfoItem.getTotalamount()) ;
                    if(totalamount > maxamount){
                        maxamount = totalamount ;
                    }


                    sum+=totalamount ;
                    before = baijiaInfoItem ;
                }

                //平均每次的消费金额
                double avramount = sum /list.size() ;

                //总天数
                int totaldays = 0 ;
                for(Map.Entry<Integer,Integer> entry : frequencymap.entrySet()){
                    Integer frequencydays = entry.getKey() ;
                    Integer count = entry.getValue() ;
                    totaldays += frequencydays* count ;

                }

                //平均多少天消费一次
                int avrdays = totaldays/list.size() ;

                // 败家指数 = 支付金额平均值*0.3 + 最大支付金额*0.3 + 下单频率*0.4
                // 支付金额平均值30分（0-20 5 20-60 10 60-100 20 100-150 30 150-200 40 200-250 60 250-350 70 350-450 80 450-600 90 600以上 100  ）
                // 最大支付金额30分（0-20 5 20-60 10 60-200 30 200-500 60 500-700 80 700 100）
                // 下单平率30分 （0-5 100 5-10 90 10-30 70 30-60 60 60-80 40 80-100 20 100以上的 10）
                int avraoumtsoce = 0;
                if(avramount>=0 && avramount < 20){
                    avraoumtsoce = 5;
                }else if (avramount>=20 && avramount < 60){
                    avraoumtsoce = 10;
                }else if (avramount>=60 && avramount < 100){
                    avraoumtsoce = 20;
                }else if (avramount>=100 && avramount < 150){
                    avraoumtsoce = 30;
                }else if (avramount>=150 && avramount < 200){
                    avraoumtsoce = 40;
                }else if (avramount>=200 && avramount < 250){
                    avraoumtsoce = 60;
                }else if (avramount>=250 && avramount < 350){
                    avraoumtsoce = 70;
                }else if (avramount>=350 && avramount < 450){
                    avraoumtsoce = 80;
                }else if (avramount>=450 && avramount < 600){
                    avraoumtsoce = 90;
                }else if (avramount>=600){
                    avraoumtsoce = 100;
                }

                int maxaoumtscore = 0;
                if(maxamount>=0 && maxamount < 20){
                    maxaoumtscore = 5;
                }else if (maxamount>=20 && maxamount < 60){
                    maxaoumtscore = 10;
                }else if (maxamount>=60 && maxamount < 200){
                    maxaoumtscore = 30;
                }else if (maxamount>=200 &&maxamount < 500){
                    maxaoumtscore = 60;
                }else if (maxamount>=500 && maxamount < 700){
                    maxaoumtscore = 80;
                }else if (maxamount>=700){
                    maxaoumtscore = 100;
                }

                // 下单平率30分 （0-5 100 5-10 90 10-30 70 30-60 60 60-80 40 80-100 20 100以上的 10）
                int avrdaysscore = 0;
                if(avrdays>=0 && avrdays < 5){
                    avrdaysscore = 100;
                }else if (avramount>=5 && avramount < 10){
                    avrdaysscore = 90;
                }else if (avramount>=10 && avramount < 30){
                    avrdaysscore = 70;
                }else if (avramount>=30 && avramount < 60){
                    avrdaysscore = 60;
                }else if (avramount>=60 && avramount < 80){
                    avrdaysscore = 40;
                }else if (avramount>=80 && avramount < 100){
                    avrdaysscore = 20;
                }else if (avramount>=100){
                    avrdaysscore = 10;
                }
                double totalscore = (avraoumtsoce/100)*30+(maxaoumtscore/100)*30+(avrdaysscore/100)*40;

                String tablename = "userflaginfo";
                String rowkey = userid;
                String famliyname = "baseinfo";
                String colum = "baijiasoce";
                HBaseUtils.putdata(tablename,rowkey,famliyname,colum,totalscore+"");

            }
            env.execute("baijia analyse") ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
