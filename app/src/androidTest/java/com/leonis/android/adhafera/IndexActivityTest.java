package com.leonis.android.adhafera;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by leonis on 2018/02/17.
 *
 */
@RunWith(AndroidJUnit4.class)
public class IndexActivityTest extends ActivityInstrumentationTestCase2<IndexActivity> {
    private IndexActivity indexActivity;
    private int[] fieldIDs;

    public IndexActivityTest() {
        super(IndexActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        indexActivity = getActivity();

        fieldIDs = new int[]{
                R.id.index_field_date_after,
                R.id.index_field_date_before,
                R.id.index_field_content,
                R.id.index_field_price_upper,
                R.id.index_field_price_lower
        };
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testSearchPayments() {
        for (int fieldID : fieldIDs) {
            onView(withId(fieldID)).check(matches(withText("")));
        }

        onView(withId(R.id.index_field_content_type)).check(matches(withSpinnerText("を含む")));
        onView(withId(R.id.index_payment_type)).check(matches(withSpinnerText("全て")));

        assertErrorChecker(TextView.INVISIBLE, TextView.INVISIBLE);

        String[] queries= {"invalid", "", "", "", "invalid"};
        inputCondition(queries);
        onView(withId(R.id.index_submit)).perform(click());
        assertErrorChecker(TextView.VISIBLE, TextView.VISIBLE);

        queries[4] = "100";
        inputCondition(queries);
        onView(withId(R.id.index_submit)).perform(click());
        assertErrorChecker(TextView.VISIBLE, TextView.INVISIBLE);

        queries[0] = "1000-01-01";
        queries[3] = "invalid";
        inputCondition(queries);
        onView(withId(R.id.index_submit)).perform(click());
        assertErrorChecker(TextView.INVISIBLE, TextView.VISIBLE);

        queries[3] = "100";
        queries[4] = "10000";
        inputCondition(queries);
        onView(withId(R.id.index_submit)).perform(click());
        assertErrorChecker(TextView.INVISIBLE, TextView.INVISIBLE);
    }

    private void assertErrorChecker(int periodVisibility, int priceVisibility) {
        assertTrue(indexActivity.findViewById(R.id.index_check_period).getVisibility() == periodVisibility);
        assertTrue(indexActivity.findViewById(R.id.index_check_price).getVisibility() == priceVisibility);
    }

    private void inputCondition(String[] texts) {
        for(int i = 0;i < fieldIDs.length;i++) {
            if(!texts[i].equals("")) {
                onView(withId(fieldIDs[i])).perform(clearText());
                onView(withId(fieldIDs[i])).perform(typeText(texts[i]));
            }
        }
    }
}