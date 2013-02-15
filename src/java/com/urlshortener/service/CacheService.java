/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import net.spy.memcached.CASMutation;
import net.spy.memcached.CASMutator;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.GetFuture;
import net.spy.memcached.transcoders.SerializingTranscoder;
import net.spy.memcached.transcoders.Transcoder;
import org.apache.log4j.Logger;

/**
 *
 * @author Austunner
 */
public class CacheService {
    
    public static final String KEY_RECENTLY_CREATED = "recentlycreated";
    public static final String KEY_RECENTLY_ACCESS = "recentlyaccessed";
    
    private MemcachedClient client;
    private Logger log = Logger.getLogger(CacheService.class);

    public CacheService() {
        try {
            client = new MemcachedClient(
                    new InetSocketAddress("localhost", 8081));
        } catch (IOException ex) {
            log.error(ex);
        }
    }

    public Object get(String key) {
        GetFuture<Object> f =client.asyncGet(key);
        Object o = null;
        try {
            o = f.get(2, TimeUnit.SECONDS);
            
        } catch (Exception e) {
            log.error(e);
        } finally {
            f.cancel(false);
        }
        
        return o;
    }
    
    /**
     * expires in 24hrs
     * @param key
     * @param value 
     */
    public void set(String key, Object value) {
        client.set(key, 5*60, value);
        
        
    }
    
    /**
     * Appends only if the value is unique
     * @param key
     * @param value
     * @param maxSize
     * @return
     * @throws Exception 
     */
    public Object append(String key, final Object value, final int maxSize) throws Exception {
        CASMutation<List<Object>> mutation = new CASMutation<List<Object>> () {

            @Override
            public List<Object> getNewValue(List<Object> t) {
                // only append if unique
                if (t != null && t.contains(value)) {
                    return t;
                }
                LinkedList<Object> ll = new LinkedList<Object>(t);
                if (t.size() >= maxSize) {
                    ll.removeFirst();
                }
                ll.addLast(value);
                return ll;
            }
            
        };
        
        CASMutator mutator = new CASMutator(client, new SerializingTranscoder());
        return mutator.cas(key, Collections.singletonList(value), 5*60, mutation);
    }
}
