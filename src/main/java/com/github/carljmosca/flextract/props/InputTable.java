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

    protected String name;
    protected String whereClause;
    protected int limitRecords;
    protected int skipRecords;
    protected List<InputTable> inputRelatedTables;
    protected List<TableReference> tableReferences;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public int getLimitRecords() {
        return limitRecords;
    }

    public void setLimitRecords(int limitRecords) {
        this.limitRecords = limitRecords;
    }

    public int getSkipRecords() {
        return skipRecords;
    }

    public void setSkipRecords(int skipRecords) {
        this.skipRecords = skipRecords;
    }

    public List<InputTable> getInputRelatedTables() {
        return inputRelatedTables;
    }

    public void setInputRelatedTables(List<InputTable> inputRelatedTables) {
        this.inputRelatedTables = inputRelatedTables;
    }

    public List<TableReference> getTableReferences() {
        return tableReferences;
    }

    public void setTableReferences(List<TableReference> tableReferences) {
        this.tableReferences = tableReferences;
    }
}
