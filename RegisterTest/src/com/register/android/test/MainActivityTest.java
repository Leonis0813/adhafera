package com.register.android.test;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.register.android.MainActivity;
import com.register.android.R;
import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{
	private Solo solo;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Before
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@After
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	@Test
	public void testRegistration_normal() {
		assertStartApplication();
		
		EditText editText;
		editText = (EditText) solo.getView(R.id.field_date);
		solo.typeText(editText, "2015-01-01");
		editText = (EditText) solo.getView(R.id.field_content);
		solo.typeText(editText, "data for system test");
		editText = (EditText) solo.getView(R.id.field_category);
		solo.typeText(editText, "test");
		editText = (EditText) solo.getView(R.id.field_price);
		solo.typeText(editText, "100");
		solo.clickOnView(solo.getView(R.id.income));
		
		solo.clickOnView(solo.getView(R.id.OK));
		
		assertToast("‰ÆŒv•ë‚ð“o˜^‚µ‚Ü‚µ‚½");

		int[] fieldIds = {R.id.field_date, R.id.field_content, R.id.field_category , R.id.field_price};
		for(int i=0;i<fieldIds.length;i++) {
			assertTextInField(fieldIds[i], "");
		}

		assertTableButton("Žû“ü");

		int[] checkIds = {R.id.check_date, R.id.check_content, R.id.check_category, R.id.check_price};
		for(int i=0;i<checkIds.length;i++) {
			assertVisibility(checkIds[i], TextView.INVISIBLE);
		}
	}

	@Test
	public void testRegistration_exception_includeEmpty() {
		assertStartApplication();
		
		EditText editText;
		editText = (EditText) solo.getView(R.id.field_date);
		solo.typeText(editText, "2015-01-01");
		editText = (EditText) solo.getView(R.id.field_category);
		solo.typeText(editText, "test");
		editText = (EditText) solo.getView(R.id.field_price);
		solo.typeText(editText, "100");
		solo.clickOnView(solo.getView(R.id.income));
		
		solo.clickOnView(solo.getView(R.id.OK));
		
		assertToast("“à—e‚ª“ü—Í‚³‚ê‚Ä‚¢‚Ü‚¹‚ñ");

		assertTextInField(R.id.field_date, "2015-01-01");
		assertTextInField(R.id.field_content, "");
		assertTextInField(R.id.field_category, "test");
		assertTextInField(R.id.field_price, "100");

		assertTableButton("Žû“ü");

		assertVisibility(R.id.check_date, TextView.INVISIBLE);
		assertVisibility(R.id.check_content, TextView.VISIBLE);
		assertVisibility(R.id.check_category, TextView.INVISIBLE);
		assertVisibility(R.id.check_price, TextView.INVISIBLE);
	}

	@Test
	public void testRegistration_exception_includeInvalidValue() {
		assertStartApplication();
		
		EditText editText;
		editText = (EditText) solo.getView(R.id.field_date);
		solo.typeText(editText, "invalid_date");
		editText = (EditText) solo.getView(R.id.field_content);
		solo.typeText(editText, "data for system test");
		editText = (EditText) solo.getView(R.id.field_category);
		solo.typeText(editText, "test");
		editText = (EditText) solo.getView(R.id.field_price);
		solo.typeText(editText, "100");
		solo.clickOnView(solo.getView(R.id.income));
		
		solo.clickOnView(solo.getView(R.id.OK));
		
		assertToast("“ú•t‚ª•s³‚Å‚·");

		assertTextInField(R.id.field_date, "invalid_date");
		assertTextInField(R.id.field_content, "data for system test");
		assertTextInField(R.id.field_category, "test");
		assertTextInField(R.id.field_price, "100");

		assertTableButton("Žû“ü");

		assertVisibility(R.id.check_date, TextView.VISIBLE);
		assertVisibility(R.id.check_content, TextView.INVISIBLE);
		assertVisibility(R.id.check_category, TextView.INVISIBLE);
		assertVisibility(R.id.check_price, TextView.INVISIBLE);
	}

	@Test
	public void testCancelRegistration_normal() {
		assertStartApplication();

		EditText editText;
		editText = (EditText) solo.getView(R.id.field_date);
		solo.typeText(editText, "2015-01-01");
		editText = (EditText) solo.getView(R.id.field_content);
		solo.typeText(editText, "data for system test");
		editText = (EditText) solo.getView(R.id.field_category);
		solo.typeText(editText, "test");
		editText = (EditText) solo.getView(R.id.field_price);
		solo.typeText(editText, "100");
		solo.clickOnView(solo.getView(R.id.income));

		solo.clickOnView(solo.getView(R.id.cancel));
		assertCancel();
	}

	@Test
	public void testCancelRegistration_normal_includeEmpty() {
		assertStartApplication();

		EditText editText;
		editText = (EditText) solo.getView(R.id.field_date);
		solo.typeText(editText, "2015-01-01");
		editText = (EditText) solo.getView(R.id.field_content);
		solo.typeText(editText, "data for system test");
		solo.clickOnView(solo.getView(R.id.income));

		solo.clickOnView(solo.getView(R.id.cancel));
		assertCancel();
	}

	@Test
	public void testCancelRegistration_normal_includeInvalidValue() {
		assertStartApplication();

		EditText editText;
		editText = (EditText) solo.getView(R.id.field_date);
		solo.typeText(editText, "2015-01-01");
		editText = (EditText) solo.getView(R.id.field_content);
		solo.typeText(editText, "data for system test");
		editText = (EditText) solo.getView(R.id.field_category);
		solo.typeText(editText, "test");
		editText = (EditText) solo.getView(R.id.field_price);
		solo.typeText(editText, "invalid_value");
		solo.clickOnView(solo.getView(R.id.income));

		solo.clickOnView(solo.getView(R.id.cancel));
		assertCancel();
	}

	private void assertStartApplication() {
		assertTrue(solo.waitForActivity(MainActivity.class, 5 * 1000));

		int[] fieldIds = {R.id.field_date, R.id.field_content, R.id.field_category , R.id.field_price};
		for(int i=0;i<fieldIds.length;i++) {
			assertTextInField(fieldIds[i], "");
		}

		assertTableButton("Žxo");

		int[] checkIds = {R.id.check_date, R.id.check_content, R.id.check_category, R.id.check_price};
		for(int i=0;i<checkIds.length;i++) {
			assertVisibility(checkIds[i], TextView.INVISIBLE);
		}
	}

	private void assertCancel() {
		int[] fieldIds = {R.id.field_date, R.id.field_content, R.id.field_category , R.id.field_price};
		for(int i=0;i<fieldIds.length;i++) {
			assertTextInField(fieldIds[i], "");
		}

		assertTableButton("Žû“ü");

		int[] checkIds = {R.id.check_date, R.id.check_content, R.id.check_category, R.id.check_price};
		for(int i=0;i<checkIds.length;i++) {
			assertVisibility(checkIds[i], TextView.INVISIBLE);
		}
	}

	private void assertTextInField(int id, String text) {
		EditText editText = (EditText) solo.getView(id);
		assertTrue(editText.getText().toString().equals(text));
	}

	private void assertVisibility(int id, int visibility) {
		TextView textView = (TextView) solo.getView(id);
		assertEquals(textView.getVisibility(), visibility);
	}

	private void assertTableButton(String text) {
		RadioGroup radioGroup = (RadioGroup) solo.getView(R.id.radiogroup);
		int id = radioGroup.getCheckedRadioButtonId();
		RadioButton radioButton = (RadioButton) solo.getView(id);
		assertTrue(radioButton.getText().toString().equals(text));
	}

	private void assertToast(String text) {
		assertTrue(solo.waitForText(text, 1, 5 * 1000));
	}
}
