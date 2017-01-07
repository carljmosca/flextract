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
        String code = "return value + '!'";
        Object inputValue = "hello";
        GroovyScriptRunner instance = new GroovyScriptRunner();
        Object expResult = inputValue + "!";
        Object result = instance.runGroovyScript(code, inputValue);
        assertEquals(expResult, result);
    }

}
