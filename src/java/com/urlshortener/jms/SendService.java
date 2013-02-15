/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.jms;
import com.urlshortener.jms.StatsInfo.StatsType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Austunner
 */
@Service("SendService")
public class SendService {
    
    static int orderSequence = 1;
    final private SendManager statsSender;
    final private JmsTemplate jmsTemplate;
    
    public SendService(JmsTemplate template) {
        this.jmsTemplate = template;
        this.statsSender = new SendManager(this.jmsTemplate);
    }
    
    public void sendStats(String stats, StatsType type)
    {
        StatsInfo order = new StatsInfo(stats, type);
        statsSender.pushStats(order);
    }

}
