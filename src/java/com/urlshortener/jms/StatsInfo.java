/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.jms;

/**
 *
 * @author Austunner
 */
public class StatsInfo {
    private String data;
    
    public StatsInfo(String data) {
        this.data = data;
    }
    
    public String getData() {
        return this.data;
    }
    public void setData(String d) {
        this.data = d;
    }
}
