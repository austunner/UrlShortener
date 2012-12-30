/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.pojo;

import com.urlshortener.utils.HttpUtils;
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
    private Map<String, Object> bodyContent;
    private String requestResource;
    private Map<String, String> queryParams;
    private String remoteAddr;
    
    // user agent, request path
    private Map<String, String> loggableData = new HashMap<String, String>(3);
    private UrlShortenerRequestType requestType;

    static {
        LOGGABLEDATA_HEADER_KEYS.add("user-agent");
    }
    
    public UrlShortenerReqObj(String remoteAddr, Map<String, String> headers, Map<String, Object> bodyContent, String requestPath, Map<String, String[]> queryParams, UrlShortenerRequestType requestType) {
        this.remoteAddr = remoteAddr;
        this.requestType = requestType;
        this.headers = headers;
        this.bodyContent = bodyContent;
        this.requestResource = requestPath;
        this.queryParams = HttpUtils.getQueryParams(queryParams);
        
        this.loggableData = getLoggableData();
        this.loggableData.put("request_type", requestType.toString());
        if (StringUtils.isNotBlank(requestPath)) {
            loggableData.put("request_path", requestPath);
        }
    }
    
    public String getRemoteAddr() {
        return this.remoteAddr;
    }
    public Map<String, String> getHeaders() {
        return this.headers;
    }
    /**
     * for http GET, this will be the short url.
     * @return 
     */
    public String getRequestResource() {
        return this.requestResource;
    }
    
    public Map<String, Object> getRequestBodyContentMap() {
        return bodyContent;
    }
   
    public Map<String, String> getQueryParams() {
        return queryParams;
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
        if (!this.bodyContent.containsKey(URL_KEY)) {
            throw new IllegalArgumentException("missing url to shorten");
        }
        
        // url value must be string
        if (!(this.bodyContent.get(URL_KEY) instanceof String)) {
            throw new IllegalArgumentException("url must be string in the data object");
        }
        
        // check "url" starts with "http://" or "www"
        String url = (String)this.bodyContent.get(URL_KEY);
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
