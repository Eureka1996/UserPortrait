package com.wufuqiang.map;

import com.alibaba.fastjson.JSONObject;
import com.wufuqiang.entity.BrandLike;
import com.wufuqiang.kafka.KafkaEvent;
import com.wufuqiang.log.ScanProductLog;
import com.wufuqiang.util.HBaseUtils;
import com.wufuqiang.utils.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;

/**
 * @ author wufuqiang
 * @ date 2019/4/6/006 - 14:44
 **/
public class BrandLikeMap implements FlatMapFunction<KafkaEvent,BrandLike> {

    @Override
    public void flatMap(KafkaEvent kafkaEvent, Collector<BrandLike> collector) throws Exception {
        String data = kafkaEvent.getWord() ;
        ScanProductLog scanProductLog = JSONObject.parseObject(data,ScanProductLog.class) ;

        int userid = scanProductLog.getUserid();
        String brand = scanProductLog.getBrand();

        String tablename = "userflaginfo";
        String rowkey = userid+"";
        String famliyname = "userbehavior";
        String colunm = "brandlist";     //品牌列表

        //HBase中以map格式存放用户购买过的品牌名称{brand:count}
        String mapdata = HBaseUtils.getdata(tablename,rowkey,famliyname,colunm);


        Map<String,Long> map = new HashMap<String,Long>();
        if(StringUtils.isNotBlank(mapdata)){
            map = JSONObject.parseObject(mapdata,Map.class);
        }

        //获取之前的品牌偏好
        String maxprebrand = MapUtils.getmaxbyMap(map);

        long prebarnd = map.get(brand)==null?0L:map.get(brand);
        map.put(brand,prebarnd+1);
        String finalstring = JSONObject.toJSONString(map);
        //更新用户购买过的品牌信息
        HBaseUtils.putdata(tablename,rowkey,famliyname,colunm,finalstring);

        String maxbrand = MapUtils.getmaxbyMap(map);

        //偏好转变，将之前的偏好品牌的count设设置为-1L
        if(StringUtils.isNotBlank(maxbrand)&&!maxbrand.equals(maxprebrand)){
            BrandLike brandLike = new BrandLike();
            brandLike.setBrand(maxprebrand);
            brandLike.setCount(-1L);
            brandLike.setGroupbyfield("brandlike=="+maxprebrand);
            collector.collect(brandLike);
        }


        BrandLike brandLike = new BrandLike();
        brandLike.setBrand(maxbrand);
        brandLike.setCount(1L);
        brandLike.setGroupbyfield("brandlike=="+maxbrand);
        collector.collect(brandLike);

        colunm = "brandlike"; //偏好的品牌
        HBaseUtils.putdata(tablename,rowkey,famliyname,colunm,maxbrand);


    }
}
