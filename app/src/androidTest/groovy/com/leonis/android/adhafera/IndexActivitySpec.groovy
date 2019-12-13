package com.leonis.android.adhafera

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.widget.TextView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import static android.support.test.espresso.Espresso.onData
import static android.support.test.espresso.Espresso.onView
import static android.support.test.espresso.action.ViewActions.*
import static android.support.test.espresso.assertion.ViewAssertions.matches
import static android.support.test.espresso.matcher.ViewMatchers.*
import static org.hamcrest.Matchers.*
import static junit.framework.Assert.assertTrue
/**
 * Created by leonis on 2018/02/17.
 *
 */
@RunWith(AndroidJUnit4.class)
class IndexActivitySpec {
    def indexActivity
    static fieldIDs = [
            R.id.index_field_date_after,
            R.id.index_field_date_before,
            R.id.index_field_content,
            R.id.index_field_price_upper,
            R.id.index_field_price_lower
    ]

    @Rule
    public ActivityTestRule<IndexActivity> indexActivityRule = new ActivityTestRule<>(IndexActivity.class)

    @Before
    void setUp() {
        indexActivity = indexActivityRule.getActivity()
    }

    @Test
    void testSearchPayments() {
        def queries= ["", "", "", "", ""]
        assertEditText(queries)
        assertSpinner("を含む", "全て")
        assertErrorChecker(TextView.INVISIBLE, TextView.INVISIBLE)

        onView(withId(fieldIDs[0])).perform(click())
        onView(withText("OK")).perform(click())

        onView(withId(R.id.index_field_content_type)).perform(click())
        onData(allOf(is(instanceOf(String.class)), is("と一致する"))).perform(click())

        onView(withId(R.id.index_payment_type)).perform(click())
        onData(allOf(is(instanceOf(String.class)), is("収入"))).perform(click())

        queries[3] = "100"
        queries[4] = "10000"
        inputCondition(queries)
        onView(withId(R.id.index_submit)).perform(click())

        Calendar date = Calendar.getInstance()
        queries[0] = String.format("%d-%02d-%02d", date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DATE))
        assertErrorChecker(TextView.INVISIBLE, TextView.INVISIBLE)
        assertEditText(queries)
        assertSpinner("と一致する", "収入")
    }

    private void assertEditText(def queries) {
        for(int i = 0;i < fieldIDs.size();i++) {
            onView(withId(fieldIDs[i])).check(matches(withText(queries[i])))
        }
    }

    private void assertSpinner(def expectedContentType, def expectedPaymentType) {
        onView(withId(R.id.index_field_content_type)).check(matches(withSpinnerText(expectedContentType)))
        onView(withId(R.id.index_payment_type)).check(matches(withSpinnerText(expectedPaymentType)))
    }

    private void assertErrorChecker(def periodVisibility, def priceVisibility) {
        assertTrue(indexActivity.findViewById(R.id.index_check_period).getVisibility() == periodVisibility)
        assertTrue(indexActivity.findViewById(R.id.index_check_price).getVisibility() == priceVisibility)
    }

    private void inputCondition(def queries) {
        for(int i = 0;i < fieldIDs.size();i++) {
            if(queries[i] != "") {
                onView(withId(fieldIDs[i])).perform(clearText())
                onView(withId(fieldIDs[i])).perform(typeText(queries[i]))
            }
        }
    }
}