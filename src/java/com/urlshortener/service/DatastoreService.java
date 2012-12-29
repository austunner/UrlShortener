/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.service;

import com.urlshortener.hibernate.pojo.Url;
import com.urlshortener.hibernate.pojo.User;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
/**
 * // controls mysql and/or redis/memcached(b) datastores
 * @author Austunner
 */
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
public class DatastoreService {
    
    private SessionFactory sf;
    private Logger log = Logger.getLogger(DatastoreService.class);
    
    public DatastoreService() {
        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(
                configuration.getProperties()).buildServiceRegistry();
        sf = configuration.buildSessionFactory(serviceRegistry);
        
    }
    
    public Session getSession() {
        Session session = sf.getCurrentSession();
        if (session.getTransaction() == null || !session.getTransaction().isActive()) {
            log.debug("get session and start a transaction");
            session.beginTransaction();
        }
        
        return session;
    }
    
    public void save(Object o) throws Exception{
        try {
            Session s = getSession();
            s.save(o);
        } catch (Exception e) {
            release();
            
            throw e;
        }
    }
    
    public void commit() throws Exception {
        try {
            log.debug("flushing and committing db transaction");
            Session s = getSession();
            s.flush();
            s.getTransaction().commit();
        } catch (Exception e) {
            release();
            
            throw e;
        }
    }
    
   
    
    public List<User> getAllUsers() {
        Session s = getSession();
        
        return s.createSQLQuery("SELECT * FROM user")
                .addEntity(User.class)
                .setMaxResults(100)
                .list();
    }
   
    
    public User getUserByIpaddr(String ipaddr) {
        Session s = getSession();
        return (User)s.createSQLQuery("SELECT * FROM user u where u.ipaddr="+ipaddr)
                .addEntity(User.class)
                .uniqueResult();
    }
    
    public User getUserById(String userId) {
        Session s = getSession();
        return (User)s.load(User.class, userId);
    }
    
    
    
    private void release() {
        try {
            Session s = sf.getCurrentSession();
            if (s != null && s.isOpen()) {
                try {
                    Transaction tx = s.getTransaction();
                    
                    if (tx != null && tx.isActive()) {
                        log.debug("Rolling back transaction");
                        tx.rollback();
                    }
                } catch(Exception e) {
                   log.error(e.getMessage());
                } finally {
                    if (s.isOpen()) {
                        s.close();
                    }
                }
            }
        } catch (Exception e) {
            
        }
    }
}
