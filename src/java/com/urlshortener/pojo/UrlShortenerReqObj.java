/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.pojo;

import java.util.*;
import org.springframework.util.StringUtils;

/**
 * Contains all the information of the url shortener request
 * @author Austunner
 */
public class UrlShortenerReqObj {
    
    public final static String URL_KEY = "url";
    
    private Map<String, String> headers;
    private Map<String, Object> requestBodyContent;
    
    public UrlShortenerReqObj(Map<String, String> headers, Map<String, Object> requestContent) {
        this.headers = headers;
        this.requestBodyContent = requestContent;
    }
    
    public Map<String, Object> getRequestBodyContentMap() {
        return requestBodyContent;
    }
    
    public void validateAndCleanupRequest() {
        // check "url" exists.
        if (!this.requestBodyContent.containsKey(URL_KEY)) {
            throw new IllegalArgumentException("missing url to shorten");
        }
        
        // url value must be string
        if (!(this.requestBodyContent.get(URL_KEY) instanceof String)) {
            throw new IllegalArgumentException("url must be string in the data object");
        }
        
        // check "url" starts with "http://" or "www"
        String url = (String)this.requestBodyContent.get(URL_KEY);
        if (!StringUtils.startsWithIgnoreCase(url, "http://") 
                && !StringUtils.startsWithIgnoreCase(url, "www")) {
            throw new IllegalArgumentException("url must starts with http:// or www");
        }
        
    }
}
