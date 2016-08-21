package com.register.android.test;

import java.text.SimpleDateFormat;
import java.util.Date;

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

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
  private Solo solo;
  private int[] fieldIDs, checkIDs;
  private String today, settle_before;

  public MainActivityTest() {
    super(MainActivity.class);
  }

  @Before
  public void setUp() throws Exception {
    solo = new Solo(getInstrumentation(), getActivity());
    fieldIDs = new int[]{R.id.field_date, R.id.field_content, R.id.field_category, R.id.field_price};
    checkIDs = new int[]{R.id.check_date, R.id.check_content, R.id.check_category, R.id.check_price};

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    today = simpleDateFormat.format(new Date());
  }

  @After
  public void tearDown() throws Exception {
    solo.finishOpenedActivities();
  }

  @Test
  public void testRegistration_normal() {
    assertStartApplication();

    String[] texts = {"", "data for system test", "test", "100"};
    inputAccountInfo(texts);
    solo.clickOnView(solo.getView(R.id.income));

    solo.clickOnView(solo.getView(R.id.OK));
    int[] visibilities = {TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE};
    assertRegistration("家計簿を登録しました", new String[]{today, "", "", ""}, "収入", visibilities);

    int settle_after = Integer.parseInt(settle_before) + 100;
    assertSettleView(String.valueOf(settle_after));
  }

  @Test
  public void testRegistration_exception_includeEmpty() {
    assertStartApplication();

    String[] texts = {"2015-01-01", "", "test", "100"};
    inputAccountInfo(texts);
    solo.clickOnView(solo.getView(R.id.income));

    solo.clickOnView(solo.getView(R.id.OK));
    int[] visibilities = {TextView.INVISIBLE, TextView.VISIBLE, TextView.INVISIBLE, TextView.INVISIBLE};
    assertRegistration("内容が入力されていません", texts, "収入", visibilities);
    assertSettleView(settle_before);
  }

  @Test
  public void testRegistration_exception_includeInvalidValue() {
    assertStartApplication();

    String[] texts = {"invalid_date", "data for system test", "test", "100"};
    inputAccountInfo(texts);
    solo.clickOnView(solo.getView(R.id.income));

    solo.clickOnView(solo.getView(R.id.OK));
    int[] visibilities = {TextView.VISIBLE, TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE};
    assertRegistration("日付が不正です", texts, "収入", visibilities);
    assertSettleView(settle_before);
  }

  @Test
  public void testCancelRegistration_normal() {
    assertStartApplication();

    inputAccountInfo(new String[]{"2015-01-01", "data for system test", "test", "100"});
    solo.clickOnView(solo.getView(R.id.income));

    solo.clickOnView(solo.getView(R.id.cancel));
    assertCancel();
    assertSettleView(settle_before);
  }

  @Test
  public void testCancelRegistration_normal_includeEmpty() {
    assertStartApplication();

    inputAccountInfo(new String[]{"2015-01-01", "data for system test", "", ""});
    solo.clickOnView(solo.getView(R.id.income));

    solo.clickOnView(solo.getView(R.id.cancel));
    assertCancel();
    assertSettleView(settle_before);
  }

  @Test
  public void testCancelRegistration_normal_includeInvalidValue() {
    assertStartApplication();

    inputAccountInfo(new String[]{"2015-01-01", "data for system test", "test", "invalid_price"});
    solo.clickOnView(solo.getView(R.id.income));

    solo.clickOnView(solo.getView(R.id.cancel));
    assertCancel();
    assertSettleView(settle_before);
  }

  private void assertStartApplication() {
    assertTrue(solo.waitForActivity(MainActivity.class, 5 * 1000));
    assertTextInField(new String[]{today, "", "", ""});
    assertTableButton("支出");
    assertErrorChecker(new int[]{TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE});
    assertSettleView("円");
    settle_before = ((TextView) solo.getView(R.id.result_settle)).getText().toString().replaceAll(" 円", "");
  }

  private void assertRegistration(String toast, String[] inputTexts, String button, int[] visibilities) {
    assertTrue(solo.waitForText(toast, 1, 10 * 1000));
    assertTextInField(inputTexts);
    assertTableButton(button);
    assertErrorChecker(visibilities);
  }

  private void assertCancel() {
    assertTextInField(new String[]{"", "", "", ""});
    assertTableButton("収入");
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
  
  private void assertSettleView(String settlement) {
    assertTrue(((TextView) solo.getView(R.id.result_settle)).getText().toString().contains(settlement));
  }

  private void inputAccountInfo(String[] texts) {
    for(int i=0;i<fieldIDs.length;i++) {
      if(!texts[i].equals("")) {
        solo.clearEditText((EditText) solo.getView(fieldIDs[i]));
        solo.typeText((EditText) solo.getView(fieldIDs[i]), texts[i]);
      }
    }
  }
}
