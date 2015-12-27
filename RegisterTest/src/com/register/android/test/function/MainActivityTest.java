package com.register.android.test.function;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.register.android.MainActivity;

import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOnCreateBundle() {
		fail("Not yet implemented");
	}

	@Test
	public void testRegistAccount() {
		fail("Not yet implemented");
	}

	@Test
	public void testNoticeError() {
		fail("Not yet implemented");
	}

	@Test
	public void testNoticeResult() {
		fail("Not yet implemented");
	}

}
