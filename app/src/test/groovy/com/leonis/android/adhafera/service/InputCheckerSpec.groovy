package com.leonis.android.adhafera.service

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
/**
 * Created by leonis on 2018/02/20.
 *
 */
class InputCheckerSpec extends Specification {
    @Shared inputChecker

    def setupSpec() {
        inputChecker = new InputChecker()
    }

    @Unroll
    def "checkEmpty([#date, #content, #category, #price, #payment_type]) return #expectedIds"() {
        given:
        String[] inputs = [date, content, category, price, payment_type]

        when:
        def emptyIds = inputChecker.checkEmpty(inputs)

        then:
        emptyIds == expectedIds

        where:
        date         | content | category | price | payment_type || expectedIds
        "2015-01-01" | "test"  | "test"   | "100" | "expense"    || []
        null         | "test"  | "test"   | "100" | "expense"    || [0]
        "2015-01-01" | null    | "test"   | "100" | "expense"    || [1]
        "2015-01-01" | "test"  | null     | "100" | "expense"    || [2]
        "2015-01-01" | "test"  | "test"   | null  | "expense"    || [3]
        "2015-01-01" | "test"  | "test"   | "100" | null         || [4]
        null         | null    | null     | null  | null         || [0, 1, 2, 3, 4]
    }

    @Unroll
    def "checkDate(#date) return #expect"() {
        when:
        def result = inputChecker.isValidDate(date)

        then:
        result == expect

        where:
        date                  || expect
        "2015-01-01"          || true
        "invalid_date"        || false
        "2015/01/01"          || false
        "01-01-2015"          || false
        "2015-01-01 00:00:00" || false
    }

    @Unroll
    def "checkPrice(#price) return #expect"() {
        when:
        def result = inputChecker.isValidPrice(price)

        then:
        result == expect

        where:
        price           || expect
        "1"             || true
        "invalid_price" || false
        "1.0"           || false
        "-1"            || false
    }
}
