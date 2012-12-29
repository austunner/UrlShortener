/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.servlet;

import com.urlshortener.WebappContext;
import com.urlshortener.service.UrlService;
import java.io.IOException;
import java.io.PrintWriter;
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        log.debug("params: " + request.getParameterMap());
        urlservice.processGet(request, response);
        
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String msg = "POST processed";
        try {
            msg = urlservice.processPost(request);
        } catch (Exception e) {
            log.error("", e);
            msg = "Exception";
        }
        PrintWriter out = response.getWriter();
        out.println(msg);
        out.flush();
        out.close();
    }
}
