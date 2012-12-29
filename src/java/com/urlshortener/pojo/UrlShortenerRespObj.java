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
    private Error error;
    private String redirectUrl;
    
    public UrlShortenerRespObj(Error error) {
        this.error = error;
    }
    public UrlShortenerRespObj(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    
    
    public Error getError() {
        return this.error;
    }
    public void setError(Error e) {
        this.error = e;
    }
    
    public String getRedirectUrl() {
        return redirectUrl;
    }
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    
    public enum Error {
        ERROR_NO_URL_FOUND,
        ERROR_REDIRECT_FAILED;
        
        public int getHttpErrorCode(Error e) {
            int r = 504;
            switch (e) {
                case ERROR_NO_URL_FOUND:
                    r = 503;
                    
                case ERROR_REDIRECT_FAILED:
                    r = 502;
                    
            }
            return r;
        }
        
    };
}
