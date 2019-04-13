package com.leonis.android.adhafera;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by leonis on 2018/02/17.
 *
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    private int[] fieldIDs, checkIDs;
    private String today, settleBefore;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();

        fieldIDs = new int[]{
                R.id.create_field_date,
                R.id.create_field_content,
                R.id.create_field_category,
                R.id.create_field_price
        };
        checkIDs = new int[]{
                R.id.create_check_date,
                R.id.create_check_content,
                R.id.create_check_category,
                R.id.create_check_price
        };

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        today = simpleDateFormat.format(new Date());
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testInputCategory() {
        assertStartApplication();

        String[] texts = {"", "", "test", ""};
        inputPaymentInfo(texts);
        onView(withId(R.id.income)).perform(click());
        onView(withId(R.id.OK)).perform(click());

        int[] visibilities = {TextView.INVISIBLE, TextView.VISIBLE, TextView.INVISIBLE, TextView.VISIBLE};
        assertRegistration(new String[]{today, "", "test", ""}, "収入", visibilities);

        texts[1] = "data for system test";
        texts[2] = "";
        inputPaymentInfo(texts);
        onView(withId(R.id.OK)).perform(click());

        visibilities[1] = TextView.INVISIBLE;
        assertRegistration(new String[]{today, "data for system test", "test", ""}, "収入", visibilities);

        texts[1] = "";
        texts[3] = "100";
        inputPaymentInfo(texts);
        onView(withId(R.id.OK)).perform(click());

        visibilities[3] = TextView.INVISIBLE;
        assertRegistration(new String[]{today, "", "", ""}, "収入", visibilities);

        int settleAfter = Integer.parseInt(settleBefore.isEmpty() ? "0" : settleBefore) + 100;
        assertSettleView(String.valueOf(settleAfter));

    }

    @Test
    public void testSelectCategory() {
        assertStartApplication();

        onView(withId(R.id.OK)).perform(click());

        int[] visibilities = {TextView.INVISIBLE, TextView.VISIBLE, TextView.VISIBLE, TextView.VISIBLE};
        assertRegistration(new String[]{today, "", "", ""}, "支出", visibilities);

        onView(withId(R.id.cancel)).perform(click());

        assertCancel();

        String[] texts = {"invalid_date", "data for system test", "", "100"};
        inputPaymentInfo(texts);

        onView(withId(R.id.create_select_category)).perform(click());
        onView(withText("test")).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.OK)).perform(click());

        visibilities[0] = TextView.VISIBLE;
        visibilities[1] = TextView.INVISIBLE;
        visibilities[2] = TextView.INVISIBLE;
        visibilities[3] = TextView.INVISIBLE;
        assertRegistration(new String[]{"invalid_date", "data for system test", "test", "100"}, "支出", visibilities);

        texts[0] = today;
        texts[1] = "";
        texts[2] = "";
        texts[3] = "";
        visibilities[0] = TextView.INVISIBLE;
        inputPaymentInfo(texts);
        onView(withId(R.id.OK)).perform(click());

        assertRegistration(new String[]{today, "", "", ""}, "支出", visibilities);

        int settleAfter = Integer.parseInt(settleBefore.isEmpty() ? "0" : settleBefore) - 100;
        assertSettleView(String.valueOf(settleAfter));
    }

    private void assertStartApplication() {
        assertTextInField(new String[]{today, "", "", ""});
        assertTableButton("支出");
        assertErrorChecker(new int[]{TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE});
        assertSettleView("");
        settleBefore = ((TextView) mainActivity.findViewById(R.id.result_settle)).getText().toString().replaceAll(" 円", "");
    }

    private void assertRegistration(String[] inputTexts, String button, int[] visibilities) {
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
            onView(withId(fieldIDs[i])).check(matches(withText(texts[i])));
        }
    }

    private void assertErrorChecker(int[] visibilities) {
        for(int i=0;i<checkIDs.length;i++) {
            assertTrue(mainActivity.findViewById(checkIDs[i]).getVisibility() == visibilities[i]);
        }
    }

    private void assertTableButton(String text) {
        onView(withText(text)).check(matches(isChecked()));
    }

    private void assertSettleView(String settlement) {
        assertTrue(((TextView) mainActivity.findViewById(R.id.result_settle)).getText().toString().contains(settlement));
    }

    private void inputPaymentInfo(String[] texts) {
        for(int i=0;i<fieldIDs.length;i++) {
            if(!texts[i].equals("")) {
                onView(withId(fieldIDs[i])).perform(clearText());
                onView(withId(fieldIDs[i])).perform(typeText(texts[i]));
                closeSoftKeyboard();
            }
        }
    }
}