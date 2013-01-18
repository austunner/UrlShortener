/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.service;

import com.urlshortener.hibernate.pojo.Url;
import com.urlshortener.hibernate.pojo.User;
import com.urlshortener.jms.SendService;
import com.urlshortener.manager.UrlManager;
import com.urlshortener.pojo.UrlShortenerReqObj;
import com.urlshortener.pojo.UrlShortenerReqObj.UrlShortenerRequestType;
import com.urlshortener.pojo.UrlShortenerRespObj;
import com.urlshortener.utils.HttpUtils;
import com.urlshortener.utils.JsonUtils;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Austunner
 */
public class UrlService {
    private Logger log = Logger.getLogger(UrlService.class);
    private final DatastoreService datastoreSvcs;
    private final UrlManager urlManager;
    private final SendService sendService;
    
    public UrlService(DatastoreService datastoreSvcs, SendService sendService) {
        this.datastoreSvcs = datastoreSvcs;
        this.sendService = sendService;
        this.urlManager = new UrlManager(datastoreSvcs);
        
        //testJMS();
    }
    
    private void testJMS() {
        
        log.debug("Testing JMS");
        for(int i =1; i<=5; i++) {
            sendService.sendStats("testing " +i);
        }
    }
    
    /**
     * 
     * @param get
     * ?all&=fetch all from db (latest 200 created)
     * /{shorturl} - this will 302 redirect
     * @param response 
     */
    public UrlShortenerRespObj processGet(UrlShortenerReqObj requestObj) {
        
        UrlShortenerRespObj respObj = new UrlShortenerRespObj();
        
        if (requestObj.getQueryParams().containsKey("all")) {
            
            log.debug("Returning all urls in DB");
            StringBuilder sb = new StringBuilder();
            
            for (Url url : this.urlManager.getAllUrls()) {
                sb.append(url.toString()).append("\n");
            }
            
            respObj.setResponseBody(sb.toString());
            
        } else {
            /// UrlShortener, /heyyo, http://localhost:8080/UrlShortener/heyyo
            // return get.getContextPath() +", " + get.getPathInfo() + ", " + get.getRequestURL().toString();
            Url lookupUrl = urlManager.getUrlByShortUrl(requestObj.getRequestResource());
            
            if (lookupUrl != null) {
                String longUrl = lookupUrl.getUrl();
                
                respObj = new UrlShortenerRespObj(longUrl);
                    
            } else {
                log.debug("Couldn't find the look up url in the datastore: " + requestObj.getRequestResource());
                respObj.setHttpResponseCode(UrlShortenerRespObj.HttpResponseCode.ERROR_NO_URL_FOUND);
                respObj.setResponseBody("Short url DNE in datastore");
            }
            
        }
        
        sendService.sendStats(requestObj.toString());
        return respObj;
    }
    
    /**
     * 
     * @param post - json body with "url" as key
     * @return
     * @throws Exception 
     */
    public UrlShortenerRespObj processPost(UrlShortenerReqObj post) throws Exception {
        
        post.validateAndCleanupRequest();
        
        UrlShortenerRespObj resp = new UrlShortenerRespObj();
        
        // check if url already exists, or create new url and user obj is not already
        
        String urlStr = (String)post.getRequestBodyContentMap().get(UrlShortenerReqObj.URL_KEY);
        
        Url urlObj = this.urlManager.getUrlByTargetUrl(urlStr);
        
        if (urlObj != null) {
            
            resp.setResponseBody(urlStr +" got shrunk to " + urlObj.getUrlshort());
            
        } else {
            
            log.debug("Creating new user, url objects");
            User user = new User();
            user.setIpaddr(post.getRemoteAddr());
            user.setUserAgent(post.getHeaders().get("user-agent"));
            
            String fullTargetUrl = (String)post.getRequestBodyContentMap().get(UrlShortenerReqObj.URL_KEY);
            Url url = new Url();
            url.setDate_created(new Date(System.currentTimeMillis()));
            url.setUser(user);
            url.setUrl(fullTargetUrl);
            url.setUrlshort(generateShortUrl());
            
            this.datastoreSvcs.save(user);
            this.datastoreSvcs.commit();
            
            this.datastoreSvcs.save(url);
            this.datastoreSvcs.commit();
            
            resp.setResponseBody(url.getUrl() + " is shrunken down to " + url.getUrlshort());
        }
        
        sendService.sendStats(post.toString());
        return resp;
    }
    /**
     * http://localhost:8080/UrlShortener/<datastore id>/<short url key>
     * http://localhost:8080/UrlShortener/a/a23vsd
     * 
     * this function will return "/a/a23vsd"
     * @param targetUrl
     * @return 
     */
    private String generateShortUrl() {
        
        // determine which datastore id
        final String datastoreId = "/a/"; // hardcode for now.
        
        String randVar = null;
        // check if randVar clobbers anything.
        int ctr = 0;
        while(ctr < Integer.MAX_VALUE) {
            randVar = generateRandomString(6);
            if (!this.urlManager.isShortUrlExist(datastoreId+randVar)) {
                break;
            }
            log.debug("clobbering..");
            ctr++;
        }
        if (StringUtils.isBlank(randVar)) {
            throw new RuntimeException("Couldn't find unique short url");
        }
        return datastoreId+randVar;
                
    }
    
    private static String generateRandomString(int length) {
        Random r = new Random();
        final String poss = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; // 62
        StringBuilder sb = new StringBuilder();
        
        for (int i=0; i<length; i++) {
            sb.append(poss.charAt(r.nextInt(poss.length())));
        }
        
        return sb.toString();
        
    }
}
