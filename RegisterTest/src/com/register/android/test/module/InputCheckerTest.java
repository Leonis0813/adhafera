package com.register.android.test.module;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.register.android.service.InputChecker;

public class InputCheckerTest extends TestCase {
  private InputChecker inputChecker;

  public InputCheckerTest() {}

  @Before
  public void setUp() throws Exception {
    inputChecker = new InputChecker();
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testCheckEmpty_normal() {
    String[] inputs = {"2015-01-01", "テスト用データ", "テスト", "100", "expense"};
    assertTrue(inputChecker.checkEmpty(inputs).isEmpty());
  }

  @Test
  public void testCheckEmpty_exception_priceIsNull() {
    String[] inputs = {"2015-01-01", "テスト用データ", "テスト", null, "expense"};
    ArrayList<Integer> ids = new ArrayList<Integer>();
    ids.add(3);
    assertEquals(ids, inputChecker.checkEmpty(inputs));
  }

  @Test
  public void testCheckEmpty_exception_allInputsIsNull() {
    String[] inputs = {null, null, null, null, null};
    ArrayList<Integer> ids = new ArrayList<Integer>();
    for(int i=0;i<5;i++) {
      ids.add(i);
    }
    assertEquals(ids, inputChecker.checkEmpty(inputs));
  }

  @Test
  public void testCheckDate_normal() {
    assertTrue(inputChecker.checkDate("2015-01-01"));
  }

  @Test
  public void testCheckDate_exception_dateIsInvalid() {
    assertFalse(inputChecker.checkDate("invalid_date"));
  }

  @Test
  public void testCheckPrice_normal() {
    assertTrue(inputChecker.checkPrice("100"));
  }

  @Test
  public void testCheckPrice_exception_priceIsInvalid() {
    assertFalse(inputChecker.checkPrice("invalid_price"));
  }
}
