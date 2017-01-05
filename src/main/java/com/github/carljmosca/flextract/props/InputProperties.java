/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.flextract.props;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author moscac
 */
@Configuration
@ConfigurationProperties(prefix = "input")
public class InputProperties {

    private final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    private final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private String outputDirectory;
    private List<InputTable> inputTables;
    private String dateFormat;
    private String timeFormat;
    private String dateTimeFormat;
    protected int limitRecords;
    protected int skipRecords;

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public List<InputTable> getInputTables() {
        return inputTables;
    }

    public void setInputTables(List<InputTable> inputTables) {
        this.inputTables = inputTables;
    }

    public String getDateFormat() {
        if (dateFormat == null || dateFormat.isEmpty()) {
            return DEFAULT_DATE_FORMAT;
        }
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getTimeFormat() {
        if (timeFormat == null || timeFormat.isEmpty()) {
            return DEFAULT_TIME_FORMAT;
        }
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getDateTimeFormat() {
        if (dateTimeFormat == null || dateTimeFormat.isEmpty()) {
            return DEFAULT_DATE_TIME_FORMAT;
        }
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
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

}
