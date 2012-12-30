/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;


/**
 *http://java-diaries.blogspot.com/2011/03/get-started-with-spring-jms-using.html
 * @author Austunner
 */
public class SendManager {
    
    private JmsTemplate jmsTemplate;
    
    public SendManager(JmsTemplate template) {
        this.jmsTemplate = template;
    }
    
    public void pushStats(final StatsInfo order){
        jmsTemplate.send(
                new MessageCreator() {
                    
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        
                        MapMessage mapMessage = session.createMapMessage();
                        mapMessage.setString("stats", order.getData());
                        return mapMessage;
                    }
                }
                );
        System.out.println("Stats sent - data: "+ order.getData());
    }
}
