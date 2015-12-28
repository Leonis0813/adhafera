package com.register.android.test.function;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.register.android.MainActivity;
import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{
	private MainActivity mainActivity;
	private Solo solo;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Before
	public void setUp() throws Exception {
		mainActivity = getActivity();
		solo = new Solo(getInstrumentation(), mainActivity);
	}

	@After
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	@Test
	public void testRegistAccount() {
		String[] inputs = {};
		mainActivity.registAccount(inputs);
	}

	@Test
	public void testNoticeError() {
		mainActivity.noticeError("", null);
	}

	@Test
	public void testNoticeResult() {
		mainActivity.noticeResult("");
	}

}
