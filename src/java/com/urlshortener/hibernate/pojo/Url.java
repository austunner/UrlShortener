/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urlshortener.hibernate.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Austunner
 */
@Entity
@Table(name="URL")
public class Url {
    
    @Id
    @GeneratedValue
    @Column(name="URL_ID")
    private long urlid;
    
    @Column(name="URL")
    private String url;
    
    @Column(name="URL_SHORT")
    private String url_short;
    
    @Temporal(TemporalType.DATE)
    @Column(name="DATE_CREATED")
    private Date date_created;
    
    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;
    
    
    public long getUrlid() {
        return this.urlid;
    }
    
    public void setUrlid(Long id) {
        this.urlid = id;
    }
    
    
    public String getUrl() {
        return this.url;
    }
  
    
    public void setUrl(String url) {
        this.url = url;
    }
    
      
    public void setUrlshort(String url) {
        this.url_short = url;
    }
    
    public String getUrlshort() {
        return this.url_short;
    }
    
    public Date getDate_created() {
        return this.date_created;
    }
    
    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }
    
    
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Url Info:\n\nURL ID:"+ this.urlid)
                .append(", CREATE DATE:"+ this.date_created)
                .append(", USER ID:"+this.user.getUserid())
                .append(", URL:"+ this.url)
                .append(", SHORT URL:" + this.url_short)
                .append("\n\nUser Info:\n")
                .append(this.user.toString());
        
        return sb.toString();
    }
                
}
