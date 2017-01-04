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
 
    private String outputDirectory;
    private List<InputTable> inputTables;

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
        
}
