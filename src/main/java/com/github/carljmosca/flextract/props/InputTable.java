/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.flextract.props;

import java.util.List;

/**
 *
 * @author moscac
 */
public class InputTable {
    
    private String name;
    private int limitrecords;
    private int skipRecords;
    private List<InputRelatedTable> inputRelatedTables;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLimitrecords() {
        return limitrecords;
    }

    public void setLimitrecords(int limitrecords) {
        this.limitrecords = limitrecords;
    }

    public int getSkipRecords() {
        return skipRecords;
    }

    public void setSkipRecords(int skipRecords) {
        this.skipRecords = skipRecords;
    }

    public List<InputRelatedTable> getInputRelatedTables() {
        return inputRelatedTables;
    }

    public void setInputRelatedTables(List<InputRelatedTable> inputRelatedTables) {
        this.inputRelatedTables = inputRelatedTables;
    }
    
}
