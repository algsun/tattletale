package com.microwise.tattletale.web;

import com.google.gson.Gson;
import com.microwise.tattletale.config.SocketSessionRegistry;
import com.microwise.tattletale.entity.HelloMessage;
import com.microwise.tattletale.model.AlarmRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Set;


/**
 * WebSocket Demo
 *
 * @author li.jianfei
 * @since 2017/12/11
 */
@Controller
public class GreetingController {

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private SocketSessionRegistry socketSessionRegistry;

    @MessageMapping("/hello")
    public void greeting(HelloMessage message) throws Exception {
        AlarmRecord alarmRecord = new AlarmRecord();
        alarmRecord.setId("1");
        alarmRecord.setState(0);
        alarmRecord.setFactor("着火了");
        alarmRecord.setAlarmtime(new Date());
        Gson gson = new Gson();
        String alarmMessage = gson.toJson(alarmRecord);
        Set<String> sessionIds = socketSessionRegistry.getSessionIds("1");
        for (String sessionId : sessionIds) {
            template.convertAndSendToUser(sessionId, "/topic/greetings", alarmMessage);
        }
    }
}
