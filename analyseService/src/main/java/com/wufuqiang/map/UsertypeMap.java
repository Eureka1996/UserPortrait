package com.wufuqiang.map;

import com.alibaba.fastjson.JSONObject;
import com.wufuqiang.entity.BrandLike;
import com.wufuqiang.entity.UsertypeInfo;
import com.wufuqiang.kafka.KafkaEvent;
import com.wufuqiang.log.ScanProductLog;
import com.wufuqiang.util.HBaseUtils;
import com.wufuqiang.utils.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;

/**
 * @ author wufuqiang
 * @ date 2019/4/6/006 - 14:44
 **/
public class UsertypeMap implements FlatMapFunction<KafkaEvent,UsertypeInfo> {

    @Override
    public void flatMap(KafkaEvent kafkaEvent, Collector<UsertypeInfo> collector) throws Exception {
        String data = kafkaEvent.getWord() ;
        ScanProductLog scanProductLog = JSONObject.parseObject(data,ScanProductLog.class) ;

        int userid = scanProductLog.getUserid();
        int usertypeint = scanProductLog.getUsetype(); //终端类型：0、pc端；1、移动端；2、小程序端
        String usertype = usertypeint == 0 ? "PC端" : usertypeint ==1? "移动端" : "小程序端" ;

        String tablename = "userflaginfo";
        String rowkey = userid+"";
        String famliyname = "userbehavior";
        String colunm = "usertype";     //终端名称

        //HBase中以map格式存放用户使用的终端名称{usertype:count}
        String mapdata = HBaseUtils.getdata(tablename,rowkey,famliyname,colunm);


        Map<String,Long> map = new HashMap<String,Long>();
        if(StringUtils.isNotBlank(mapdata)){
            map = JSONObject.parseObject(mapdata,Map.class);
        }

        //获取之前的终端偏好
        String maxpreusertype = MapUtils.getmaxbyMap(map);

        long preusertype = map.get(usertype)==null?0L:map.get(usertype);
        map.put(usertype,preusertype+1);
        String finalstring = JSONObject.toJSONString(map);
        //更新用户使用过的终端信息
        HBaseUtils.putdata(tablename,rowkey,famliyname,colunm,finalstring);

        String maxusertype = MapUtils.getmaxbyMap(map);

        //偏好转变，将之前的偏好品牌的count设设置为-1L
        if(StringUtils.isNotBlank(maxusertype)&&!maxusertype.equals(maxpreusertype)){
            UsertypeInfo usertypeInfo = new UsertypeInfo();
            usertypeInfo.setUsertype(maxpreusertype);
            usertypeInfo.setCount(-1L);
            usertypeInfo.setGroupbyfield("usertype=="+maxpreusertype);
            collector.collect(usertypeInfo);
        }


        UsertypeInfo usertypeInfo = new UsertypeInfo();
        usertypeInfo.setUsertype(maxusertype);
        usertypeInfo.setCount(1L);
        usertypeInfo.setGroupbyfield("usertype=="+maxusertype);
        collector.collect(usertypeInfo);

        colunm = "usertype"; //偏好的品牌
        HBaseUtils.putdata(tablename,rowkey,famliyname,colunm,maxusertype);


    }
}
