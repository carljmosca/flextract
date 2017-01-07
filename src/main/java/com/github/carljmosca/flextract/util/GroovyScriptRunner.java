/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.flextract.util;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 *
 * @author moscac
 */
public class GroovyScriptRunner {

    public Object runGroovyScript(String code, Object value) {

        Binding binding = new Binding();
        binding.setVariable("value", value);
        GroovyShell shell = new GroovyShell(binding);

        Object result = shell.evaluate(code);

//        Binding binding = new Binding();
//        binding.setProperty("value", inputValue);
//        GroovyShell shell = new GroovyShell(binding);
//        Object o = shell.parse(code);
        return result;
    }

}
