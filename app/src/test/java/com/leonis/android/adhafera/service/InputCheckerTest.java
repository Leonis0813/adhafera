package com.leonis.android.adhafera.service;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by leonis on 2018/02/12.
 *
 */
public class InputCheckerTest {
    private InputChecker inputChecker;

    public InputCheckerTest() {}

    @Before
    public void setUp() throws Exception {
        inputChecker = new InputChecker();
    }

    @Test
    public void testCheckEmpty_OK() {
        String[] inputs = {"2015-01-01", "テスト用データ", "テスト", "100", "expense"};
        assertTrue(inputChecker.checkEmpty(inputs).isEmpty());
    }

    @Test
    public void testCheckEmpty_NG_priceIsNull() {
        String[] inputs = {"2015-01-01", "テスト用データ", "テスト", null, "expense"};
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(3);
        assertEquals(ids, inputChecker.checkEmpty(inputs));
    }

    @Test
    public void testCheckEmpty_NG_allInputsAreNull() {
        String[] inputs = {null, null, null, null, null};
        ArrayList<Integer> ids = new ArrayList<>();
        for(int i=0;i<inputs.length;i++) {
            ids.add(i);
        }
        assertEquals(ids, inputChecker.checkEmpty(inputs));
    }

    @Test
    public void testCheckDate_OK() {
        assertTrue(inputChecker.checkDate("2015-01-01"));
    }

    @Test
    public void testCheckDate_NG() {
        assertFalse(inputChecker.checkDate("invalid"));
    }

    @Test
    public void testCheckPrice_OK() {
        assertTrue(inputChecker.checkPrice("100"));
    }

    @Test
    public void testCheckPrice_NG() {
        assertFalse(inputChecker.checkPrice("-100"));
    }
}