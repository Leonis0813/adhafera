package com.register.android.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.register.android.MainActivity;
import com.register.android.R;
import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{
	private Solo solo;
	private int[] fieldIDs, checkIDs;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Before
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		fieldIDs = new int[]{R.id.field_date, R.id.field_content, R.id.field_category, R.id.field_price};
		checkIDs = new int[]{R.id.check_date, R.id.check_content, R.id.check_category, R.id.check_price};
	}

	@After
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	@Test
	public void testRegistration_normal() {
		assertStartApplication();

		String[] texts = {"2015-01-01", "data for system test", "test", "100"};
		inputAccountInfo(texts);
		solo.clickOnView(solo.getView(R.id.income));

		solo.clickOnView(solo.getView(R.id.OK));
		int[] visibilities = {TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE};
		assertRegistration("â∆åvïÎÇìoò^ÇµÇ‹ÇµÇΩ", new String[]{"", "", "", ""}, "é˚ì¸", visibilities);
	}

	@Test
	public void testRegistration_exception_includeEmpty() {
		assertStartApplication();

		String[] texts = {"2015-01-01", "", "test", "100"};
		inputAccountInfo(texts);
		solo.clickOnView(solo.getView(R.id.income));

		solo.clickOnView(solo.getView(R.id.OK));
		int[] visibilities = {TextView.INVISIBLE, TextView.VISIBLE, TextView.INVISIBLE, TextView.INVISIBLE};
		assertRegistration("ì‡óeÇ™ì¸óÕÇ≥ÇÍÇƒÇ¢Ç‹ÇπÇÒ", texts, "é˚ì¸", visibilities);
	}

	@Test
	public void testRegistration_exception_includeInvalidValue() {
		assertStartApplication();

		String[] texts = {"invalid_date", "data for system test", "test", "100"};
		inputAccountInfo(texts);
		solo.clickOnView(solo.getView(R.id.income));

		solo.clickOnView(solo.getView(R.id.OK));
		int[] visibilities = {TextView.VISIBLE, TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE};
		assertRegistration("ì˙ïtÇ™ïsê≥Ç≈Ç∑", texts, "é˚ì¸", visibilities);
	}

	@Test
	public void testCancelRegistration_normal() {
		assertStartApplication();

		inputAccountInfo(new String[]{"2015-01-01", "data for system test", "test", "100"});
		solo.clickOnView(solo.getView(R.id.income));

		solo.clickOnView(solo.getView(R.id.cancel));
		assertCancel();
	}

	@Test
	public void testCancelRegistration_normal_includeEmpty() {
		assertStartApplication();

		inputAccountInfo(new String[]{"2015-01-01", "data for system test", "", ""});
		solo.clickOnView(solo.getView(R.id.income));

		solo.clickOnView(solo.getView(R.id.cancel));
		assertCancel();
	}

	@Test
	public void testCancelRegistration_normal_includeInvalidValue() {
		assertStartApplication();

		inputAccountInfo(new String[]{"2015-01-01", "data for system test", "test", "invalid_price"});
		solo.clickOnView(solo.getView(R.id.income));

		solo.clickOnView(solo.getView(R.id.cancel));
		assertCancel();
	}

	private void assertStartApplication() {
		assertTrue(solo.waitForActivity(MainActivity.class, 5 * 1000));
		assertTextInField(new String[]{"", "", "", ""});
		assertTableButton("éxèo");
		assertErrorChecker(new int[]{TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE});
	}

	private void assertRegistration(String toast, String[] inputTexts, String button, int[] visibilities) {
		assertTrue(solo.waitForText(toast, 1, 10 * 1000));
		assertTextInField(inputTexts);
		assertTableButton(button);
		assertErrorChecker(visibilities);
	}
	
	private void assertCancel() {
		assertTextInField(new String[]{"", "", "", ""});
		assertTableButton("é˚ì¸");
		assertErrorChecker(new int[]{TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE});
	}

	private void assertTextInField(String[] texts) {
		for(int i=0;i<fieldIDs.length;i++) {
			assertTrue(((EditText) solo.getView(fieldIDs[i])).getText().toString().equals(texts[i]));
		}
	}

	private void assertErrorChecker(int[] visibilities) {
		for(int i=0;i<checkIDs.length;i++) {
			assertEquals(((TextView) solo.getView(checkIDs[i])).getVisibility(), visibilities[i]);
		}
	}

	private void assertTableButton(String text) {
		int id = ((RadioGroup) solo.getView(R.id.radiogroup)).getCheckedRadioButtonId();
		assertTrue(((RadioButton) solo.getView(id)).getText().toString().equals(text));
	}

	private void inputAccountInfo(String[] texts) {
		for(int i=0;i<fieldIDs.length;i++) {
			if(!texts[i].equals("")) {
				solo.typeText((EditText) solo.getView(fieldIDs[i]), texts[i]);
			}
		}
	}
}
