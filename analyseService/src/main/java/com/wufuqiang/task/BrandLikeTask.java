package com.wufuqiang.task;

import com.wufuqiang.entity.BrandLike;
import com.wufuqiang.kafka.KafkaEvent;
import com.wufuqiang.kafka.KafkaEventSchema;
import com.wufuqiang.map.BrandLikeMap;
import com.wufuqiang.reduce.BrandLikeReduce;
import com.wufuqiang.sink.BrandLikeSink;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import javax.annotation.Nullable;

/**
 * @ author wufuqiang
 * @ date 2019/4/6/006 - 13:29
 **/
public class BrandLikeTask {
    public static void main(String[] args){
        // parse input arguments
        args = new String[]{"--input-topic",
                "scanProductLog",
                "--bootstrap.servers",
                "10-255-0-242:9092,10-255-0-139:9092,10-255-0-197:2181",
                "--zookeeper.connect",
                "10-255-0-197:2181,10-255-0-192:2181,10-255-0-253:2181",
                "--group.id",
                "branklike"};
        final ParameterTool parameterTool = ParameterTool.fromArgs(args);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment() ;
        env.getConfig().disableSysoutLogging();
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));
        env.enableCheckpointing(5000); // create a checkpoint every 5 seconds
        env.getConfig().setGlobalJobParameters(parameterTool); // make parameters available in the web interface
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<KafkaEvent> input = env
                .addSource(
                        new FlinkKafkaConsumer010<>(
                                parameterTool.getRequired("input-topic"),
                                new KafkaEventSchema(),
                                parameterTool.getProperties())
                                .assignTimestampsAndWatermarks(new CustomWatermarkExtractor()));
        DataStream<BrandLike> brandLikeMap = input.flatMap(new BrandLikeMap());

        DataStream<BrandLike> brandLikeReduce = brandLikeMap.keyBy("groupbyfield").timeWindowAll(Time.seconds(2)).reduce(new BrandLikeReduce());

        brandLikeReduce.addSink(new BrandLikeSink());

        try {
            env.execute("brandLike analyse");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private static class CustomWatermarkExtractor implements AssignerWithPeriodicWatermarks<KafkaEvent> {

        private static final long serialVersionUID = -742759155861320823L;

        private long currentTimestamp = Long.MIN_VALUE;

        @Override
        public long extractTimestamp(KafkaEvent event, long previousElementTimestamp) {
            // the inputs are assumed to be of format (message,timestamp)
            this.currentTimestamp = event.getTimestamp();
            return event.getTimestamp();
        }

        @Nullable
        @Override
        public Watermark getCurrentWatermark() {
            return new Watermark(currentTimestamp == Long.MIN_VALUE ? Long.MIN_VALUE : currentTimestamp - 1);
        }
    }

}
