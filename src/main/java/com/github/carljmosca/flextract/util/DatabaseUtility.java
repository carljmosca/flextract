/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.flextract.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author moscac
 */
@Component
public class DatabaseUtility {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    
    

}
