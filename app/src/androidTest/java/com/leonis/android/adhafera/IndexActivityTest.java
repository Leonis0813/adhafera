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
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

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
        String[] queries= {"", "", "", "", ""};
        assertEditText(queries);
        assertSpinner("を含む", "全て");
        assertErrorChecker(TextView.INVISIBLE, TextView.INVISIBLE);

        queries[0] = "invalid";
        queries[4] = "invalid";
        inputCondition(queries);
        onView(withId(R.id.index_field_content_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("と一致する"))).perform(click());
        onView(withId(R.id.index_payment_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("収入"))).perform(click());
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

        onView(withId(R.id.index_next_page)).perform(click());
        assertEditText(queries);
        assertSpinner("と一致する", "収入");
    }

    private void assertEditText(String[] queries) {
        for(int i = 0;i < fieldIDs.length;i++) {
            onView(withId(fieldIDs[i])).check(matches(withText(queries[i])));
        }
    }

    private void assertSpinner(String expectedContentType, String expectedPaymentType) {
        onView(withId(R.id.index_field_content_type)).check(matches(withSpinnerText(expectedContentType)));
        onView(withId(R.id.index_payment_type)).check(matches(withSpinnerText(expectedPaymentType)));
    }

    private void assertErrorChecker(int periodVisibility, int priceVisibility) {
        assertTrue(indexActivity.findViewById(R.id.index_check_period).getVisibility() == periodVisibility);
        assertTrue(indexActivity.findViewById(R.id.index_check_price).getVisibility() == priceVisibility);
    }

    private void inputCondition(String[] queries) {
        for(int i = 0;i < fieldIDs.length;i++) {
            if(!queries[i].equals("")) {
                onView(withId(fieldIDs[i])).perform(clearText());
                onView(withId(fieldIDs[i])).perform(typeText(queries[i]));
            }
        }
    }
}