package com.android.sampleapp2.Model;

import java.io.Serializable;

/**
 * Created by rahul.kalamkar on 6/28/2018.
 */

public class Character implements Serializable {
    String name;
    String height;
    String mass;
    String created;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
