package com.register.android.test.function;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import com.register.android.MainActivity;
import com.register.android.view.RegistrationView;
import com.robotium.solo.Solo;

public class RegistrationViewTest extends ActivityInstrumentationTestCase2<MainActivity>{
	private RegistrationView registrationView;
	private Solo solo;

	public RegistrationViewTest() {
		super(MainActivity.class);
	}

	@Before
	public void setUp() throws Exception {
		MainActivity mainActivity = getActivity();
		registrationView = new RegistrationView(mainActivity);
		solo = new Solo(getInstrumentation(), mainActivity);
	}

	@After
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	@Test
	public void testShowMessage() {
		registrationView.showMessage("");
	}

	@Test
	public void testShowWrongInput() {
		registrationView.showWrongInput(null);
	}

}
