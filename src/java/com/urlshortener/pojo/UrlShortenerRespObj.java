/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.pojo;

import org.apache.log4j.Logger;


/**
 *
 * @author Austunner
 */
public class UrlShortenerRespObj {
    
    private Logger log = Logger.getLogger(UrlShortenerRespObj.class);
    private HttpResponseCode httpCode;
    private String redirectUrl;
    private String responseBody;
    
    public UrlShortenerRespObj() {
        this.httpCode = HttpResponseCode.GENERAL_ERROR;
    }
    
    public UrlShortenerRespObj(String redirectUrl) {
        this.httpCode = HttpResponseCode.REDIRECT;
        this.redirectUrl = redirectUrl;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("httpcode=" + httpCode.toString() + "\n"
                + "redirectUrl=" + redirectUrl);
        return sb.toString();
    }
    public void setResponseBody(String body) {
        this.responseBody = body;
    }
    public String getResponseBody() {
        return this.responseBody;
    }
    public HttpResponseCode getHttpResponseCode() {
        return this.httpCode;
    }
    public void setHttpResponseCode(HttpResponseCode e) {
        this.httpCode = e;
    }
    
    public String getRedirectUrl() {
        return redirectUrl;
    }
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    
    public enum HttpResponseCode {
        REDIRECT,
        ERROR_NO_URL_FOUND,
        ERROR_REDIRECT_FAILED,
        GENERAL_ERROR;
        
        public static int getHttpIntCode(HttpResponseCode e) {
            int r = 200;
            switch (e) {
                case REDIRECT:
                    r = 302;
                    break;
                case ERROR_NO_URL_FOUND:
                    r = 503;
                    break;
                case ERROR_REDIRECT_FAILED:
                    r = 502;
                    break;
                case GENERAL_ERROR:
                    r = 504;
                    break;
            }
            return r;
        }
        
    };
}
