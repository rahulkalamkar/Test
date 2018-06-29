package com.android.sampleapp2.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahul.kalamkar on 6/28/2018.
 */

public class Response implements Serializable {
    ArrayList<Character> results;
    String next;

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public ArrayList<Character> getResults() {
        return results;
    }

    public void setResults(ArrayList<Character> results) {
        this.results = results;
    }
}
