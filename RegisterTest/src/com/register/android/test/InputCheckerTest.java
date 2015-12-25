package com.register.android.test;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.register.android.InputChecker;

public class InputCheckerTest extends TestCase{
	private InputChecker inputChecker;
	
	public InputCheckerTest() {}

	@Before
	public void setUp() throws Exception {
		inputChecker = new InputChecker();
	}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testCheckEmpty() {
		String[] inputs = {"2015-01-01", "テスト用データ", "テスト", "100", "expense"};
		assertTrue(inputChecker.checkEmpty(inputs).isEmpty());
	}

	@Test
	public void testCheckDate() {
		assertTrue(inputChecker.checkDate("2015-01-01"));
	}

	@Test
	public void testCheckPrice() {
		assertTrue(inputChecker.checkPrice("100"));
	}

}
