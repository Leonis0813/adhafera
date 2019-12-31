package com.leonis.android.adhafera

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.widget.TextView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import java.text.SimpleDateFormat

import static android.support.test.espresso.Espresso.closeSoftKeyboard
import static android.support.test.espresso.Espresso.onView
import static android.support.test.espresso.action.ViewActions.clearText
import static android.support.test.espresso.action.ViewActions.click
import static android.support.test.espresso.action.ViewActions.typeText
import static android.support.test.espresso.assertion.ViewAssertions.matches
import static android.support.test.espresso.matcher.ViewMatchers.isChecked
import static android.support.test.espresso.matcher.ViewMatchers.withId
import static android.support.test.espresso.matcher.ViewMatchers.withText
import static junit.framework.Assert.assertTrue
/**
 * Created by leonis on 2018/02/17.
 *
 */
@RunWith(AndroidJUnit4.class)
class MainActivitySpec {
    def mainActivity
    static fieldIDs = [
            R.id.create_field_date,
            R.id.create_field_content,
            R.id.create_field_category,
            R.id.create_field_price
    ]
    static checkIDs = [
            R.id.create_check_date,
            R.id.create_check_content,
            R.id.create_check_category,
            R.id.create_check_price
    ]
    def today, settleBefore

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class)

    @Before
    void setUp() {
        mainActivity = mainActivityRule.getActivity()

        def simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
        today = simpleDateFormat.format(new Date())
    }

    @Test
    void testInputCategory() {
        assertStartApplication()

        def texts = ["", "", "test", ""]
        inputPaymentInfo(texts)
        onView(withId(R.id.income)).perform(click())
        onView(withId(R.id.OK)).perform(click())

        def visibilities = [TextView.INVISIBLE, TextView.VISIBLE, TextView.INVISIBLE, TextView.VISIBLE]
        assertRegistration([today, "", "test", ""], "収入", visibilities)

        texts[1] = "data for system test"
        texts[2] = ""
        inputPaymentInfo(texts)
        onView(withId(R.id.OK)).perform(click())

        visibilities[1] = TextView.INVISIBLE
        assertRegistration([today, "data for system test", "test", ""], "収入", visibilities)

        texts[1] = ""
        texts[2] = "test,test2"
        texts[3] = "100"
        inputPaymentInfo(texts)
        onView(withId(R.id.OK)).perform(click())

        visibilities[3] = TextView.INVISIBLE
        assertRegistration([today, "", "", ""], "収入", visibilities)

        def settleAfter = Integer.parseInt(settleBefore.isEmpty() ? "0" : settleBefore) + 100
        assertSettleView(String.valueOf(settleAfter))

    }

    @Test
    void testSelectCategory() {
        assertStartApplication()

        onView(withId(R.id.OK)).perform(click())

        def visibilities = [TextView.INVISIBLE, TextView.VISIBLE, TextView.VISIBLE, TextView.VISIBLE]
        assertRegistration([today, "", "", ""], "支出", visibilities)

        onView(withId(R.id.cancel)).perform(click())

        assertCancel()

        def texts = ["invalid_date", "data for system test", "", "100"]
        inputPaymentInfo(texts)

        onView(withId(R.id.create_select_category)).perform(click())
        onView(withText("test")).perform(click())
        onView(withText("test2")).perform(click())
        onView(withText("OK")).perform(click())

        onView(withId(R.id.OK)).perform(click())

        visibilities[0] = TextView.VISIBLE
        visibilities[1] = TextView.INVISIBLE
        visibilities[2] = TextView.INVISIBLE
        visibilities[3] = TextView.INVISIBLE
        assertRegistration(["invalid_date", "data for system test", "test,test2", "100"], "支出", visibilities)

        texts[0] = today
        texts[1] = ""
        texts[2] = ""
        texts[3] = ""
        visibilities[0] = TextView.INVISIBLE
        inputPaymentInfo(texts)
        onView(withId(R.id.OK)).perform(click())

        assertRegistration([today, "", "", ""], "支出", visibilities)

        def settleAfter = Integer.parseInt(settleBefore.isEmpty() ? "0" : settleBefore) - 100
        assertSettleView(String.valueOf(settleAfter))
    }

    private void assertStartApplication() {
        assertTextInField([today, "", "", ""])
        assertTableButton("支出")
        assertErrorChecker([TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE])
        assertSettleView("")
        settleBefore = ((TextView) mainActivity.findViewById(R.id.result_settle)).getText().toString().replaceAll(" 円", "")
    }

    private void assertRegistration(def inputTexts, def button, def visibilities) {
        assertTextInField(inputTexts)
        assertTableButton(button)
        assertErrorChecker(visibilities)
    }

    private void assertCancel() {
        assertTextInField(["", "", "", ""])
        assertTableButton("支出")
        assertErrorChecker([TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE, TextView.INVISIBLE])
    }

    private void assertTextInField(def texts) {
        for(int i = 0;i < fieldIDs.size();i++) {
            onView(withId(fieldIDs[i])).check(matches(withText(texts[i])))
        }
    }

    private void assertErrorChecker(def visibilities) {
        for(int i = 0;i < checkIDs.size();i++) {
            assertTrue(mainActivity.findViewById(checkIDs[i]).getVisibility() == visibilities[i])
        }
    }

    private void assertTableButton(def text) {
        onView(withText(text)).check(matches(isChecked()))
    }

    private void assertSettleView(def settlement) {
        assertTrue(((TextView) mainActivity.findViewById(R.id.result_settle)).getText().toString().contains(settlement))
    }

    private void inputPaymentInfo(def texts) {
        for(int i = 0;i < fieldIDs.size();i++) {
            if(texts[i] != "") {
                onView(withId(fieldIDs[i])).perform(clearText())
                onView(withId(fieldIDs[i])).perform(typeText(texts[i]))
                closeSoftKeyboard()
            }
        }
    }
}
