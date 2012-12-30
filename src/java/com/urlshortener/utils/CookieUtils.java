/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Austunner
 */
public class CookieUtils {
    
    private static List<String> RFC_KEYS = new ArrayList<String>(5);
    
    static{
        // case insensitive!
        RFC_KEYS.add("expires");
        RFC_KEYS.add("max-age");
        RFC_KEYS.add("domain");
        RFC_KEYS.add("path");
        RFC_KEYS.add("secure");
        RFC_KEYS.add("httponly");
    }
    private CookieUtils(){
    }
    
    /**
     * Parses header value for cookie attributes, namely the cookie name/value pair
     * @param - value of the "Set-Cookie" header parameters
     * @return - Map with 1 element that is the key-value of that cookie
     * test_cookie=CheckForPermission; path=/; domain=.doubleclick.net; expires=Wed, 22 Feb 2012 01:09:03 GMT
     */
    public static Map<String, String> getValueAsPair(String value){
        List<String> cookie_attrs = Arrays.asList(value.split(";")); //white space maintained
        Map<String, String> ret = new HashMap<String, String>(0);
        for(String s : cookie_attrs){
            s = s.trim();
            String[] pair = s.split("=", 2); //we limit this split to 1 because of the PITAs at doubleclick use a=b|c=e|f=g as a cookie, which is valid.
            //check key isn't a RFC key and thus be considered a cookie key-value
            if(pair.length == 2 && !RFC_KEYS.contains(pair[0].trim())){
                ret.put(pair[0], pair[1]); //found a pair, so we can quit looping
                break;
            }
        }
        return ret;
    }
    /**
     * Parses header value for cookie attributes, namely the cookie expiration
     * We support 2 different expiration date formats and we try both before failing & returning null.
     * @param - value of the "Set-Cookie" header parameters
     */
    public static Date getExpiry(String value){
        
        List<String> cookie_attrs = Arrays.asList(value.split(";")); //white space maintained
        for(String s : cookie_attrs){
            s = s.trim();
            if(s.startsWith("expires=")){
                value = s.replace("expires=", "");
                break;
            }
        }
        // Thu, 20 Feb 2014 23:51:44 GMT
        // Thu, 20-Feb-2014 23:51:44 GMT
        final String format1 = "EEE, dd MMM yyyy HH:mm:ss zzz";
        final String format2 = "EEE, dd-MMM-yyyy HH:mm:ss zzz";
        SimpleDateFormat sdf = new SimpleDateFormat(format1);
        Calendar cal = Calendar.getInstance();
        
        try {
            Date d = sdf.parse(value);
            cal.setTime(d);
            
            return cal.getTime();
            
        } catch (ParseException e) {
            // if we have exception from parsing the first date format type, then try the 2nd supported date format.
            try {
                sdf = new SimpleDateFormat(format2);
                Date d = sdf.parse(value);
                cal.setTime(d);
                
                return cal.getTime();
                
            } catch (ParseException e2) {
                // we're out of options for parsing the date, so cookie with default expiry will be returned to the client (-1 maxage value).
            }
        }
        
        return null;
    }
}
