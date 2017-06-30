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
  public void testInputCategory() {
    assertStartApplication();

    String[] texts = {"", "", "test", "invalid_price"};
    inputPaymentInfo(texts);
    solo.clickOnView(solo.getView(R.id.income));
    solo.clickOnView(solo.getView(R.id.OK));

    int[] visibilities = {TextView.INVISIBLE, TextView.VISIBLE, TextView.INVISIBLE, TextView.INVISIBLE};
    assertRegistration("内容が入力されていません", new String[]{today, "", "test", "invalid_price"}, "収入", visibilities);

    texts[1] = "data for system test";
    texts[2] = "";
    texts[3] = "";
    inputPaymentInfo(texts);
    solo.clickOnView(solo.getView(R.id.OK));

    visibilities[1] = TextView.INVISIBLE;
    visibilities[3] = TextView.VISIBLE;
    assertRegistration("金額が不正です", new String[]{today, "data for system test", "test", "invalid_price"}, "収入", visibilities);

    texts[1] = "";
    texts[3] = "100";
    inputPaymentInfo(texts);
    solo.clickOnView(solo.getView(R.id.OK));

    visibilities[3] = TextView.INVISIBLE;
    assertRegistration("収支情報を登録しました", new String[]{today, "", "", ""}, "収入", visibilities);

    int settle_after = Integer.parseInt(settle_before) + 100;
    assertSettleView(String.valueOf(settle_after));
  }

  @Test
  public void testSelectCategory() {
    assertStartApplication();

    solo.clickOnView(solo.getView(R.id.OK));

    int[] visibilities = {TextView.INVISIBLE, TextView.VISIBLE, TextView.VISIBLE, TextView.VISIBLE};
    assertRegistration("内容,カテゴリ,金額が入力されていません", new String[]{today, "", "", ""}, "支出", visibilities);

    solo.clickOnView(solo.getView(R.id.cancel));

    assertCancel();

    String[] texts = {"invalid_date", "data for system test", "", "100"};
    inputPaymentInfo(texts);

    solo.clickOnView(solo.getView(R.id.select_category));
    solo.waitForDialogToOpen();
    solo.clickOnText("test");
    solo.clickOnButton("OK");

    solo.clickOnView(solo.getView(R.id.OK));

    visibilities[0] = TextView.VISIBLE;
    visibilities[1] = TextView.INVISIBLE;
    visibilities[2] = TextView.INVISIBLE;
    visibilities[3] = TextView.INVISIBLE;
    assertRegistration("日付が不正です", new String[]{"invalid_date", "data for system test", "test", "100"}, "支出", visibilities);

    texts[0] = today;
    texts[1] = "";
    texts[2] = "";
    texts[3] = "";
    visibilities[0] = TextView.INVISIBLE;
    inputPaymentInfo(texts);
    solo.clickOnView(solo.getView(R.id.OK));

    assertRegistration("収支情報を登録しました", new String[]{today, "", "", ""}, "支出", visibilities);

    int settle_after = Integer.parseInt(settle_before) - 100;
    assertSettleView(String.valueOf(settle_after));
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
    assertTableButton("支出");
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

  private void inputPaymentInfo(String[] texts) {
    for(int i=0;i<fieldIDs.length;i++) {
      if(!texts[i].equals("")) {
        solo.clearEditText((EditText) solo.getView(fieldIDs[i]));
        solo.typeText((EditText) solo.getView(fieldIDs[i]), texts[i]);
      }
    }
  }
}
