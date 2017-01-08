/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.flextract.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author moscac
 */
public class GroovyScriptRunnerTest {

    public GroovyScriptRunnerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of runGroovyScript method, of class GroovyScriptRunner.
     */
    @Test
    public void testRunGroovyScript() {
        System.out.println("runGroovyScript");
        String code = "if (value == null) return null; if (value.length() < 4) return value; return '22-333-' + value.substring(value.length() - 4, value.length())";
        GroovyScriptRunner instance = new GroovyScriptRunner();
        Object inputValue = "123-45-6789";
        Object expResult = "22-333-6789";
        Object result = instance.runGroovyScript(code, inputValue);
        assertEquals(expResult, result);
        inputValue = null;
        assertNull(instance.runGroovyScript(code, inputValue));
        inputValue = "123";
        expResult = "123";
        result = instance.runGroovyScript(code, inputValue);
        assertEquals(expResult, result);
    }

}
