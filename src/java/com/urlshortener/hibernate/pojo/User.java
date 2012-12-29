/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.hibernate.pojo;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Austunner
 */

@Entity
@Table(name="USER")
public class User {
    
    @Id
    @GeneratedValue
    @Column(name="USER_ID")
    private long userid;
    
    @Column(name="IPADDR")
    private String ipaddr;
    
    @Column(name="USERAGENT")
    private String useragent;
    
    @OneToMany(mappedBy="urlid")
    private Set<Url> urls;
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID="+ userid)
                .append(", IPADDR=" + ipaddr)
                .append(", USERAGENT=" + useragent);
        
        return sb.toString();
                
    }
    
    public long getUserid() {
        return this.userid;
    }
    
    public void setUserid(Long id) {
        this.userid = id;
    }
    
    
    public String getIpaddr() {
        return this.ipaddr;
    }
    
    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }
    
    
    public String getUserAgent() {
        return this.useragent;
    }
    
    public void setUserAgent(String useragent) {
        this.useragent = useragent;
    }
    
    
    public Set<Url> getUrls() {
        return this.urls;
    }
    
    public void setUrls(Set<Url> urls) {
        this.urls = urls;
    }

}
