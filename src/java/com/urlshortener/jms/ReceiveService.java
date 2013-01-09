/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.jms;


import com.austunner.log.Logster;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author Austunner
 */
@Component
public class ReceiveService {
    Logster dataLogger = new Logster("/logs/urlshortener-data.log");
    Logger log = Logger.getLogger(ReceiveService.class);
    public ReceiveService() {
        log.debug("initializing ReceiveService...");
    }
    public void receiveStats(Map<String, Object> msg) {
        dataLogger.logIt(msg.get("stats").toString());
        
    }
    
}
