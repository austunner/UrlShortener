/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Austunner
 */
public final class HttpUtils {
    
    private HttpUtils() {
        
    }
    
    public static Map<String, String> getHeaders(HttpServletRequest request) {
       Map<String,String> headers = new HashMap();
        
        Enumeration head = request.getHeaderNames();
        while (head.hasMoreElements()) {
            String name = (String) head.nextElement();
            headers.put(name, request.getHeader(name));
        }
        
        return headers; 
    }
    
}
