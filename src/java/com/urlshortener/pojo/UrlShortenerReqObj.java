/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.pojo;

import java.util.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Contains all the information of the url shortener request
 * @author Austunner
 */
public class UrlShortenerReqObj {
    
    public final static String URL_KEY = "url";
    private final static List<String> LOGGABLEDATA_HEADER_KEYS = new ArrayList<String>(3);

    private Map<String, String> headers;
    private Map<String, Object> requestBodyContent;
    
    // user agent, request path
    private Map<String, String> loggableData = new HashMap<String, String>(3);
    private UrlShortenerRequestType requestType;

    static {
        LOGGABLEDATA_HEADER_KEYS.add("user-agent");
    }
    
    public UrlShortenerReqObj(Map<String, String> headers, Map<String, Object> requestContent, String requestPath, UrlShortenerRequestType requestType) {
        this.requestType = requestType;
        this.headers = headers;
        this.requestBodyContent = requestContent;
        
        this.loggableData = getLoggableData();
        this.loggableData.put("request_type", requestType.toString());
        if (StringUtils.isNotBlank(requestPath)) {
            loggableData.put("request_path", requestPath);
        }
    }
    
    public Map<String, Object> getRequestBodyContentMap() {
        return requestBodyContent;
    }
   
    
    
    private Map<String, String> getLoggableData() {
        if (loggableData.isEmpty()) {
            for(String headerKey : LOGGABLEDATA_HEADER_KEYS) {
                if (headers.containsKey(headerKey)) {
                    loggableData.put(headerKey, headers.get(headerKey));
                }
            }
        }
        
        return loggableData;
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
    
    @Override
    public String toString() {
        return this.loggableData.toString();
    }
    
    public enum UrlShortenerRequestType {
        CREATE, LOOKUP;
        
        
    }
}
