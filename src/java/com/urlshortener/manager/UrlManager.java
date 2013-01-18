/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.manager;

import com.urlshortener.hibernate.pojo.Url;
import com.urlshortener.service.DatastoreService;
import java.math.BigInteger;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author Austunner
 */
public class UrlManager {
    
    private Logger log = Logger.getLogger(UrlManager.class);
    private DatastoreService datastoreSvcs;
    
    public UrlManager(DatastoreService datastoreSvcs) {
        this.datastoreSvcs = datastoreSvcs;
    }
    
    /**
     * Latest 200
     * @return 
     */
    public List<Url> getAllUrls() {
        Session s = datastoreSvcs.getSession();
        
        return s.createSQLQuery("SELECT * FROM url u ORDER BY u.date_created ASC")
                .addEntity(Url.class)
                .setMaxResults(100)
                .list();
    }
    
    public Url getUrlById(long urlId) {
        Session s = datastoreSvcs.getSession();
        return (Url)s.load(Url.class, urlId);
        
    }
    
    // e.g. http://localhost:8080/UrlShortener/abcde
    // url will be passed in as "/abcde" by UrlService.processGet(), and is saved in the db as such.
    public Url getUrlByShortUrl(String url) {
        Session s = datastoreSvcs.getSession();
        return (Url)s.createSQLQuery("SELECT * FROM url u where u.url_short='"+url+"'")
                .addEntity(Url.class)
                .uniqueResult();
        
    }
    
    public Url getUrlByTargetUrl(String url) {
        Session s = datastoreSvcs.getSession();
        return (Url)s.createSQLQuery("SELECT * from url u where u.url='"+url+"'")
                .addEntity(Url.class)
                .uniqueResult();
        
    }
    
    public boolean isShortUrlExist(String url) {
        Session s = datastoreSvcs.getSession();
        int count = ((BigInteger)s.createSQLQuery("SELECT count(*) from url where url.url_short='" +url+ "' limit 1").uniqueResult()).intValue();
        return count > 0 ? true : false;
    }

}
