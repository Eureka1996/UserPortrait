package com.wufuqiang.control;

import com.alibaba.fastjson.JSONObject;
import com.wufuqiang.entity.ResultMessage;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ author wufuqiang
 * @ date 2019/4/5/005 - 16:07
 **/

@RestController
public class InfoinControl {

    public String helloword(HttpServletRequest req){
        String ip = req.getRemoteAddr() ;
        ResultMessage resultMessage = new ResultMessage() ;
        resultMessage.setMessage("hello:"+ip);
        resultMessage.setStatus("success") ;
        String result = JSONObject.toJSONString(resultMessage) ;
        return result ;
    }
}
