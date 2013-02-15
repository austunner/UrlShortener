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
    private StatsType type;
    public enum StatsType {
        CREATE, GET
    }
    public StatsInfo(String data, StatsType type) {
        this.data = data;
        this.type = type;
    }
    
    public StatsType getStatsType() {
        return this.type;
    }
    
    public String getData() {
        return this.data;
    }
    public void setData(String d) {
        this.data = d;
    }
}
