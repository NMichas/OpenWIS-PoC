package com.openwis.poc.model;

import javax.persistence.*;

/**
 * Created by nassos on 5/10/16.
 */
@Entity
@Table(name = "server")
public class Server {
    @Id
    private String id;

    private String servername;

    private String url;

    private String dataset;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

}
