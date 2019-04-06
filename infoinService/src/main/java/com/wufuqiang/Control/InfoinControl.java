package com.wufuqiang.Control;

import com.alibaba.fastjson.JSONObject;
import com.wufuqiang.entity.ResultMessage;
import com.wufuqiang.log.AttentionProductLog;
import com.wufuqiang.log.BuyCarProductLog;
import com.wufuqiang.log.CollectProductLog;
import com.wufuqiang.log.ScanProductLog;
import com.wufuqiang.utils.ReadProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping(value = "infolog")
public class InfoinControl {


    private final String attentionProductLogTopic = ReadProperties.getKey("attentionProductLog");
    private final String buyCartProductLogTopic = ReadProperties.getKey("buyCarProductLog");
    private final String collectProductLogTopic = ReadProperties.getKey("collectProductLog");
    private final String scanProductLogTopic = ReadProperties.getKey("scanProductLog");

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping(value="helloworld",method = RequestMethod.GET)
    public String helloworld(HttpServletRequest req){
        String ip = req.getRemoteAddr() ;
        ResultMessage resultMessage = new ResultMessage() ;
        resultMessage.setMessage("hello:"+ip);
        resultMessage.setStatus("success") ;
        String result = JSONObject.toJSONString(resultMessage) ;
        return result ;
    }


    /**
     * AttentionProductLog:{productid:productid....}
     BuyCartProductLog:{productid:productid....}
     CollectProductLog:{productid:productid....}
     * ScanProductLog:{productid:productid....}
     * @param
     * @param req
     * @return
     */
    @RequestMapping(value="receivelog",method = RequestMethod.POST)
    public String helloworld(String receivelog , HttpServletRequest req){

        if(StringUtils.isBlank(receivelog)){
            return null ;
        }

        String[] rearrays = receivelog.split(":",2) ;
        String classname = rearrays[0] ;
        String data = rearrays[1] ;
        String resultmessage = "" ;

        if("AttentionProductLog".equals(classname)){
            AttentionProductLog attentionProductLog = JSONObject.parseObject(data,AttentionProductLog.class);
            resultmessage = JSONObject.toJSONString(attentionProductLog);
            kafkaTemplate.send(attentionProductLogTopic,resultmessage+"##1##"+new Date().getTime());
        }else if("BuyCartProductLog".equals(classname)){
            BuyCarProductLog buyCartProductLog = JSONObject.parseObject(data,BuyCarProductLog.class);
            resultmessage = JSONObject.toJSONString(buyCartProductLog);
            kafkaTemplate.send(buyCartProductLogTopic,resultmessage+"##1##"+new Date().getTime());
        }else if("CollectProductLog".equals(classname)){
            CollectProductLog collectProductLog = JSONObject.parseObject(data,CollectProductLog.class);
            resultmessage = JSONObject.toJSONString(collectProductLog);
            kafkaTemplate.send(collectProductLogTopic,resultmessage+"##1##"+new Date().getTime());
        }else if("ScanProductLog".equals(classname)){
            ScanProductLog scanProductLog = JSONObject.parseObject(data,ScanProductLog.class);
            resultmessage = JSONObject.toJSONString(scanProductLog);
            kafkaTemplate.send(scanProductLogTopic,resultmessage+"##1##"+new Date().getTime());
        }
        ResultMessage resultMessage = new ResultMessage();
        resultMessage.setMessage(resultmessage);
        resultMessage.setStatus("success");
        String result = JSONObject.toJSONString(resultMessage);
        return result;
    }
}
