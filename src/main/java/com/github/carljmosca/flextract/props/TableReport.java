/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.flextract.props;

/**
 *
 * @author moscac
 */
public class TableReport {
    
    private String tableName;
    private boolean referenced;

    public TableReport(String tableName, boolean referenced) {
        this.tableName = tableName;
        this.referenced = referenced;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isReferenced() {
        return referenced;
    }

    public void setReferenced(boolean referenced) {
        this.referenced = referenced;
    }
    
}
