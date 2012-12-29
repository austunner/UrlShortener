/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * ServletContextListener representing the root context of the webapp.
 * 
 * @author Austunner
 */
public class WebappContext implements ServletContextListener {
    
    private static final Log log = LogFactory.getLog(WebappContext.class);
    
    private static ServletContext servletContext = null;
    
    
    public static ServletContext getServletContext() {
        return servletContext;
    }
    
    public static ApplicationContext getApplicationContext() {
        return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    }
    
    public static Object getBean(String bean) {
        
        return getApplicationContext().getBean(bean);
    }
    
    
    // --------------------------------------------------- Instance Methods
    
    
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Application Start");
        servletContext = sce.getServletContext();
    }
    
    
    public void contextDestroyed(ServletContextEvent arg0) {
        log.info("Application End");
    }
    
}
