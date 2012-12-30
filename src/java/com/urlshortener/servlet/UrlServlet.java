/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.servlet;

import com.urlshortener.WebappContext;
import com.urlshortener.pojo.UrlShortenerReqObj;
import com.urlshortener.pojo.UrlShortenerRespObj;
import com.urlshortener.pojo.UrlShortenerRespObj.HttpResponseCode;
import com.urlshortener.service.UrlService;
import com.urlshortener.utils.HttpUtils;
import com.urlshortener.utils.JsonUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Austunner
 */
public class UrlServlet extends HttpServlet {
    
    private Logger log = Logger.getLogger(UrlServlet.class);
    private UrlService urlservice = null;
    
    @Override
    public void init() {
        log.debug("Into servlet");
        try {
            urlservice = (UrlService) WebappContext.getBean("UrlService");
        } catch (Exception e) {
            log.error(e);
        }
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            log.debug("params: " + request.getParameterMap());
            Map<String, String> headers = HttpUtils.getHeaders(request);
            UrlShortenerReqObj requestObj = new UrlShortenerReqObj(request.getRemoteAddr(), headers, request.getParameterMap(), request.getPathInfo(), request.getParameterMap(), UrlShortenerReqObj.UrlShortenerRequestType.LOOKUP);
            
            UrlShortenerRespObj resp = urlservice.processGet(requestObj);
            
            if (HttpResponseCode.REDIRECT == resp.getHttpResponseCode()) {
                response.sendRedirect(resp.getRedirectUrl());
            } else {
                
                response.setStatus(HttpResponseCode.getHttpIntCode(resp.getHttpResponseCode()));
                PrintWriter out = response.getWriter();
                out.println(resp.getResponseBody());
                out.flush();
                out.close();
            }
            
        } catch (Exception e) {
            log.error("Exception, so default 500 response code", e);
            response.setStatus(500);
        }
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UrlShortenerRespObj respObj = new UrlShortenerRespObj();
        try {
            
            Map<String, String> headers = HttpUtils.getHeaders(request);
            BufferedReader bf = request.getReader();
            Map<String, Object> content = JsonUtils.parseToMap(bf);
            
            log.debug("HTTP POST content: " + content);
            
            UrlShortenerReqObj requestObj = new UrlShortenerReqObj(request.getRemoteAddr(), headers, content, request.getPathInfo(), request.getParameterMap(), UrlShortenerReqObj.UrlShortenerRequestType.CREATE);
            
            respObj = urlservice.processPost(requestObj);
            
              
            PrintWriter out = response.getWriter();
            out.println(respObj.getResponseBody());
            out.flush();
            out.close();
            
        } catch (Exception e) {
            log.error("Uhh damn!", e);
            respObj.setHttpResponseCode(HttpResponseCode.GENERAL_ERROR);
        }
     
    }
}
