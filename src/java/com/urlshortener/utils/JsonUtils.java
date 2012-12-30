/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;


/**
 *
 * @author Austunner
 */
public final class JsonUtils {
    
    final static ObjectMapper jom;
    
    static {
        jom = new ObjectMapper();
    }
    
    private JsonUtils() {}
    
    public static Map<String, Object> parseToMap(Reader reader) {
        try {
            Map<String, Object> map = jom.readValue(reader, Map.class);
            
            return map;
        } catch (Exception e) {
            return null;
        }
    }
}
