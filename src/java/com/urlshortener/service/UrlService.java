/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.service;

import com.urlshortener.hibernate.pojo.Url;
import com.urlshortener.hibernate.pojo.User;
import com.urlshortener.manager.UrlManager;
import com.urlshortener.pojo.UrlShortenerReqObj;
import com.urlshortener.utils.HttpUtils;
import com.urlshortener.utils.JsonUtils;
import java.io.BufferedReader;
import java.io.IOException;
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
    
    public UrlService(DatastoreService datastoreSvcs) {
        this.datastoreSvcs = datastoreSvcs;
        this.urlManager = new UrlManager(datastoreSvcs);
    }
    
    /**
     * 
     * @param get
     * @param response 
     */
    public void processGet(HttpServletRequest get, HttpServletResponse response) {
        Cookie[] cookies = get.getCookies();
        Map<String, String> headers = HttpUtils.getHeaders(get);
        
        Map<String, Object> paramMap = get.getParameterMap();
        log.debug("HTTP GET params: " + paramMap);
        
        UrlShortenerReqObj pojo = new UrlShortenerReqObj(headers, paramMap);
        
        if (pojo.getRequestBodyContentMap().containsKey("urlid")) {
            String urlid = ((String[])pojo.getRequestBodyContentMap().get("urlid"))[0];
            
            
            Url lookupUrl = this.urlManager.getUrlById(Long.parseLong(urlid));
            
            log.debug("Looked up by url id " + urlid +" for: " + lookupUrl);
            
            try {
                PrintWriter out = response.getWriter();
                out.println("found " + lookupUrl);
                out.flush();
                out.close();
            } catch (Exception e) {
                response.setStatus(503);
                log.error(e);
            }
        }
        
        else {
            /// UrlShortener, /heyyo, http://localhost:8080/UrlShortener/heyyo
            // return get.getContextPath() +", " + get.getPathInfo() + ", " + get.getRequestURL().toString();
            Url lookupUrl = urlManager.getUrlByShortUrl(get.getPathInfo());
            
            if (lookupUrl != null) {
                String longUrl = lookupUrl.getUrl();
                try {
                    response.sendRedirect(longUrl);
                } catch (Exception e) {
                    response.setStatus(504);
                    log.error("exception", e);
                    
                }
            } else {
                log.debug("Couldn't find the look up url in the datastore: " + lookupUrl.getUrl());
                response.setStatus(503);
            }
            
        }
    }
    
    public String processPost(HttpServletRequest post) throws Exception {
        Cookie[] cookies = post.getCookies();
        Map<String, String> headers = HttpUtils.getHeaders(post);
        
        BufferedReader bf = post.getReader();
        Map<String, Object> content = JsonUtils.parseToMap(bf);
        
        log.debug("HTTP POST content: " + content);
        
        UrlShortenerReqObj pojo = new UrlShortenerReqObj(headers, content);
        pojo.validateAndCleanupRequest();
        
        
        // Handle query that requests for ALL objects
        
        if (post.getParameterMap().containsKey("all")) {
            
            String[] vals = (String[])post.getParameterMap().get("all");
            if (vals.length == 1) {
                
                String val = vals[0];
                if (StringUtils.equalsIgnoreCase(val, "user")) {
                    
                }
            }
            
            log.debug("Returning all urls in DB");
            StringBuilder sb = new StringBuilder();
            for (Url url : this.urlManager.getAllUrls()) {
                sb.append(url.toString()).append("\n");
            }
            return sb.toString();
        }
        
        // check if url already exists, or create new url and user obj is not already
        
        String urlStr = (String)pojo.getRequestBodyContentMap().get(UrlShortenerReqObj.URL_KEY);
        Url urlObj = this.urlManager.getUrlByTargetUrl(urlStr);
        
        if (urlObj != null) {
            log.debug("Url exists already");
            
            return urlObj.toString();
            
        } else {
            log.debug("Creating new user, url objects");
            User user = new User();
            user.setIpaddr(post.getRemoteAddr());
            user.setUserAgent(headers.get("user-agent"));
            //user.setUserAgent(post.getHeader("User-Agent"));
            
            String fullTargetUrl = (String)pojo.getRequestBodyContentMap().get(UrlShortenerReqObj.URL_KEY);
            Url url = new Url();
            url.setDate_created(new Date(System.currentTimeMillis()));
            url.setUser(user);
            url.setUrl(fullTargetUrl);
            url.setUrlshort(generateShortUrl((String)pojo.getRequestBodyContentMap().get(UrlShortenerReqObj.URL_KEY)));
            this.datastoreSvcs.save(user);
            
            this.datastoreSvcs.commit();
            
            this.datastoreSvcs.save(url);
            this.datastoreSvcs.commit();
            
            return "Created new database entries: " + url.toString();
        }
        
    }
    /**
     * http://localhost:8080/UrlShortener/<datastore id>/<short url key>
     * http://localhost:8080/UrlShortener/a/a23vsd
     * 
     * this function will return "/a/a23vsd"
     * @param targetUrl
     * @return 
     */
    private String generateShortUrl(String targetUrl) {
        
        // determine which datastore id
        final String datastoreId = "/a/"; // hardcode for now.
        
        
        return datastoreId+generateRandomString(6);
                
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
