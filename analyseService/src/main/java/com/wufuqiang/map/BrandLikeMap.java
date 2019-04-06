package com.wufuqiang.map;

import com.wufuqiang.entity.BrandLike;
import com.wufuqiang.kafka.KafkaEvent;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.util.Collector;

/**
 * @ author wufuqiang
 * @ date 2019/4/6/006 - 14:44
 **/
public class BrandLikeMap implements FlatMapFunction<KafkaEvent,BrandLike> {

    @Override
    public void flatMap(KafkaEvent kafkaEvent, Collector<BrandLike> collector) throws Exception {

    }
}
